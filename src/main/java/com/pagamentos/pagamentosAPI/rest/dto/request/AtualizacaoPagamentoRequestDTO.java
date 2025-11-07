package com.pagamentos.pagamentosAPI.rest.dto.request;

import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizacaoPagamentoRequestDTO {
    @NotNull
    private StatusPagamentoEnum novoStatus;
}
