package com.juubsouza.jsdrugstore.utils;

import com.juubsouza.jsdrugstore.model.*;
import com.juubsouza.jsdrugstore.model.dto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockDTOs {

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

    public static CustomerDTO newMockCustomerDTO() {
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

    public static SaleDTO newMockSaleDTO() {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(1L);
        saleDTO.setPaymentMethod("CASH");
        saleDTO.setPaymentStatus("PENDING");
        saleDTO.setShippingStatus("PENDING");
        saleDTO.setTotal(BigDecimal.valueOf(10.0));
        saleDTO.setCustomerId(1L);
        saleDTO.setSellerId(1L);
        return saleDTO;
    }

    public static SaleDTO newMockSaleDTO(Long id) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(id);
        saleDTO.setPaymentMethod("CASH");
        saleDTO.setPaymentStatus("PENDING");
        saleDTO.setShippingStatus("PENDING");
        saleDTO.setTotal(BigDecimal.valueOf(10.0));
        saleDTO.setCustomerId(1L);
        saleDTO.setSellerId(1L);
        return saleDTO;
    }

    public static SaleDTOAdd newMockSaleDTOAdd() {
        SaleDTOAdd saleDTOAdd = new SaleDTOAdd();
        saleDTOAdd.setPaymentMethod("CASH");
        saleDTOAdd.setCustomerId(1L);
        saleDTOAdd.setSellerId(1L);

        List<SaleProductDTOAdd> products = new ArrayList<>();

        SaleProductDTOAdd saleProductDTOAdd = new SaleProductDTOAdd();
        saleProductDTOAdd.setProductId(1L);
        saleProductDTOAdd.setQuantity(1);

        products.add(saleProductDTOAdd);

        SaleProductDTOAdd saleProductDTOAdd2 = new SaleProductDTOAdd();
        saleProductDTOAdd2.setProductId(2L);
        saleProductDTOAdd2.setQuantity(1);

        products.add(saleProductDTOAdd2);

        saleDTOAdd.setSaleProducts(products);

        return saleDTOAdd;
    }

    public static List<Product> newMockProducts() {
        List<Product> products = new ArrayList<>();

        products.add(newMockProduct());
        products.add(newMockProduct(2L));

        return products;
    }

    public static SellerDTO newMockSellerDTO() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setId(1L);
        sellerDTO.setFirstName("John");
        sellerDTO.setLastName("Doe");
        sellerDTO.setShift("DAY");
        sellerDTO.setAdmissionDate(new Date());
        return sellerDTO;
    }

    public static SellerDTOAdd newMockSellerDTOAdd() {
        SellerDTOAdd sellerDTOAdd = new SellerDTOAdd();
        sellerDTOAdd.setFirstName("John");
        sellerDTOAdd.setLastName("Doe");
        sellerDTOAdd.setShift("DAY");
        sellerDTOAdd.setAdmissionDate(new Date());
        return sellerDTOAdd;
    }

    public static Customer newMockCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@email.com");

        return customer;
    }

    public static Seller newMockSeller() {
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("John");
        seller.setLastName("Doe");
        seller.setShift("DAY");
        seller.setAdmissionDate(new Date());

        return seller;
    }

    public static Price newMockPrice() {
        Price price = new Price();
        price.setId(1L);
        price.setPrice(BigDecimal.valueOf(10.0));

        return price;
    }

    public static Product newMockProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(newMockPrice());
        product.setStock(newMockStock());

        return product;
    }

    public static Product newMockProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(newMockPrice());
        product.setStock(newMockStock());

        return product;
    }

    public static Stock newMockStock() {
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setStock(100);

        return stock;
    }

    public static Stock newMockStock(Long id) {
        Stock stock = new Stock();
        stock.setId(id);
        stock.setStock(100);

        return stock;
    }

    public static List<SaleDTO> newMockSaleDTOs() {
        List<SaleDTO> saleDTOList = new ArrayList<>();

        saleDTOList.add(newMockSaleDTO());
        saleDTOList.add(newMockSaleDTO(2L));

        return saleDTOList;
    }

    public static List<SaleProductDTO> newMockSaleProductDTOs() {
        List<SaleProductDTO> saleProductDTOList = new ArrayList<>();

        saleProductDTOList.add(newMockSaleProductDTO());
        saleProductDTOList.add(newMockSaleProductDTO(2L));

        return saleProductDTOList;
    }

    private static SaleProductDTO newMockSaleProductDTO() {
        SaleProductDTO saleProductDTO = new SaleProductDTO();
        saleProductDTO.setQuantity(1);
        saleProductDTO.setProductId(1L);
        saleProductDTO.setProductName("Test Product");

        return saleProductDTO;
    }

    private static SaleProductDTO newMockSaleProductDTO(Long id) {
        SaleProductDTO saleProductDTO = new SaleProductDTO();
        saleProductDTO.setQuantity(1);
        saleProductDTO.setProductId(id);
        saleProductDTO.setProductName("Test Product");

        return saleProductDTO;
    }
}
