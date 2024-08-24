package com.cleonorjunior.paymentmanager.pagamento.domain.request;

import com.cleonorjunior.paymentmanager.configuration.annotation.CpfCnpj;
import com.cleonorjunior.paymentmanager.configuration.annotation.ValidEnum;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FiltroPagamentoRequest {

    @Positive
    private Integer codigo;

    @CpfCnpj
    private String cpfCnpjPagador;

    @ValidEnum(enumClass = StatusProcessamento.class)
    private String status;

    public static FiltroPagamentoRequest instance() {
        return FiltroPagamentoRequest.builder()
                .codigo(1)
                .cpfCnpjPagador("12345678909")
                .status("PENDENTE_PROCESSAMENTO")
                .build();
    }
}
