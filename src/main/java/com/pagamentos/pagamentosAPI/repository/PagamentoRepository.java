package com.pagamentos.pagamentosAPI.repository;

import com.pagamentos.pagamentosAPI.entity.Pagamento;
import com.pagamentos.pagamentosAPI.enums.StatusPagamentoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    @Query("""
        select p
        from Pagamento p
        where (:codigoDebito is null or p.codigoDebito = :codigoDebito)
          and (:cpfCnpj is null or p.cpfCnpj = :cpfCnpj)
          and (:status is null or p.statusPagamento = :status)
          order by p.id desc
        """)
    Page<Pagamento> findByFiltros(Integer codigoDebito,
                                  String cpfCnpj,
                                  StatusPagamentoEnum status,
                                  Pageable pageable);
}
