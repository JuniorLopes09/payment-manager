package com.cleonorjunior.paymentmanager.pagamento.mapper;

import com.cleonorjunior.paymentmanager.pagamento.domain.request.PagamentoRequest;
import com.cleonorjunior.paymentmanager.pagamento.domain.response.PagamentoResponse;
import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper( componentModel = "spring")
public interface PagamentoMapper {

    PagamentoResponse mapToResponse(Pagamento pagamento);

    Pagamento mapToEntity(PagamentoRequest dto);

    @Mapping(target = "status", ignore = true)
    void updateFromDTO(@MappingTarget Pagamento pagamento, PagamentoRequest dto);
}
