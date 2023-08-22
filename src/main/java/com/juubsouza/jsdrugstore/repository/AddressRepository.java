package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Address;
import com.juubsouza.jsdrugstore.model.dto.AddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.AddressDTO(a.id, a.details, a.city, a.state, a.country, ca.isShipping) FROM CustomerAddress ca " +
            "JOIN ca.customer c " +
            "JOIN ca.address a " +
            "WHERE c.id = ?1")
    List<AddressDTO> findAllDTOsByCustomerId(Long id);
}