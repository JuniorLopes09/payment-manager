package com.cleonorjunior.paymentmanager.pagamento.repository;

import com.cleonorjunior.paymentmanager.pagamento.domain.model.HistoricoProcessamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoProcessamentoRepository extends JpaRepository<HistoricoProcessamento, Integer> {

}
