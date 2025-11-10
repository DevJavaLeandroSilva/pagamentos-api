package com.pagamentos.pagamentosAPI.util;

import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.exceptions.InvalidStatusChangeException;

public class ValidadorStatusPagamento {
    public static void validarTransicao(StatusPagamentoEnum atual, StatusPagamentoEnum novo) {

        if (atual == null || novo == null) {
            throw new InvalidStatusChangeException("Transição de status não permitida.");
        }

        switch (atual) {
            case PENDENTE_PROCESSAMENTO:
                if (novo != StatusPagamentoEnum.PROCESSADO_SUCESSO &&
                        novo != StatusPagamentoEnum.PROCESSADO_FALHA) {
                    throw new InvalidStatusChangeException("Status inválido para pagamento pendente.");
                }
                break;

            case PROCESSADO_SUCESSO:
                throw new InvalidStatusChangeException("Pagamento processado com sucesso não pode ser alterado.");

            case PROCESSADO_FALHA:
                if (novo != StatusPagamentoEnum.PENDENTE_PROCESSAMENTO) {
                    throw new InvalidStatusChangeException("Pagamento com falha só pode retornar para pendente.");
                }
                break;

            default:
                throw new InvalidStatusChangeException("Transição de status não permitida.");
        }
    }
}
