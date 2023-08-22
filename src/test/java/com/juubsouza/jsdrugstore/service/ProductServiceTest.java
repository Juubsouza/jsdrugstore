package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Price;
import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.Stock;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.repository.PriceRepository;
import com.juubsouza.jsdrugstore.repository.ProductRepository;
import com.juubsouza.jsdrugstore.repository.StockRepository;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private StockRepository stockRepository;

    private final Long PRODUCT_ID = 1L;

    @Test
    public void testAddProduct() {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();

        Product product = new Product();

        Price price = new Price();

        Stock stock = new Stock();

        when(productRepository.save(any())).thenReturn(product);
        when(priceRepository.save(any())).thenReturn(price);
        when(stockRepository.save(any())).thenReturn(stock);

        ProductDTO addedProductDTO = productService.addProduct(productDTOAdd);

        assertNotNull(addedProductDTO);
        assertEquals(product.getName(), addedProductDTO.getName());

        verify(productRepository, times(1)).save(any());
        verify(priceRepository, times(1)).save(any());
        verify(stockRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(newMockProduct()));

        assertDoesNotThrow(() -> productService.deleteProduct(PRODUCT_ID));

        verify(productRepository, times(1)).findById(PRODUCT_ID);
        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
    }

    @Test
    public void testFindAllProducts() {
        productService.findAllProducts();

        verify(productRepository, times(1)).findAllDTOs();
    }

    @Test
    public void testFindProductById() {
        productService.findProductById(PRODUCT_ID);

        verify(productRepository, times(1)).findDTOById(PRODUCT_ID);
    }

    @Test
    public void testFindProductByName() {
        productService.findProductsByName("Test Product");

        verify(productRepository, times(1)).findDTOsByName("Test Product");
    }

    @Test
    public void testProductExists() {
        when(productRepository.existsByName("Test Product")).thenReturn(true);

        assertTrue(productService.productExists("Test Product"));
    }

    @Test
    public void testUpdateProduct_EntityNotFoundException() {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productDTO));
    }

    @Test
    public void testUpdateProduct_EntityNotFoundExceptionForPrice() {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(newMockProduct()));
        when(priceRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productDTO));
    }

    @Test
    public void testUpdateProduct_EntityNotFoundExceptionForStock() {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(newMockProduct()));
        when(priceRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(new Price()));
        when(stockRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productDTO));
    }

    @Test
    public void testUpdateProductOk() {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(newMockProduct()));
        when(priceRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(new Price()));
        when(stockRepository.findByProductId(PRODUCT_ID)).thenReturn(Optional.of(new Stock()));

        assertDoesNotThrow(() -> productService.updateProduct(productDTO));

        verify(productRepository, times(1)).save(any());
        verify(priceRepository, times(1)).save(any());
        verify(stockRepository, times(1)).save(any());
    }

    private Product newMockProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);

        return product;
    }
}