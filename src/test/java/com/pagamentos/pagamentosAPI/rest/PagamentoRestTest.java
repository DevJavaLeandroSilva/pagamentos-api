package com.pagamentos.pagamentosAPI.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.MetodoPagamentoEnum;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.exceptions.BadRequestException;
import com.pagamentos.pagamentosAPI.rest.dto.request.AtualizacaoPagamentoRequestDTO;
import com.pagamentos.pagamentosAPI.rest.dto.request.PagamentoRequestDTO;
import com.pagamentos.pagamentosAPI.service.interfaces.IPagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoRest.class)
public class PagamentoRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPagamentoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Pagamento pagamento;
    private PagamentoRequestDTO pagamentoRequestDTO;

    @BeforeEach
    void setup() {
        pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setCodigoDebito(10);
        pagamento.setCpfCnpj("12345678901");
        pagamento.setMetodoPagamento(MetodoPagamentoEnum.PIX);
        pagamento.setValorPagamento(BigDecimal.valueOf(50.00));
        pagamento.setStatusPagamento(StatusPagamentoEnum.PENDENTE_PROCESSAMENTO);

        pagamentoRequestDTO = new PagamentoRequestDTO();
        pagamentoRequestDTO.setCodigoDebito(10);
        pagamentoRequestDTO.setCpfCnpj("12345678901");
        pagamentoRequestDTO.setMetodoPagamento(MetodoPagamentoEnum.PIX);
        pagamentoRequestDTO.setValorPagamento(BigDecimal.valueOf(50.00));
    }

    @Test
    void criaNovoPagamento() throws Exception {
        Mockito.when(service.novoPagamento(any(Pagamento.class))).thenReturn(pagamento);

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagamentoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCnpj").value("12345678901"))
                .andExpect(jsonPath("$.statusPagamento").value("PENDENTE_PROCESSAMENTO"));
    }

    @Test
    void retornaErroCpfCnpjVazio() throws Exception {
        pagamentoRequestDTO.setCpfCnpj("");

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagamentoRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retornaErroCartaoSemNumero() throws Exception {
        pagamentoRequestDTO.setMetodoPagamento(MetodoPagamentoEnum.CARTAO_CREDITO);
        pagamentoRequestDTO.setNumeroCartao(null);

        Mockito.when(service.novoPagamento(any()))
                .thenThrow(new BadRequestException("O número do cartão não pode ser vazio."));

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagamentoRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void retornaErroStatusInvalido() throws Exception {
        AtualizacaoPagamentoRequestDTO request = new AtualizacaoPagamentoRequestDTO();
        request.setNovoStatus(StatusPagamentoEnum.INATIVO);

        Mockito.when(service.atualizarStatusPagamento(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Status inválido para pagamento pendente."));

        mockMvc.perform(patch("/api/pagamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listaPagamentos() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));
        Page<Pagamento> page = new PageImpl<>(List.of(pagamento), pageable, 1);

        Mockito.when(service.listarPagamentos(any(), any(), any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/pagamentos")
                        .param("codigoDebito", "10")
                        .param("cpfCnpj", "12345678901")
                        .param("statusPagamento", "PENDENTE_PROCESSAMENTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigoDebito").value(10))
                .andExpect(jsonPath("$.content[0].cpfCnpj").value("12345678901"));
    }

    @Test
    void atualizaStatusPagamento() throws Exception {
        pagamento.setStatusPagamento(StatusPagamentoEnum.PROCESSADO_SUCESSO);

        AtualizacaoPagamentoRequestDTO request = new AtualizacaoPagamentoRequestDTO();
        request.setNovoStatus(StatusPagamentoEnum.PROCESSADO_SUCESSO);

        Mockito.when(service.atualizarStatusPagamento(eq(1L), any()))
                .thenReturn(pagamento);

        mockMvc.perform(patch("/api/pagamentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusPagamento").value("PROCESSADO_SUCESSO"));
    }

    @Test
    void deletaPagamento() throws Exception {
        Mockito.doNothing().when(service).deletarPagamento(1L);

        mockMvc.perform(delete("/api/pagamentos/{id}", 1L))
                .andExpect(status().isNoContent());
    }


}
