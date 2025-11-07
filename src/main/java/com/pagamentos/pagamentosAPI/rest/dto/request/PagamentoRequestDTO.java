package com.pagamentos.pagamentosAPI.rest.dto.request;

import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagamentoRequestDTO {
    @NotNull
    private Integer codigoDebito;

    @NotBlank
    private String cpfCnpj;

    @NotNull
    private MetodoPagamentoEnum metodoPagamento;

    private String numeroCartao;

    @NotNull
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que 0.01", inclusive = true)
    private BigDecimal valorPagamento;
}
