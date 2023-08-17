package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

}
