package com.pagamentos.pagamentosAPI.service.interfaces;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import org.springframework.data.domain.Page;

public interface IPagamentoService {
    Pagamento novoPagamento(Pagamento pagamento);

    Pagamento atualizarStatusPagamento(Long id, StatusPagamentoEnum novoStatus);

    Page<Pagamento> listarPagamentos(Integer codigoDebito,
                                     String cpfCnpj,
                                     StatusPagamentoEnum statusPagamento,
                                     int paginaAtual,
                                     int tamanhoPagina,
                                     String ordenacao,
                                     String direcao);

    void deletarPagamento(Long id);
}
