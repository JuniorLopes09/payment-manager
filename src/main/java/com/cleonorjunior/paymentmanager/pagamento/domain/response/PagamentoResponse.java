package com.cleonorjunior.paymentmanager.pagamento.domain.response;

import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PagamentoResponse {

    private Integer codigo;

    private String cpfCnpjPagador;

    private String metodoPagamento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String numeroCartao;

    private BigDecimal valor;

    private StatusProcessamento status;

    public static PagamentoResponse instance() {
        return PagamentoResponse.builder()
                .codigo(1)
                .metodoPagamento("PIX")
                .cpfCnpjPagador("12345678909")
                .valor(BigDecimal.TEN)
                .status(StatusProcessamento.PENDENTE_PROCESSAMENTO)
                .build();
    }

}
