package com.cleonorjunior.paymentmanager.pagamento.domain.model;

import com.cleonorjunior.paymentmanager.pagamento.domain.enums.StatusProcessamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HistoricoProcessamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="codigo")
    private Pagamento pagamento;

    @Enumerated(EnumType.STRING)
    private StatusProcessamento status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataInsercao;

    public HistoricoProcessamento() {}

    public HistoricoProcessamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        this.status = pagamento.getStatus();
    }


}
