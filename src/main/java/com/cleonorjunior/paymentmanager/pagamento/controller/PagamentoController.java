package com.cleonorjunior.paymentmanager.pagamento.controller;

import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.FiltroPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.ProcessarPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(PagamentoController.BASE_PATH)
@Tag(name = "Pagamento")
public class PagamentoController {

    public static final String BASE_PATH = "/pagamentos";

    Logger logger = LoggerFactory.getLogger(PagamentoController.class);

    private final PagamentoService pagamentoService;


    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(description = "Incluir novo pagamento")
    public ResponseEntity<PagamentoResponse> save(@RequestBody @Valid PagamentoRequest pagamentoRequest, UriComponentsBuilder uriBuilder) {

        logger.info("Incluindo novo pagamento com os parametros: {}", pagamentoRequest);

        PagamentoResponse persistedPagamento = pagamentoService.save(pagamentoRequest);

        logger.info("Pagamento#{} incluido com sucesso", persistedPagamento.getCodigo());

        URI uri = uriBuilder.path(BASE_PATH).buildAndExpand(persistedPagamento.getCodigo()).toUri();

        return ResponseEntity.created(uri).body(persistedPagamento);
    }

    @PutMapping("/{codigo}")
    @Operation(description = "Atualizar um pagamento existente")
    public ResponseEntity<PagamentoResponse> update(@PathVariable Integer codigo, @RequestBody @Valid PagamentoRequest pagamentoRequest) {

        logger.info("Alterando Pagamento#{} com os parametros: {}", codigo, pagamentoRequest);

        PagamentoResponse updatedPagamento = pagamentoService.update(codigo, pagamentoRequest);

        logger.info("Pagamento#{} alterado com sucesso", updatedPagamento.getCodigo());

        return ResponseEntity.ok(updatedPagamento);
    }

    @GetMapping
    @Operation(description = "Buscar pagamentos com filtro")
    public ResponseEntity<Page<PagamentoResponse>> findAll(@Valid FiltroPagamentoRequest filtroPagamento, Pageable paginacao) {

        logger.info("Buscando Pagamento com os parametros: {} {}", filtroPagamento, paginacao);

        Page<PagamentoResponse> pagamentos = pagamentoService.findAll(filtroPagamento, paginacao);

        return ResponseEntity.ok().body(pagamentos);
    }

    @GetMapping("/{codigo}")
    @Operation(description = "Buscar um pagamento por código")
    public ResponseEntity<PagamentoResponse> findById(@PathVariable Integer codigo) {

        logger.info("Buscando Pagamento#{}", codigo);

        PagamentoResponse pagamento = pagamentoService.findById(codigo);

        logger.info("Pagamento#{} encontrado com sucesso", codigo);

        return ResponseEntity.ok().body(pagamento);
    }

    @DeleteMapping("/{codigo}")
    @Operation(description = "Excluir um pagamento por código")
    public ResponseEntity<Void> delete(@PathVariable Integer codigo) {

        logger.info("Deletando Pagamento#{}", codigo);

        pagamentoService.delete(codigo);

        logger.info("Pagamento#{} deletado com sucesso", codigo);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{codigo}/status")
    @Operation(description = "Alterar status de um pagamento")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable Integer codigo, @RequestBody @Valid ProcessarPagamentoRequest request) {

        logger.info("Alterando status do Pagamento#{} com os parametros: {}", codigo, request);

        PagamentoResponse pagamento = pagamentoService.processarPagamento(
                codigo,
                StatusProcessamento.valueOf(request.getStatus())
        );

        logger.info("Status do Pagamento#{} alterado com sucesso", codigo);

        return ResponseEntity.ok().body(pagamento);
    }
}
