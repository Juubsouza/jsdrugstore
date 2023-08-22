package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Seller;
import com.juubsouza.jsdrugstore.model.dto.SellerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.SellerDTO(s.id, s.firstName, s.lastName, s.shift, s.admissionDate) FROM Seller s ")
    List<SellerDTO> findAllDTOs();

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.SellerDTO(s.id, s.firstName, s.lastName, s.shift, s.admissionDate) FROM Seller s " +
            "WHERE s.id = ?1")
    Optional<SellerDTO> findDTOById(Long id);

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.SellerDTO(s.id, s.firstName, s.lastName, s.shift, s.admissionDate) FROM Seller s " +
            "WHERE s.firstName LIKE %?1% OR s.lastName LIKE %?1%")
    List<SellerDTO> findDTOsByFirstOrLastName(String name);
}
