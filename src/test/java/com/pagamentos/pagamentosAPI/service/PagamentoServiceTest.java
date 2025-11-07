package com.pagamentos.pagamentosAPI.service;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.exceptions.BadRequestException;
import com.pagamentos.pagamentosAPI.exceptions.InvalidStatusChangeException;
import com.pagamentos.pagamentosAPI.exceptions.NotFoundException;
import com.pagamentos.pagamentosAPI.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repository;

    @InjectMocks
    private PagamentoService service;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setCodigoDebito(123);
        pagamento.setCpfCnpj("12345678901");
        pagamento.setMetodoPagamento(MetodoPagamentoEnum.PIX);
        pagamento.setValorPagamento(BigDecimal.valueOf(100.00));
        pagamento.setStatusPagamento(StatusPagamentoEnum.PENDENTE_PROCESSAMENTO);
    }

    @Test
    void criaNovoPagamentoComStatusPendente() {
        when(repository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento salvo = service.novoPagamento(pagamento);

        assertThat(salvo.getStatusPagamento()).isEqualTo(StatusPagamentoEnum.PENDENTE_PROCESSAMENTO);
        verify(repository).save(any(Pagamento.class));
    }

    @Test
    void atualizaStatusDePendenteParaProcessadoSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(repository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        Pagamento atualizado = service.atualizarStatusPagamento(1L, StatusPagamentoEnum.PROCESSADO_SUCESSO);

        assertThat(atualizado.getStatusPagamento()).isEqualTo(StatusPagamentoEnum.PROCESSADO_SUCESSO);
        verify(repository).save(pagamento);
    }

    @Test
    void lancaExcecaoQuandoStatusInvalido() {
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));

        assertThatThrownBy(() ->
                service.atualizarStatusPagamento(1L, StatusPagamentoEnum.INATIVO))
                .isInstanceOf(InvalidStatusChangeException.class)
                .hasMessageContaining("Status inválido");
    }

    @Test
    void lancaExcecaoQuandoPagamentoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.atualizarStatusPagamento(1L, StatusPagamentoEnum.PROCESSADO_SUCESSO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pagamento não encontrado");
    }

    @Test
    void listaPagamentosComFiltros() {
        Page<Pagamento> page = new PageImpl<>(List.of(pagamento));
        when(repository.findByFiltros(any(), any(), any(), any(Pageable.class))).thenReturn(page);

        Page<Pagamento> resultado = service.listarPagamentos(123, "12345678901",
                StatusPagamentoEnum.PENDENTE_PROCESSAMENTO, 0, 5, "id", "asc");

        assertThat(resultado.getContent()).hasSize(1);
        verify(repository).findByFiltros(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void inativaPagamentoPendente() {
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(repository.save(any(Pagamento.class))).thenAnswer(inv -> inv.getArgument(0));

        service.deletarPagamento(1L);

        assertThat(pagamento.getStatusPagamento()).isEqualTo(StatusPagamentoEnum.INATIVO);
        verify(repository).save(pagamento);
    }

    @Test
    void lancaExcecaoSeNaoForPendenteAoDeletar() {
        pagamento.setStatusPagamento(StatusPagamentoEnum.PROCESSADO_SUCESSO);
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));

        assertThatThrownBy(() -> service.deletarPagamento(1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Somente pagamentos pendentes");
    }
}
