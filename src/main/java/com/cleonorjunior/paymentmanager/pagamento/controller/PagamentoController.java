package com.cleonorjunior.paymentmanager.pagamento.controller;

import com.cleonorjunior.paymentmanager.pagamento.domain.request.FiltroPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.ProcessarPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/pagamentos")
@Tag(name = "Pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;


    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping
    @Operation(description = "Incluir novo pagamento")
    public ResponseEntity<PagamentoResponse> save(@RequestBody @Valid PagamentoRequest pagamentoRequest, UriComponentsBuilder uriBuilder) {
        PagamentoResponse persistedPagamento = pagamentoService.save(pagamentoRequest);

        URI uri = uriBuilder.path("/pagamentos").buildAndExpand(persistedPagamento.getCodigo()).toUri();

        return ResponseEntity.created(uri).body(persistedPagamento);
    }

    @PutMapping("/{codigo}")
    @Operation(description = "Atualizar um pagamento existente")
    public ResponseEntity<PagamentoResponse> update(@PathVariable Integer codigo, @RequestBody @Valid PagamentoRequest pagamentoRequest) {
        PagamentoResponse updatedPagamento = pagamentoService.update(codigo, pagamentoRequest);
        return ResponseEntity.ok(updatedPagamento);
    }

    @GetMapping
    @Operation(description = "Buscar pagamentos com filtro")
    public ResponseEntity<Page<PagamentoResponse>> findAll(FiltroPagamentoRequest filtroPagamento, Pageable paginacao) {
        Page<PagamentoResponse> pagamentos = pagamentoService.findAll(filtroPagamento, paginacao);

        return ResponseEntity.ok().body(pagamentos);
    }

    @GetMapping("/{codigo}")
    @Operation(description = "Buscar um pagamento por código")
    public ResponseEntity<PagamentoResponse> findById(@PathVariable Integer codigo) {
        return ResponseEntity.ok().body(pagamentoService.findById(codigo));
    }

    @DeleteMapping("/{codigo}")
    @Operation(description = "Excluir um pagamento por código")
    public ResponseEntity<Void> delete(@PathVariable Integer codigo) {
        pagamentoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{codigo}/status")
    @Operation(description = "Alterar status de um pagamento")
    public ResponseEntity<PagamentoResponse> atualizarStatus(@PathVariable Integer codigo, @RequestBody ProcessarPagamentoRequest pagamentoDTO) {
        return ResponseEntity.ok().body(pagamentoService.processarPagamento(codigo, StatusProcessamento.valueOf(pagamentoDTO.getStatus())));
    }
}
