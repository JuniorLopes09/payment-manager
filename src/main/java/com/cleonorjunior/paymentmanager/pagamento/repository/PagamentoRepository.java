package com.cleonorjunior.paymentmanager.pagamento.repository;

import com.cleonorjunior.paymentmanager.pagamento.domain.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>, JpaSpecificationExecutor<Pagamento> {

    Optional<Pagamento> findByCodigoAndExcluido(Integer integer, Boolean excluido);
}


