package com.juubsouza.jsdrugstore.repository;

import com.juubsouza.jsdrugstore.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    List<CustomerAddress> findAllByCustomerId(Long id);

    Optional<CustomerAddress> findByAddressId(Long addressId);
}