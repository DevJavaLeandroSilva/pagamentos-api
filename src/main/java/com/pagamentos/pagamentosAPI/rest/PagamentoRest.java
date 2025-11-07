package com.pagamentos.pagamentosAPI.rest;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import com.pagamentos.pagamentosAPI.rest.dto.request.AtualizacaoPagamentoRequestDTO;
import com.pagamentos.pagamentosAPI.rest.dto.request.PagamentoRequestDTO;
import com.pagamentos.pagamentosAPI.rest.dto.response.PagamentoResponseDTO;
import com.pagamentos.pagamentosAPI.rest.factory.PagamentoRestFactory;
import com.pagamentos.pagamentosAPI.service.interfaces.IPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoRest {

    @Autowired
    private IPagamentoService service;

    @Operation(
            summary = "Cria um novo pagamento",
            description = """
            Cria um pagamento no sistema com **status inicial "PENDENTE_PROCESSAMENTO"**
            <br><br>
            **Validações aplicadas:**
            - O campo `cpfCnpj` deve conter apenas números (sem pontos, traços ou espaços)
            - Para pagamentos com método `CARTAO_CREDITO` ou `CARTAO_DEBITO`, o campo `numeroCartao` é obrigatório
            - O `numeroCartao` deve conter apenas dígitos, entre **13 e 19 caracteres**
            - O valor do pagamento deve ser maior que 0.01
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pagamento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = PagamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos campos ou formato inválido",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> novoPagamento(@Valid @RequestBody PagamentoRequestDTO request) {
        Pagamento pagamento = PagamentoRestFactory.toEntity(request);
        Pagamento salvarPagamento = service.novoPagamento(pagamento);

        return ResponseEntity.ok(PagamentoRestFactory.toResponse(salvarPagamento));
    }

    @Operation(
            summary = "Lista pagamentos com filtros e paginação",
            description = """
            Retorna uma lista paginada de pagamentos com opção de filtros
            <br><br>
            **Filtros disponíveis:**
            - `codigoDebito`: inteiro representando o débito pago
            - `cpfCnpj`: CPF ou CNPJ exato do pagador (sem pontuação)
            - `statusPagamento`: status atual do pagamento
            <br><br>
            **Paginação e ordenação:**
            - `paginaAtual`: número da página
            - `tamanhoPagina`: quantidade de registros por página
            - `ordenacao`: campo de ordenação
            - `direcao`: `asc` ou `desc`
            """
    )
    @GetMapping
    public ResponseEntity<Page<PagamentoResponseDTO>> listarPagamentos(@RequestParam(required = false) Integer codigoDebito,
                                                                       @RequestParam(required = false) String cpfCnpj,
                                                                       @RequestParam(required = false) StatusPagamentoEnum statusPagamento,
                                                                       @RequestParam(defaultValue = "0") int paginaAtual,
                                                                       @RequestParam(defaultValue = "5") int tamanhoPagina,
                                                                       @RequestParam(defaultValue = "id") String ordenacao,
                                                                       @RequestParam(defaultValue = "asc") String direcao) {

        Page<Pagamento> pagina = service.listarPagamentos(
                codigoDebito, cpfCnpj, statusPagamento, paginaAtual, tamanhoPagina, ordenacao, direcao
        );

        return ResponseEntity.ok(PagamentoRestFactory.toResponsePage(pagina));

    }


    @Operation(
            summary = "Atualiza o status de um pagamento existente",
            description = """
            Atualiza o status de um pagamento com base nas regras de negócio:
            <br><br>
            **Regras de atualização:**
            - Se o pagamento está **PENDENTE_PROCESSAMENTO**, ele pode ser alterado para **PROCESSADO_SUCESSO** ou **PROCESSADO_FALHA**
            - Se está **PROCESSADO_SUCESSO**, **não pode ser alterado**
            - Se está **PROCESSADO_FALHA**, só pode retornar para **PENDENTE_PROCESSAMENTO**
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = PagamentoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Transição de status inválida")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizarStatusPagamento(
                                                    @PathVariable Long id,
                                                    @Valid @RequestBody AtualizacaoPagamentoRequestDTO request) {
        Pagamento pagamentoAtualizado = service.atualizarStatusPagamento(id, request.getNovoStatus());
        return ResponseEntity.ok(PagamentoRestFactory.toResponse(pagamentoAtualizado));
    }

    @Operation(
            summary = "Inativa/deleta um pagamento",
            description = """
            Realiza uma exclusão lógica do pagamento, mantendo o registro no banco
            <br><br>
            **Condições obrigatórias:**
            - O pagamento deve estar com status **PENDENTE_PROCESSAMENTO**
            - O campo `statusPagamento` será atualizado para **INATIVO**
            - O pagamento não é removido do banco
            """,
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pagamento inativado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Pagamento não está pendente de processamento"),
                    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLogicamente(@PathVariable Long id) {
        service.deletarPagamento(id);
        return ResponseEntity.noContent().build();
    }

}
