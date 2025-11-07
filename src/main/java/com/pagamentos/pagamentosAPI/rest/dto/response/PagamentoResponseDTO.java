package com.pagamentos.pagamentosAPI.rest.dto.response;

import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagamentoResponseDTO {
    private Long id;
    private Integer codigoDebito;
    private String cpfCnpj;
    private MetodoPagamentoEnum metodoPagamento;
    private String numeroCartao;
    private BigDecimal valorPagamento;
    private StatusPagamentoEnum statusPagamento;
}
