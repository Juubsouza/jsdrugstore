package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.service.ProductService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        mockMvc.perform(get("/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testFindProductById() throws Exception {
        ProductDTO product = this.newMockProductDTO();
        when(productService.findProductById(any(Long.class))).thenReturn(product);

        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(product.getId().intValue()))) ;
    }

    @Test
    public void testAddProduct() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();

        ProductDTO addedProduct = this.newMockProductDTO();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.addProduct(productDTOAdd)).thenReturn(addedProduct);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(addedProduct.getId().intValue())));
    }

    @Test
    public void testAddProductWithEmptyOrNullName() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();
        productDTOAdd.setName("");

        when(productService.productExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product name cannot be empty.")));

        productDTOAdd.setName(null);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product name cannot be empty.")));
    }

    @Test
    public void testAddProductWithExistingName() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();

        when(productService.productExists(anyString())).thenReturn(true);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("There already is a product with this name in the database.")));
    }

    @Test
    public void testAddProductWithEmptyOrNullManufacturer() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();
        productDTOAdd.setManufacturer("");

        when(productService.productExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product manufacturer cannot be empty.")));

        productDTOAdd.setManufacturer(null);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product manufacturer cannot be empty.")));
    }

    @Test
    public void testAddProductWithNullPriceOrPriceLessThanZero() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();
        productDTOAdd.setPrice(null);

        when(productService.productExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product price must be greater than 0.")));

        productDTOAdd.setPrice(BigDecimal.valueOf(-1.0));

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product price must be greater than 0.")));
    }

    @Test
    public void testAddProductWithNullStockOrStockLessThanZero() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();
        productDTOAdd.setStock(null);

        when(productService.productExists(anyString())).thenReturn(false);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product stock must be greater than or equal to 0.")));

        productDTOAdd.setStock(-1);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is("Product stock must be greater than or equal to 0.")));
    }

    @Test
    public void testAddProductWithException() throws Exception {
        ProductDTOAdd productDTOAdd = this.newMockProductDTOAdd();

        when(productService.productExists(anyString())).thenReturn(false);
        when(productService.addProduct(any(ProductDTOAdd.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTOAdd)))
                .andExpect(status().isInternalServerError());
    }

    private ProductDTOAdd newMockProductDTOAdd(){
        ProductDTOAdd product = new ProductDTOAdd();
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStock(100);
        return product;
    }

    private ProductDTO newMockProductDTO(){
        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setName("Test Product");
        product.setManufacturer("Test Manufacturer");
        product.setPrice(BigDecimal.valueOf(10.0));
        product.setStock(100);
        return product;
    }
}
