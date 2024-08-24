package com.cleonorjunior.paymentmanager.pagamento.domain.request;

import com.cleonorjunior.paymentmanager.configuration.annotation.CpfCnpj;
import com.cleonorjunior.paymentmanager.configuration.annotation.ValidEnum;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.MetodoPagamento;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.CreditCardNumber;


import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PagamentoRequest {

    @NotNull
    @Positive
    private Integer codigo;

    @NotBlank
    @CpfCnpj
    private String cpfCnpjPagador;

    @NotBlank
    @ValidEnum(enumClass = MetodoPagamento.class)
    private String metodoPagamento;

    @CreditCardNumber
    private String numeroCartao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=14, fraction=2)
    private BigDecimal valor;


    public static PagamentoRequest instance() {
        return PagamentoRequest.builder()
                .codigo(1)
                .metodoPagamento("PIX")
                .cpfCnpjPagador("12345678909")
                .valor(BigDecimal.TEN)
                .build();
    }
    @Hidden
    @AssertTrue(message = "{default.numeroCartao.required.message}")
    public boolean isNotEmptyForCardMethod() {
        if ("CARTAO_CREDITO".equals(metodoPagamento) || "CARTAO_DEBITO".equals(metodoPagamento)) {
            return !StringUtils.isEmpty(numeroCartao);
        }

        return true;
    }

    @Hidden
    @AssertTrue(message = "{default.numeroCartao.notRequired.message}")
    public boolean isEmptyForOtherMethods() {
        if (!"CARTAO_CREDITO".equals(metodoPagamento) && !"CARTAO_DEBITO".equals(metodoPagamento)) {
            return StringUtils.isEmpty(numeroCartao);
        }

        return true;
    }

}
