package com.pagamentos.pagamentosAPI.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusPagamentoEnum {
    PENDENTE_PROCESSAMENTO("Pendente de processamento"),
    PROCESSADO_SUCESSO("Processado com sucesso"),
    PROCESSADO_FALHA("Processado com falha"),
    INATIVO("Inativo");

    private String descricao;
}
