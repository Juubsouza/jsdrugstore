package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
}