package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.Customer;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.CustomerDTO(c.id, c.firstName, c.lastName, c.email) FROM Customer c")
    List<CustomerDTO> findAllDTOs();

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.CustomerDTO(c.id, c.firstName, c.lastName, c.email) FROM Customer c " +
            "WHERE c.id = ?1")
    Optional<CustomerDTO> findDTOById(Long id);

    @Query("SELECT new com.juubsouza.jsdrugstore.model.dto.CustomerDTO(c.id, c.firstName, c.lastName, c.email) FROM Customer c " +
            "WHERE c.firstName LIKE %?1% OR c.lastName LIKE %?1%")
    List<CustomerDTO> findDTOsByFirstOrLastName(String name);
}
