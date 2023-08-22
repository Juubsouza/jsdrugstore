package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

}
