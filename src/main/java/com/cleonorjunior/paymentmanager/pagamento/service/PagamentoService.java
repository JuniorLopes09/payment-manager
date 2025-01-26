package com.cleonorjunior.paymentmanager.pagamento.service;

import com.cleonorjunior.paymentmanager.configuration.exception.BusinessException;
import com.cleonorjunior.paymentmanager.configuration.exception.RegisterNotFoundException;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.HistoricoProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.FiltroPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.mapper.PagamentoMapper;
import com.cleonorjunior.paymentmanager.pagamento.repository.HistoricoProcessamentoRepository;
import com.cleonorjunior.paymentmanager.pagamento.repository.PagamentoRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagamentoService {

    Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    private final PagamentoRepository pagamentoRepository;

    private final PagamentoMapper pagamentoMapper;

    private final HistoricoProcessamentoRepository historicoProcessamentoRepository;


    public static final String PAGAMENTO_JA_EXISTE_MESSAGE = "Pagamento já existente";

    public static final String ALTERAR_PARA_MESMO_STATUS = "Pagamento já possui o status %s";

    public static final String PAGAMENTO_JA_PROCESSADO = "O pagamento já foi processado";

    public static final String ALTERAR_STATUS_INVALIDO = "Pagamento com status %s não aceita o status %s";

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            PagamentoMapper pagamentoMapper,
            HistoricoProcessamentoRepository historicoProcessamentoRepository
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.pagamentoMapper = pagamentoMapper;
        this.historicoProcessamentoRepository = historicoProcessamentoRepository;
    }

    public PagamentoResponse findById(Integer codigo) {
        return pagamentoRepository
                .findByCodigoAndExcluido(codigo, Boolean.FALSE)
                .map(pagamentoMapper::mapToResponse)
                .orElseThrow(() -> {
                    logger.warn("findById - Pagamento#{}: não encontrado", codigo);
                    return new RegisterNotFoundException(codigo);
                });
    }

    public Page<PagamentoResponse> findAll(FiltroPagamentoRequest filtroPagamento, Pageable paginacao) {

        Specification<Pagamento> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtroPagamento.getCodigo() != null) {
                predicates.add(criteriaBuilder.equal(root.get("codigo"), filtroPagamento.getCodigo()));
            }

            if (filtroPagamento.getCpfCnpjPagador() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cpfCnpjPagador"), filtroPagamento.getCpfCnpjPagador()));
            }

            if (filtroPagamento.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filtroPagamento.getStatus()));
            }

            predicates.add(criteriaBuilder.equal(root.get("excluido"), Boolean.FALSE));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Pagamento> page = pagamentoRepository.findAll(specification, paginacao);

        if (page.isEmpty()) {
            logger.warn("Não foi possível encontrar nenhum pagamento com os parâmetros: {} {}", filtroPagamento, paginacao);
        }

        return page.map(pagamentoMapper::mapToResponse);
    }

    @Transactional
    public PagamentoResponse save(PagamentoRequest pagamentoRequest) {

        if(pagamentoRepository.existsById(pagamentoRequest.getCodigo())) {
            logger.error("Pagamento#{}: {}", pagamentoRequest.getCodigo(), PAGAMENTO_JA_EXISTE_MESSAGE);
            throw new BusinessException(PAGAMENTO_JA_EXISTE_MESSAGE, HttpStatus.BAD_REQUEST);
        }
        Pagamento pagamento = pagamentoMapper.mapToEntity(pagamentoRequest);
        pagamento.setStatus(StatusProcessamento.PENDENTE_PROCESSAMENTO);

        logHistoricoProcessamento(pagamento);

        return pagamentoMapper.mapToResponse(pagamentoRepository.save(pagamento));
    }

    public PagamentoResponse update(Integer codigo, PagamentoRequest pagamentoRequest) {
        Pagamento pagamento = pagamentoRepository
                .findByCodigoAndExcluido(codigo, Boolean.FALSE)
                .orElseThrow(() -> {
                    logger.warn("update - Pagamento#{} não encontrado", codigo);
                    return new RegisterNotFoundException(codigo);
                });

        pagamentoMapper.updateFromDTO(pagamento, pagamentoRequest);

        return pagamentoMapper.mapToResponse(pagamentoRepository.save(pagamento));
    }

    public void delete(Integer codigo) {
        Pagamento pagamento = pagamentoRepository
                .findByCodigoAndExcluido(codigo, Boolean.FALSE)
                .orElseThrow(() -> {
                    logger.warn("delete - Pagamento#{} não encontrado", codigo);
                    return new RegisterNotFoundException(codigo);
                });

        pagamento.setExcluido(Boolean.TRUE);

        pagamentoRepository.save(pagamento);
    }

    @Transactional
    public PagamentoResponse processarPagamento(Integer codigo, StatusProcessamento status) {
        Pagamento pagamento = pagamentoRepository
                .findByCodigoAndExcluido(codigo, Boolean.FALSE)
                .orElseThrow(() -> {
                    logger.error("processarPagamento - Pagamento#{} não encontrado", codigo);
                    return new RegisterNotFoundException(codigo);
                });


        if(pagamento.getStatus() == status) {
            String errorMessage = String.format(ALTERAR_PARA_MESMO_STATUS, status);
            logger.error("Pagamento#{}: {}", codigo, errorMessage);
            throw new BusinessException(errorMessage);
        }

        if(pagamento.getStatus() == StatusProcessamento.PROCESSADO_COM_SUCESSO) {
            logger.error("Pagamento#{}: {}", codigo, PAGAMENTO_JA_PROCESSADO);
            throw new BusinessException(PAGAMENTO_JA_PROCESSADO);
        }

        if(pagamento.getStatus() == StatusProcessamento.PROCESSADO_COM_FALHA
                && status != StatusProcessamento.PENDENTE_PROCESSAMENTO) {
            String errorMessage = String.format(ALTERAR_STATUS_INVALIDO, pagamento.getStatus(), status);
            logger.error("Pagamento#{}: {}", codigo, errorMessage);
            throw new BusinessException(errorMessage);
        }

        pagamento.setStatus(status);

        logHistoricoProcessamento(pagamento);

        return pagamentoMapper.mapToResponse(pagamentoRepository.save(pagamento));
    }

    @Transactional
    protected void logHistoricoProcessamento(Pagamento pagamento) {
        HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento(pagamento);
        historicoProcessamentoRepository.save(historicoProcessamento);

        logger.info("Historico de processamento registrado para o Pagamento#{}", pagamento.getCodigo());
    }
}
