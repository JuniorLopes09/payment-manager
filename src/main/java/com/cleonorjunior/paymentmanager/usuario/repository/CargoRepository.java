package com.cleonorjunior.paymentmanager.usuario.repository;

import com.cleonorjunior.paymentmanager.usuario.domain.enums.TipoCargo;
import com.cleonorjunior.paymentmanager.usuario.domain.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findByCargo(TipoCargo cargo);

}
