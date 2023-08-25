package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juubsouza.jsdrugstore.model.dto.SaleDTO;
import com.juubsouza.jsdrugstore.model.dto.SaleDTOAdd;
import com.juubsouza.jsdrugstore.service.SaleService;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SaleController.class)
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @Test
    public void testAddSaleOk() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        SaleDTO saleDTO = MockDTOs.newMockSaleDTO();

        when(saleService.addSale(saleDTOAdd)).thenReturn(saleDTO);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(saleDTO.getId().intValue()));
    }

    @Test
    public void testAddSaleNullOrEmptyProducts() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setSaleProducts(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have at least one product."));

        saleDTOAdd.setSaleProducts(new ArrayList<>());

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have at least one product."));
    }

    @Test
    public void testAddSalePaymentMethodNullOrEmpty() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setPaymentMethod(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a payment method."));

        saleDTOAdd.setPaymentMethod("");

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a payment method."));
    }

    @Test
    public void testAddSaleCustomerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setCustomerId(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a customer."));

        saleDTOAdd.setCustomerId(0L);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a customer."));
    }

    @Test
    public void testAddSaleSellerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setSellerId(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a seller."));

        saleDTOAdd.setSellerId(0L);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale must have a seller."));
    }

    @Test
    public void testAddProductListHasProductWithNullId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale product must have an ID."));
    }

    @Test
    public void testAddProductListHasDuplicateProductId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(1L);
        saleDTOAdd.getSaleProducts().get(1).setProductId(1L);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale product cannot be added more than once."));
    }

    @Test
    public void testAddProductListHasProductWithNullQuantityOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setQuantity(null);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale product quantity must be greater than 0."));

        saleDTOAdd.getSaleProducts().get(0).setQuantity(0);

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Sale product quantity must be greater than 0."));
    }

    @Test
    public void testAddProductThrowsException() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();

        when(saleService.addSale(saleDTOAdd)).thenThrow(new RuntimeException());

        mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isInternalServerError());
    }
}
