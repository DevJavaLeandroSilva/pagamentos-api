package com.pagamentos.pagamentosAPI.util;

import com.pagamentos.pagamentosAPI.exceptions.BadRequestException;

import java.util.regex.Pattern;

public class ValidadorDadosFinanceiros {
    private static final Pattern SOMENTE_NUMEROS = Pattern.compile("^[0-9]+$");

    public static void validarNumeroCartao(String numeroCartao) {
        if (numeroCartao == null || numeroCartao.trim().isEmpty()) {
            throw new BadRequestException("O número do cartão não pode ser vazio.");
        }

        if (!SOMENTE_NUMEROS.matcher(numeroCartao).matches()) {
            throw new BadRequestException("O número do cartão deve conter apenas dígitos, sem espaços ou caracteres especiais.");
        }

        if (numeroCartao.length() < 13 || numeroCartao.length() > 19) {
            throw new BadRequestException("O número do cartão deve conter entre 13 e 19 dígitos.");
        }
    }

    public static void validarCpfCnpj(String cpfCnpj) {
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
            throw new BadRequestException("CPF ou CNPJ não pode ser vazio.");
        }

        if (!SOMENTE_NUMEROS.matcher(cpfCnpj).matches()) {
            throw new BadRequestException("CPF ou CNPJ deve conter apenas números, sem pontos, traços ou espaços.");
        }

        int length = cpfCnpj.length();
        if (length != 11 && length != 14) {
            throw new BadRequestException("CPF deve conter 11 dígitos e CNPJ deve conter 14 dígitos.");
        }
    }
}
