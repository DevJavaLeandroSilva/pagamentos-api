package com.pagamentos.pagamentosAPI.service;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.exceptions.BadRequestException;
import com.pagamentos.pagamentosAPI.exceptions.NotFoundException;
import com.pagamentos.pagamentosAPI.repository.PagamentoRepository;
import com.pagamentos.pagamentosAPI.service.interfaces.IPagamentoService;
import com.pagamentos.pagamentosAPI.util.ValidadorDadosFinanceiros;
import com.pagamentos.pagamentosAPI.util.ValidadorStatusPagamento;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService implements IPagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Override
    @Transactional
    public Pagamento novoPagamento(Pagamento pagamento) {
        ValidadorDadosFinanceiros.validarCpfCnpj(pagamento.getCpfCnpj());

        if (pagamento.getMetodoPagamento() == MetodoPagamentoEnum.CARTAO_CREDITO ||
                pagamento.getMetodoPagamento() == MetodoPagamentoEnum.CARTAO_DEBITO) {

            ValidadorDadosFinanceiros.validarNumeroCartao(pagamento.getNumeroCartao());
        }

        pagamento.setStatusPagamento(StatusPagamentoEnum.PENDENTE_PROCESSAMENTO);

        return repository.save(pagamento);
    }

    @Override
    @Transactional
    public Pagamento atualizarStatusPagamento(Long id, StatusPagamentoEnum novoStatus) {
        Pagamento pagamento = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Pagamento não encontrado: " + id));

        ValidadorStatusPagamento.validarTransicao(pagamento.getStatusPagamento(), novoStatus);

        pagamento.setStatusPagamento(novoStatus);
        return repository.save(pagamento);
    }

    @Override
    @Transactional
    public Page<Pagamento> listarPagamentos(Integer codigoDebito,
                                            String cpfCnpj,
                                            StatusPagamentoEnum statusPagamento,
                                            int paginaAtual,
                                            int tamanhoPagina,
                                            String ordenacao,
                                            String direcao) {

        Sort sort = Sort.by(Sort.Direction.fromString(direcao), ordenacao);
        Pageable pageable = PageRequest.of(paginaAtual, tamanhoPagina, sort);

        return repository.findByFiltros(codigoDebito, cpfCnpj, statusPagamento, pageable);
    }

    @Override
    @Transactional
    public void deletarPagamento(Long id) {
        Pagamento pagamento = repository.findById(id).orElseThrow(() ->
                new NotFoundException("Pagamento não encontrado: " + id));

        if (pagamento.getStatusPagamento() != StatusPagamentoEnum.PENDENTE_PROCESSAMENTO) {
            throw new BadRequestException("Somente pagamentos pendentes podem ser deletados/inativados.");
        }

        pagamento.setStatusPagamento(StatusPagamentoEnum.INATIVO);
        repository.save(pagamento);
    }

}