package com.cleonorjunior.paymentmanager.pagamento.mapper;

import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring")
public interface PagamentoMapper {

    PagamentoResponse mapToResponse(Pagamento pagamento);

    Pagamento mapToEntity(PagamentoRequest dto);

    void updateFromDTO(@MappingTarget Pagamento pagamento, PagamentoRequest dto);
}
