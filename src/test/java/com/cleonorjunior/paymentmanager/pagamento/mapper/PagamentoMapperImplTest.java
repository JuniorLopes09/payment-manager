package com.cleonorjunior.paymentmanager.pagamento.mapper;

import com.cleonorjunior.paymentmanager.pagamento.domain.enums.MetodoPagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoMapperImplTest {

    PagamentoMapperImpl pagamentoMapper = new PagamentoMapperImpl();


    @Test
    void testMapToResponseWithValidPagamento() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setCodigo(1);
        pagamento.setCpfCnpjPagador("11122233344");
        pagamento.setMetodoPagamento(MetodoPagamento.CARTAO_CREDITO);
        pagamento.setNumeroCartao("4111111111111111");
        pagamento.setValor(BigDecimal.TEN);
        pagamento.setStatus(StatusProcessamento.PENDENTE_PROCESSAMENTO);

        // Act
        PagamentoResponse response = pagamentoMapper.mapToResponse(pagamento);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getCodigo());
        assertEquals("11122233344", response.getCpfCnpjPagador());
        assertEquals("CARTAO_CREDITO", response.getMetodoPagamento());
        assertEquals("4111111111111111", response.getNumeroCartao());
        assertEquals(BigDecimal.TEN, response.getValor());
        assertEquals(StatusProcessamento.PENDENTE_PROCESSAMENTO, response.getStatus());
    }

    @Test
    void testMapToResponseWithNullPagamento() {
        PagamentoResponse response = pagamentoMapper.mapToResponse(null);

        // Assert
        assertNull(response);
    }
    @Test
    void testMapToResponseWithNullMetodo() {
        Pagamento pagamento = new Pagamento();

        // Act
        PagamentoResponse response = pagamentoMapper.mapToResponse(pagamento);

        // Assert
        assertNull(response.getMetodoPagamento());
    }

    @Test
    void testMapToEntityWithValidDto() {
        // Arrange
        PagamentoRequest dto = PagamentoRequest.instance();

        // Act
        Pagamento pagamento = pagamentoMapper.mapToEntity(dto);

        // Assert
        assertNotNull(pagamento);
        assertEquals(dto.getCodigo(), pagamento.getCodigo());
        assertEquals(dto.getCpfCnpjPagador(), pagamento.getCpfCnpjPagador());
        assertEquals(MetodoPagamento.valueOf(dto.getMetodoPagamento()), pagamento.getMetodoPagamento());
        assertEquals(dto.getNumeroCartao(), pagamento.getNumeroCartao());
        assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    void testMapToEntityWithoutMethodDto() {
        // Arrange
        PagamentoRequest dto = PagamentoRequest.instance();
        dto.setMetodoPagamento(null);

        // Act
        Pagamento pagamento = pagamentoMapper.mapToEntity(dto);

        // Assert
        assertNotNull(pagamento);
        assertEquals(dto.getCodigo(), pagamento.getCodigo());
        assertEquals(dto.getCpfCnpjPagador(), pagamento.getCpfCnpjPagador());
        assertNull(pagamento.getMetodoPagamento());
        assertEquals(dto.getNumeroCartao(), pagamento.getNumeroCartao());
        assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    void testMapToEntityWithNullDto() {
        // Act
        Pagamento pagamento = pagamentoMapper.mapToEntity(null);

        // Assert
        assertNull(pagamento);
    }

    @Test
    void testUpdateFromDTOWithValidDto() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        PagamentoRequest dto = PagamentoRequest.instance();

        // Act
        pagamentoMapper.updateFromDTO(pagamento, dto);

        // Assert
        assertNotNull(pagamento);
        assertEquals(dto.getCodigo(), pagamento.getCodigo());
        assertEquals(dto.getCpfCnpjPagador(), pagamento.getCpfCnpjPagador());
        assertEquals(MetodoPagamento.valueOf(dto.getMetodoPagamento()), pagamento.getMetodoPagamento());
        assertEquals(dto.getNumeroCartao(), pagamento.getNumeroCartao());
        assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    void testUpdateFromDTOWithoutMetodoDto() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        PagamentoRequest dto = PagamentoRequest.instance();
        dto.setMetodoPagamento(null);

        // Act
        pagamentoMapper.updateFromDTO(pagamento, dto);

        // Assert
        assertNotNull(pagamento);
        assertEquals(dto.getCodigo(), pagamento.getCodigo());
        assertEquals(dto.getCpfCnpjPagador(), pagamento.getCpfCnpjPagador());
        assertNull(pagamento.getMetodoPagamento());
        assertEquals(dto.getNumeroCartao(), pagamento.getNumeroCartao());
        assertEquals(dto.getValor(), pagamento.getValor());
    }

    @Test
    void testUpdateFromDTOWithNullDto() {
        // Arrange
        Pagamento pagamento = new Pagamento();

        // Act
        pagamentoMapper.updateFromDTO(pagamento, null);

        // Assert
        assertNull(pagamento.getCodigo());
        assertNull(pagamento.getCpfCnpjPagador());
        assertNull(pagamento.getMetodoPagamento());
        assertNull(pagamento.getNumeroCartao());
        assertNull(pagamento.getValor());
    }
}