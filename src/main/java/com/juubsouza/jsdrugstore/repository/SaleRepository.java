package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Sale;
import com.juubsouza.jsdrugstore.model.dto.SaleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.SaleDTO(s.id, s.paymentMethod, s.paymentStatus, s.shippingStatus, s.total, s.customer.id, s.seller.id) FROM Sale s")
    List<SaleDTO> findAllDTOsByCustomerId(Long id);
}
