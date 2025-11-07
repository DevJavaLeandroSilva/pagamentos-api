package com.pagamentos.pagamentosAPI.entity;

import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_debito", nullable = false)
    private Integer codigoDebito;

    @Column(name = "cpf_cnpj", nullable = false, length = 14)
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false, length = 15)
    private MetodoPagamentoEnum metodoPagamento;

    @Column(name = "numero_cartao", length = 19)
    private String numeroCartao;

    @Column(name = "valor_pagamento", nullable = false, scale = 2, precision = 19)
    private BigDecimal valorPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false)
    private StatusPagamentoEnum statusPagamento;

}
