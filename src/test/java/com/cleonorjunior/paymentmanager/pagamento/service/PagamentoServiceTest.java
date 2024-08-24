package com.cleonorjunior.paymentmanager.pagamento.service;

import com.cleonorjunior.paymentmanager.configuration.exception.BusinessException;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.FiltroPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.MetodoPagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.HistoricoProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import com.cleonorjunior.paymentmanager.pagamento.mapper.PagamentoMapper;
import com.cleonorjunior.paymentmanager.pagamento.repository.HistoricoProcessamentoRepository;
import com.cleonorjunior.paymentmanager.pagamento.repository.PagamentoRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoMapper mapper;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private HistoricoProcessamentoRepository historicoProcessamentoRepository;

    @Mock
    Root<Pagamento> root;

    @Mock
    CriteriaQuery<?> query;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Captor
    private ArgumentCaptor<Specification<Pagamento>> specificationCaptor;

    private final PagamentoRequest pagamentoRequest = PagamentoRequest.instance();

    private final PagamentoResponse pagamentoResponse = PagamentoResponse.instance();

    private final Pagamento pagamento = new Pagamento();

    private final FiltroPagamentoRequest filtroPagamentoRequest = FiltroPagamentoRequest.instance();
    
    private final HistoricoProcessamento historicoProcessamento = new HistoricoProcessamento();


    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        pagamento.setMetodoPagamento(MetodoPagamento.BOLETO);

        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamento));

        PagamentoResponse response = service.findById(pagamentoRequest.getCodigo());

        assertEquals(pagamentoRequest.getCodigo(), response.getCodigo());
    }

    @Test
    void findAll() {
        Page<Pagamento> pagamentos = new PageImpl<>(Collections.singletonList(pagamento));
        Pageable paginacao = Pageable.ofSize(10);

        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);


        when(pagamentoRepository.findAll(any(Specification.class), eq(paginacao))).thenReturn(pagamentos);

        Page<PagamentoResponse> response = service.findAll(FiltroPagamentoRequest.builder().build(), paginacao);

        verify(pagamentoRepository).findAll(specificationCaptor.capture(), eq(paginacao));
        Specification<Pagamento> capturedSpecification = specificationCaptor.getValue();

        Predicate resultPredicate = capturedSpecification.toPredicate(root, query, criteriaBuilder);

        assertEquals(pagamentoRequest.getCodigo(), response.getContent().get(0).getCodigo());
    }

    @Test
    void findAllWithFilter() {
        Page<Pagamento> pagamentos = new PageImpl<>(Collections.singletonList(pagamento));
        Pageable paginacao = Pageable.ofSize(10);

        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);


        when(pagamentoRepository.findAll(any(Specification.class), eq(paginacao))).thenReturn(pagamentos);

        Page<PagamentoResponse> response = service.findAll(filtroPagamentoRequest, paginacao);

        verify(pagamentoRepository).findAll(specificationCaptor.capture(), eq(paginacao));
        Specification<Pagamento> capturedSpecification = specificationCaptor.getValue();

        Predicate resultPredicate = capturedSpecification.toPredicate(root, query, criteriaBuilder);

        assertEquals(pagamentoRequest.getCodigo(), response.getContent().get(0).getCodigo());
    }

    @Test
    void save() {
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(mapper.mapToEntity(any())).thenReturn(pagamento);

        when(pagamentoRepository.existsById(pagamentoRequest.getCodigo())).thenReturn(Boolean.FALSE);

        when(pagamentoRepository.save(any())).thenReturn(pagamento);

        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);

        PagamentoResponse response = service.save(pagamentoRequest);

        assertEquals(pagamentoRequest.getCodigo(), response.getCodigo());
    }

    @Test()
    void saveWithException() {
        when(pagamentoRepository.existsById(pagamentoRequest.getCodigo())).thenReturn(Boolean.TRUE);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.save(pagamentoRequest));
        assertEquals(PagamentoService.PAGAMENTO_JA_EXISTE_MESSAGE, exception.getMessage());

    }

    @Test
    void update() {

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamento));

        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(mapper.mapToEntity(any())).thenReturn(pagamento);

        doNothing().when(mapper).updateFromDTO(any(), any());

        PagamentoResponse response = service.update(pagamentoRequest.getCodigo(), pagamentoRequest);

        assertEquals(pagamentoRequest.getCodigo(), response.getCodigo());
    }

    @Test
    void delete() {
        when(pagamentoRepository.save(any())).thenReturn(pagamento);

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamento));

        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);


        service.delete(pagamentoRequest.getCodigo());

        verify(pagamentoRepository, times(1)).save(any());
    }

    @Test
    void processarPagamento() {

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.save(any())).thenReturn(pagamento);
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);

        PagamentoResponse response = service.processarPagamento(pagamentoRequest.getCodigo(), StatusProcessamento.PROCESSADO_COM_SUCESSO);

        assertEquals(pagamentoRequest.getCodigo(), response.getCodigo());
    }

    @Test
    void processarPagamentoProcessadoComFalha() {
        StatusProcessamento status = StatusProcessamento.PROCESSADO_COM_FALHA;
        Pagamento pagamentoEntity = new Pagamento();

        pagamentoEntity.setStatus(status);


        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamentoEntity));
        when(pagamentoRepository.save(any())).thenReturn(pagamento);
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);

        PagamentoResponse response = service.processarPagamento(pagamentoRequest.getCodigo(), StatusProcessamento.PENDENTE_PROCESSAMENTO);

        assertEquals(pagamentoRequest.getCodigo(), response.getCodigo());
    }

    @Test
    void processarPagamentoMesmoStatusException() {

        StatusProcessamento status = StatusProcessamento.PROCESSADO_COM_FALHA;
        Pagamento pagamentoEntity = new Pagamento();

        pagamentoEntity.setStatus(status);

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamentoEntity));
        when(pagamentoRepository.save(any())).thenReturn(pagamento);
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.processarPagamento(pagamentoRequest.getCodigo(), status)
        );

        String errorMessage = String.format(PagamentoService.ALTERAR_PARA_MESMO_STATUS, status);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void processarPagamentoJaProcessadoException() {

        StatusProcessamento novoStatus = StatusProcessamento.PENDENTE_PROCESSAMENTO;
        StatusProcessamento status = StatusProcessamento.PROCESSADO_COM_SUCESSO;
        Pagamento pagamentoEntity = new Pagamento();

        pagamentoEntity.setStatus(status);

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamentoEntity));
        when(pagamentoRepository.save(any())).thenReturn(pagamento);
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.processarPagamento(pagamentoRequest.getCodigo(), novoStatus)
        );

        assertEquals(PagamentoService.PAGAMENTO_JA_PROCESSADO, exception.getMessage());
    }

    @Test
    void processarPagamentoStatusInvalidoException() {

        StatusProcessamento novoStatus = StatusProcessamento.PROCESSADO_COM_SUCESSO;
        StatusProcessamento status = StatusProcessamento.PROCESSADO_COM_FALHA;
        Pagamento pagamentoEntity = new Pagamento();

        pagamentoEntity.setStatus(status);

        when(pagamentoRepository.findById(pagamentoRequest.getCodigo())).thenReturn(Optional.of(pagamentoEntity));
        when(pagamentoRepository.save(any())).thenReturn(pagamento);
        when(mapper.mapToResponse(any())).thenReturn(pagamentoResponse);
        when(historicoProcessamentoRepository.save(any())).thenReturn(historicoProcessamento);


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> service.processarPagamento(pagamentoRequest.getCodigo(), novoStatus)
        );

        String errorMessage = String.format(PagamentoService.ALTERAR_STATUS_INVALIDO, status, novoStatus);
        assertEquals(errorMessage, exception.getMessage());
    }


}