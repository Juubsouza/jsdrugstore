package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.ProductDTO(p.id, p.name, p.manufacturer, p.price.price, p.stock.stock) " +
            "FROM Product p")
    List<ProductDTO> findAllProducts();

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.ProductDTO(p.id, p.name, p.manufacturer, p.price.price, p.stock.stock)" +
            "FROM Product p " +
            "WHERE p.id = ?1")
    Optional<ProductDTO> findProductById(Long id);

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.ProductDTO(p.id, p.name, p.manufacturer, p.price.price, p.stock.stock)" +
            "FROM Product p " +
            "WHERE p.name = ?1")
    Optional<ProductDTO> findProductByName(String name);
}
