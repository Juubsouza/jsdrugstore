package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.ProductDTO(p.id, p.name, p.manufacturer, pr.price, s.stock) FROM Product p " +
            "JOIN Price pr ON pr.product_id = p.id " +
            "JOIN Stock s ON s.product_id = p.id ")
    List<ProductDTO> findAllProducts();

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.ProductDTO(p.id, p.name, p.manufacturer, pr.price, s.stock) FROM Product p " +
            "JOIN Price pr ON pr.product_id = p.id " +
            "JOIN Stock s ON s.product_id = p.id " +
            "WHERE p.id = ?1")
    Optional<ProductDTO> findProductById(Long id);
}
