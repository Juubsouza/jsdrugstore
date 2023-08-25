package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.SaleProduct;
import com.juubsouza.jsdrugstore.model.dto.SaleProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.SaleProductDTO(sp.quantity, sp.product.id, sp.product.name) " +
            "FROM SaleProduct sp WHERE sp.sale.id = ?1")
    List<SaleProductDTO> findAllDTOsBySaleId(Long id);
}