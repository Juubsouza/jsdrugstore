package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have at least one product.", errorMessage);

        saleDTOAdd.setSaleProducts(new ArrayList<>());

        result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        jsonResponse = result.getResponse().getContentAsString();
        errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have at least one product.", errorMessage);
    }

    @Test
    public void testAddSalePaymentMethodNullOrEmpty() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setPaymentMethod(null);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a payment method.", errorMessage);

        saleDTOAdd.setPaymentMethod("");

        result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        jsonResponse = result.getResponse().getContentAsString();
        errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a payment method.", errorMessage);
    }

    @Test
    public void testAddSaleCustomerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setCustomerId(null);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a customer.", errorMessage);

        saleDTOAdd.setCustomerId(0L);

        result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        jsonResponse = result.getResponse().getContentAsString();
        errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a customer.", errorMessage);
    }

    @Test
    public void testAddSaleSellerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setSellerId(null);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a seller.", errorMessage);

        saleDTOAdd.setSellerId(0L);

        result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        jsonResponse = result.getResponse().getContentAsString();
        errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale must have a seller.", errorMessage);
    }

    @Test
    public void testAddProductListHasProductWithNullId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(null);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale product must have an ID.", errorMessage);
    }

    @Test
    public void testAddProductListHasDuplicateProductId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(1L);
        saleDTOAdd.getSaleProducts().get(1).setProductId(1L);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale product cannot be added more than once.", errorMessage);
    }

    @Test
    public void testAddProductListHasProductWithNullQuantityOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setQuantity(null);

        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale product quantity must be greater than 0.", errorMessage);

        saleDTOAdd.getSaleProducts().get(0).setQuantity(0);

        result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        jsonResponse = result.getResponse().getContentAsString();
        errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals("Sale product quantity must be greater than 0.", errorMessage);
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
