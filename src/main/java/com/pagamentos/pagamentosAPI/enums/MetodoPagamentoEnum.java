package com.pagamentos.pagamentosAPI.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MetodoPagamentoEnum {
    BOLETO("Boleto"),
    PIX("Pix"),
    CARTAO_CREDITO("Cartão de crédido"),
    CARTAO_DEBITO("Cartão de débito");

    private String descricao;
}
