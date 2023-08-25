package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.service.ProductService;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testFindAllProducts() throws Exception {
        List<ProductDTO> products = new ArrayList<>();
        when(productService.findAllProducts()).thenReturn(products);

        mockMvc.perform(get("/product/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testFindProductById() throws Exception {
        ProductDTO product = MockDTOs.newMockProductDTO();
        when(productService.findProductById(any(Long.class))).thenReturn(product);

        mockMvc.perform(get("/product/by-id={id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(product.getId().intValue())));
    }

    @Test
    public void testFindProductByName() throws Exception {
        List<ProductDTO> products = new ArrayList<>();
        products.add(MockDTOs.newMockProductDTO());
        when(productService.findProductsByName(anyString())).thenReturn(products);

        mockMvc.perform(get("/product/by-name={name}", "Test Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is(products.get(0).getName())));
    }

    @Test
    public void testAddProduct() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();

        ProductDTO addedProduct = MockDTOs.newMockProductDTO();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.addProduct(productDTOAdd)).thenReturn(addedProduct);

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(addedProduct.getId().intValue())));
    }

    @Test
    public void testUpdateProductOk() throws Exception {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.updateProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(productDTO.getId().intValue())));
    }

    @Test
    public void testUpdateProductWithInvalidId() throws Exception {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();
        productDTO.setId(null);

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.updateProduct(any(ProductDTO.class))).thenReturn(productDTO);

        validateAndExpectBadRequest("Product ID must be greater than zero.", productDTO);

        productDTO.setId(0L);

        validateAndExpectBadRequest("Product ID must be greater than zero.", productDTO);
    }

    @Test
    public void testUpdateProductThrowsException() throws Exception {
        ProductDTO productDTO = MockDTOs.newMockProductDTO();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.updateProduct(any(ProductDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteProductOk() throws Exception {
        mockMvc.perform(delete("/product/delete={id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProductThrowsException() throws Exception {
        doThrow(new RuntimeException("Some error message"))
                .when(productService)
                .deleteProduct(anyLong());

        mockMvc.perform(delete("/product/delete=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddProductWithEmptyOrNullName() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();
        productDTOAdd.setName("");

        when(productService.productExists(anyString())).thenReturn(false);

        validateAndExpectBadRequest("Product name cannot be empty.", productDTOAdd);

        productDTOAdd.setName(null);

        validateAndExpectBadRequest("Product name cannot be empty.", productDTOAdd);
    }

    @Test
    public void testAddProductWithExistingName() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();

        when(productService.productExists(anyString())).thenReturn(true);

        validateAndExpectBadRequest("There already is a product with this name in the database.", productDTOAdd);
    }

    @Test
    public void testAddProductWithEmptyOrNullManufacturer() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();
        productDTOAdd.setManufacturer("");

        when(productService.productExists(anyString())).thenReturn(false);

        validateAndExpectBadRequest("Product manufacturer cannot be empty.", productDTOAdd);

        productDTOAdd.setManufacturer(null);

        validateAndExpectBadRequest("Product manufacturer cannot be empty.", productDTOAdd);
    }

    @Test
    public void testAddProductWithNullPriceOrPriceLessThanZero() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();
        productDTOAdd.setPrice(null);

        when(productService.productExists(anyString())).thenReturn(false);

        validateAndExpectBadRequest("Product price must be greater than 0.", productDTOAdd);

        productDTOAdd.setPrice(BigDecimal.valueOf(-1.0));

        validateAndExpectBadRequest("Product price must be greater than 0.", productDTOAdd);
    }

    @Test
    public void testAddProductWithNullStockOrStockLessThanZero() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();
        productDTOAdd.setStock(null);

        when(productService.productExists(anyString())).thenReturn(false);

        validateAndExpectBadRequest("Product stock must be greater than or equal to 0.", productDTOAdd);

        productDTOAdd.setStock(-1);

        validateAndExpectBadRequest("Product stock must be greater than or equal to 0.", productDTOAdd);
    }

    @Test
    public void testAddProductWithException() throws Exception {
        ProductDTOAdd productDTOAdd = MockDTOs.newMockProductDTOAdd();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.addProduct(any(ProductDTOAdd.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isInternalServerError());
    }

    private void validateAndExpectBadRequest(String message, ProductDTO productDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }

    private void validateAndExpectBadRequest(String message, ProductDTOAdd productDTOadd) throws Exception {
        MvcResult result = mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOadd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }
}
