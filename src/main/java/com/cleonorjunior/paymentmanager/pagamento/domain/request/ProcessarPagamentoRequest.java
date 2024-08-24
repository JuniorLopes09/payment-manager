package com.cleonorjunior.paymentmanager.pagamento.domain.request;

import com.cleonorjunior.paymentmanager.configuration.annotation.ValidEnum;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProcessarPagamentoRequest {

    @NotBlank
    @ValidEnum(enumClass = StatusProcessamento.class )
    private String status;

    public static ProcessarPagamentoRequest instance() {
        return ProcessarPagamentoRequest.builder().status("PENDENTE_PROCESSAMENTO").build();
    }

}
