package com.cleonorjunior.paymentmanager.pagamento.domain.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoRequestTest {

    @Test
    void validateValueForCartaoCredito() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("CARTAO_CREDITO")
                .numeroCartao("5516889900038664").build();

        assertTrue(pagamentoRequest.isNotEmptyForCardMethod());
    }

    @Test
    void validateValueForCartaoDebito() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("CARTAO_DEBITO")
                .numeroCartao("5516889900038664").build();

        assertTrue(pagamentoRequest.isNotEmptyForCardMethod());
    }

    @Test
    void validateValueForOtherMethods() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("PIX")
                .build();

        assertTrue(pagamentoRequest.isNotEmptyForCardMethod());
    }

    @Test
    void validateEmptyForCartaoCredito() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("CARTAO_CREDITO").build();

        assertTrue(pagamentoRequest.isEmptyForOtherMethods());
    }


    @Test
    void validateEmptyForCartaoDebito() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("CARTAO_DEBITO").build();

        assertTrue(pagamentoRequest.isEmptyForOtherMethods());
    }

    @Test
    void validateEmptyForPIX() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("PIX").build();

        assertTrue(pagamentoRequest.isEmptyForOtherMethods());
    }

    @Test
    void validateEmptyForBoleto() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("BOLETO").build();

        assertTrue(pagamentoRequest.isEmptyForOtherMethods());
    }

    @Test
    void validateNotEmptyForPIX() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("PIX")
                .numeroCartao("5516889900038664").build();

        assertFalse(pagamentoRequest.isEmptyForOtherMethods());
    }

    @Test
    void validateNotEmptyForBoleto() {
        PagamentoRequest pagamentoRequest = PagamentoRequest.builder()
                .metodoPagamento("BOLETO")
                .numeroCartao("5516889900038664").build();

        assertFalse(pagamentoRequest.isEmptyForOtherMethods());
    }

}