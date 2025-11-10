package com.pagamentos.pagamentosAPI.util;

import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.exceptions.InvalidStatusChangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidadorStatusPagamentoTest {
    @Test
    void permitePendenteParaProcessadoSucesso() {
        assertDoesNotThrow(() ->
                ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PENDENTE_PROCESSAMENTO,
                        StatusPagamentoEnum.PROCESSADO_SUCESSO));
    }

    @Test
    void permitePendenteParaProcessadoFalha() {
        assertDoesNotThrow(() ->
                ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PENDENTE_PROCESSAMENTO,
                        StatusPagamentoEnum.PROCESSADO_FALHA));
    }

    @Test
    void lancaErroParaPendenteParaInativo() {
        InvalidStatusChangeException exception = assertThrows(
                InvalidStatusChangeException.class,
                () -> ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PENDENTE_PROCESSAMENTO,
                        StatusPagamentoEnum.INATIVO));

        assertEquals("Status inválido para pagamento pendente.", exception.getMessage());
    }

    @Test
    void lancarErroParaProcessadoSucesso() {
        InvalidStatusChangeException exception = assertThrows(
                InvalidStatusChangeException.class,
                () -> ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PROCESSADO_SUCESSO,
                        StatusPagamentoEnum.PROCESSADO_FALHA));

        assertEquals("Pagamento processado com sucesso não pode ser alterado.", exception.getMessage());
    }

    @Test
    void permiteFalhaParaPendente() {
        assertDoesNotThrow(() ->
                ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PROCESSADO_FALHA,
                        StatusPagamentoEnum.PENDENTE_PROCESSAMENTO));
    }

    @Test
    void lancaErroFalhaParaSucesso() {
        InvalidStatusChangeException exception = assertThrows(
                InvalidStatusChangeException.class,
                () -> ValidadorStatusPagamento.validarTransicao(
                        StatusPagamentoEnum.PROCESSADO_FALHA,
                        StatusPagamentoEnum.PROCESSADO_SUCESSO));

        assertEquals("Pagamento com falha só pode retornar para pendente.", exception.getMessage());
    }

    @Test
    void lancaErroParaStatusDesconhecido() {
        InvalidStatusChangeException exception = assertThrows(
                InvalidStatusChangeException.class,
                () -> ValidadorStatusPagamento.validarTransicao(null, StatusPagamentoEnum.PENDENTE_PROCESSAMENTO));

        assertEquals("Transição de status não permitida.", exception.getMessage());
    }
}
