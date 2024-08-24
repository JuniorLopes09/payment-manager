package com.cleonorjunior.paymentmanager.pagamento.domain.model;

import com.cleonorjunior.paymentmanager.pagamento.domain.enums.MetodoPagamento;
import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Pagamento {

    @Id
    private Integer codigo;

    private String cpfCnpjPagador;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    private String numeroCartao;

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private StatusProcessamento status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataInsercao;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    private Boolean excluido = Boolean.FALSE;

    public Pagamento() {}
}
