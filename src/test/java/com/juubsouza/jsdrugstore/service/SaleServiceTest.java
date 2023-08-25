package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.dto.SaleDTOAdd;
import com.juubsouza.jsdrugstore.repository.*;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleProductRepository saleProductRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    private final Long MOCK_ID = 1L;

    private final Long MOCK_ID_TWO = 2L;

    @Test
    public void testAddSaleOk() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockCustomer()));
        when(sellerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockSeller()));
        when(productRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockProduct()));
        when(productRepository.findById(MOCK_ID_TWO)).thenReturn(Optional.of(MockDTOs.newMockProduct(MOCK_ID_TWO)));
        when(productRepository.findByIdIn(any())).thenReturn(MockDTOs.newMockProducts());
        when(stockRepository.findByProductId(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockStock()));
        when(stockRepository.findByProductId(MOCK_ID_TWO)).thenReturn(Optional.of(MockDTOs.newMockStock(MOCK_ID_TWO)));

        saleService.addSale(saleDTOAdd);

        verify(saleRepository, times(1)).save(any());
        verify(saleProductRepository, times(1)).saveAll(any());
        verify(stockRepository, times(1)).saveAll(any());
    }

    @Test
    public void testAddSaleCustomerNotFound() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> saleService.addSale(saleDTOAdd));
    }

    @Test
    public void testAddSaleSellerNotFound() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockCustomer()));
        when(sellerRepository.findById(MOCK_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> saleService.addSale(saleDTOAdd));
    }


    @Test
    public void testAddSaleProductNotFound() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockCustomer()));
        when(sellerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockSeller()));
        when(productRepository.findById(MOCK_ID)).thenReturn(Optional.empty());
        when(productRepository.findByIdIn(any())).thenReturn(MockDTOs.newMockProducts());

        assertThrows(EntityNotFoundException.class, () -> saleService.addSale(saleDTOAdd));
    }

    @Test
    public void testAddSaleStockNotFound() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockCustomer()));
        when(sellerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockSeller()));
        when(productRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockProduct()));
        when(productRepository.findById(MOCK_ID_TWO)).thenReturn(Optional.of(MockDTOs.newMockProduct(MOCK_ID_TWO)));
        when(productRepository.findByIdIn(any())).thenReturn(MockDTOs.newMockProducts());
        when(stockRepository.findByProductId(MOCK_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> saleService.addSale(saleDTOAdd));
    }

    @Test
    public void testAddSaleProductNotFoundWhenUpdatingStock() {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        List<Product> products = MockDTOs.newMockProducts();

        when(customerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockCustomer()));
        when(sellerRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockSeller()));
        when(productRepository.findById(MOCK_ID)).thenReturn(Optional.of(MockDTOs.newMockProduct()));
        when(productRepository.findById(MOCK_ID_TWO)).thenReturn(Optional.empty());
        when(productRepository.findByIdIn(any())).thenReturn(products);
        when(stockRepository.findByProductId(MOCK_ID)).thenReturn(Optional.empty());
        when(stockRepository.findByProductId(MOCK_ID_TWO)).thenReturn(Optional.empty());

        List<Product> emptyProducts = Collections.emptyList();
        when(productRepository.findByIdIn(any())).thenReturn(emptyProducts);

        assertThrows(EntityNotFoundException.class, () -> saleService.addSale(saleDTOAdd));
    }

    @Test
    public void testFindAllSalesForCustomerOk() {
        saleService.findAllSalesForCustomer(MOCK_ID);

        verify(saleRepository, times(1)).findAllDTOsByCustomerId(MOCK_ID);
    }
}
