package com.pagamentos.pagamentosAPI.rest.factory;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.rest.dto.request.PagamentoRequestDTO;
import com.pagamentos.pagamentosAPI.rest.dto.response.PagamentoResponseDTO;
import org.springframework.data.domain.Page;

public class PagamentoRestFactory {
    public static Pagamento toEntity(PagamentoRequestDTO pagamentoRequest) {
        if (pagamentoRequest == null) return null;

        Pagamento entity = new Pagamento();
        entity.setCodigoDebito(pagamentoRequest.getCodigoDebito());
        entity.setCpfCnpj(pagamentoRequest.getCpfCnpj());
        entity.setMetodoPagamento(pagamentoRequest.getMetodoPagamento());
        entity.setNumeroCartao(pagamentoRequest.getNumeroCartao());
        entity.setValorPagamento(pagamentoRequest.getValorPagamento());
        return entity;
    }

    public static PagamentoResponseDTO toResponse(Pagamento pagamento) {
        if (pagamento == null) return null;

        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(pagamento.getId());
        dto.setCodigoDebito(pagamento.getCodigoDebito());
        dto.setCpfCnpj(pagamento.getCpfCnpj());
        dto.setMetodoPagamento(pagamento.getMetodoPagamento());
        dto.setNumeroCartao(pagamento.getNumeroCartao());
        dto.setValorPagamento(pagamento.getValorPagamento());
        dto.setStatusPagamento(pagamento.getStatusPagamento());
        return dto;
    }

    public static Page<PagamentoResponseDTO> toResponsePage(Page<Pagamento> pagamentos) {
        return pagamentos.map(PagamentoRestFactory::toResponse);
    }
}
