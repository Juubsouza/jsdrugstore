package com.juubsouza.jsdrugstore.utils;

import com.juubsouza.jsdrugstore.model.dto.*;

import java.math.BigDecimal;

public class MockDTOs {

    public static ProductDTOAdd newMockProductDTOAdd() {
        ProductDTOAdd product = new ProductDTOAdd();
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStock(100);
        return product;
    }

    public static ProductDTO newMockProductDTO() {
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStock(100);
        return product;
    }

    public static CustomerDTO newMockCustomerDTO() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setEmail("johndoe@email.com");
        return customerDTO;
    }

    public static CustomerDTOAdd newMockCustomerDTOAdd() {
        CustomerDTOAdd customerDTOAdd = new CustomerDTOAdd();
        customerDTOAdd.setFirstName("John");
        customerDTOAdd.setLastName("Doe");
        customerDTOAdd.setEmail("johndoe@email.com");
        return customerDTOAdd;
    }

    public static AddressDTO newMockAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setDetails("Test Address");
        addressDTO.setCity("Test City");
        addressDTO.setState("Test State");
        addressDTO.setCountry("Test Country");
        addressDTO.setShipping(true);

        return addressDTO;
    }

    public static AddressDTOAdd newMockAddressDTOAdd() {
        AddressDTOAdd addressDTOAdd = new AddressDTOAdd();
        addressDTOAdd.setDetails("Test Address");
        addressDTOAdd.setCity("Test City");
        addressDTOAdd.setState("Test State");
        addressDTOAdd.setCountry("Test Country");
        addressDTOAdd.setShipping(true);
        addressDTOAdd.setCustomerId(1L);

        return addressDTOAdd;
    }
}
