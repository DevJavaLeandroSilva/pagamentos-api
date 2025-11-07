package com.pagamentos.pagamentosAPI.util;

import com.pagamentos.pagamentosAPI.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ValidadorDadosFinanceirosTest {
    @Test
    void validarCpf() {
        assertThatCode(() -> ValidadorDadosFinanceiros.validarCpfCnpj("12345678901"))
                .doesNotThrowAnyException();
    }

    @Test
    void validarCnpj() {
        assertThatCode(() -> ValidadorDadosFinanceiros.validarCpfCnpj("12345678000199"))
                .doesNotThrowAnyException();
    }

    @Test
    void lancaExcecaoCpfCnpjVazio() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarCpfCnpj(" "))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("não pode ser vazio");
    }

    @Test
    void lancaExcecaoCpfCnpjComCaracteresInvalidos() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarCpfCnpj("123.456.789-00"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("apenas números");
    }

    @Test
    void lancaExcecaoCpfTamanhoInvalido() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarCpfCnpj("12345"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("CPF deve conter 11 dígitos");
    }

    @Test
    void lancaExcecaoCnpjTamanhoInvalido() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarCpfCnpj("123456789012345"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("CNPJ deve conter 14 dígitos");
    }

    @Test
    void validarNumeroCartao() {
        assertThatCode(() -> ValidadorDadosFinanceiros.validarNumeroCartao("1234567890123456"))
                .doesNotThrowAnyException();
    }

    @Test
    void lancarExcecaoCartaoVazio() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarNumeroCartao(" "))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("não pode ser vazio");
    }

    @Test
    void lancaExcecaoCartaoComEspaco() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarNumeroCartao("1234 5678 9012 3456"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("apenas dígitos");
    }

    @Test
    void lancaExcecaoCartaoComCaracteresEspeciais() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarNumeroCartao("1234-5678-9012-3456"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("apenas dígitos");
    }

    @Test
    void lancaExcecaoCartaoCurto() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarNumeroCartao("123456789012"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("entre 13 e 19 dígitos");
    }
    @Test
    void lancaExcecaoCartaoLongoDemais() {
        assertThatThrownBy(() -> ValidadorDadosFinanceiros.validarNumeroCartao("123456789012345678901"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("entre 13 e 19 dígitos");
    }

}
