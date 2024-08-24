package com.cleonorjunior.paymentmanager.pagamento.controller;

import com.cleonorjunior.paymentmanager.pagamento.domain.request.FiltroPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.ProcessarPagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PagamentoControllerTest {

    @Mock
    private PagamentoService service;

    @InjectMocks
    private PagamentoController controller;

    private final PagamentoRequest pagamentoRequest = PagamentoRequest.instance();

    private final PagamentoResponse pagamentoResponse = PagamentoResponse.instance();

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        when(service.save(pagamentoRequest)).thenReturn(pagamentoResponse);

        ResponseEntity<PagamentoResponse> response = controller.save(pagamentoRequest, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void update() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.instance();

        when(service.update(pagamentoRequest.getCodigo(), pagamentoRequest)).thenReturn(pagamentoResponse);

        ResponseEntity<PagamentoResponse> response = controller.update(pagamentoRequest.getCodigo(), pagamentoRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findAll() {
        FiltroPagamentoRequest filtroPagamentoRequest = FiltroPagamentoRequest.instance();

        Pageable paginacao = Pageable.ofSize(10);
        Page<PagamentoResponse> page = new PageImpl<>(Collections.singletonList(pagamentoResponse));

        when(service.findAll(filtroPagamentoRequest, paginacao)).thenReturn(page);

        ResponseEntity<Page<PagamentoResponse>> response = controller.findAll(filtroPagamentoRequest, paginacao);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findById() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.instance();

        when(service.findById(pagamentoRequest.getCodigo())).thenReturn(pagamentoResponse);

        ResponseEntity<PagamentoResponse> response = controller.findById(pagamentoRequest.getCodigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void delete() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.instance();

        doNothing().when(service).delete(pagamentoRequest.getCodigo());

        ResponseEntity<Void> response = controller.delete(pagamentoRequest.getCodigo());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void atualizarStatus() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.instance();
        ProcessarPagamentoRequest processarPagamentoRequest = ProcessarPagamentoRequest.instance();
        StatusProcessamento status = StatusProcessamento.valueOf(processarPagamentoRequest.getStatus());

        when(service.processarPagamento(pagamentoRequest.getCodigo(), status)).thenReturn(pagamentoResponse);

        ResponseEntity<PagamentoResponse> response = controller.atualizarStatus(pagamentoRequest.getCodigo(), processarPagamentoRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}