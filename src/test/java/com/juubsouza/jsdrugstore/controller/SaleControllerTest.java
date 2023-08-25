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
import java.util.List;

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

        validateAndExpectBadRequest("Sale must have at least one product.", saleDTOAdd);

        saleDTOAdd.setSaleProducts(new ArrayList<>());

        validateAndExpectBadRequest("Sale must have at least one product.", saleDTOAdd);
    }

    @Test
    public void testAddSalePaymentMethodNullOrEmpty() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setPaymentMethod(null);

        validateAndExpectBadRequest("Sale must have a payment method.", saleDTOAdd);

        saleDTOAdd.setPaymentMethod("");

        validateAndExpectBadRequest("Sale must have a payment method.", saleDTOAdd);
    }

    @Test
    public void testAddSaleCustomerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setCustomerId(null);

        validateAndExpectBadRequest("Sale must have a customer.", saleDTOAdd);

        saleDTOAdd.setCustomerId(0L);

        validateAndExpectBadRequest("Sale must have a customer.", saleDTOAdd);
    }

    @Test
    public void testAddSaleSellerIdNullOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.setSellerId(null);

        validateAndExpectBadRequest("Sale must have a seller.", saleDTOAdd);

        saleDTOAdd.setSellerId(0L);

        validateAndExpectBadRequest("Sale must have a seller.", saleDTOAdd);
    }

    @Test
    public void testAddProductListHasProductWithNullId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(null);

        validateAndExpectBadRequest("Sale product must have an ID.", saleDTOAdd);
    }

    @Test
    public void testAddProductListHasDuplicateProductId() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setProductId(1L);
        saleDTOAdd.getSaleProducts().get(1).setProductId(1L);

        validateAndExpectBadRequest("Sale product cannot be added more than once.", saleDTOAdd);
    }

    @Test
    public void testAddProductListHasProductWithNullQuantityOrZero() throws Exception {
        SaleDTOAdd saleDTOAdd = MockDTOs.newMockSaleDTOAdd();
        saleDTOAdd.getSaleProducts().get(0).setQuantity(null);

        validateAndExpectBadRequest("Sale product quantity must be greater than 0.", saleDTOAdd);

        saleDTOAdd.getSaleProducts().get(0).setQuantity(0);

        validateAndExpectBadRequest("Sale product quantity must be greater than 0.", saleDTOAdd);
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

    @Test
    public void testFindAllSalesForCustomer() throws Exception {
        List<SaleDTO> saleDTOList = MockDTOs.newMockSaleDTOs();

        when(saleService.findAllSalesForCustomer(1L)).thenReturn(saleDTOList);

        mockMvc.perform(get("/sale/all-for-customer=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(saleDTOList.get(0).getId().intValue()))
                .andExpect(jsonPath("$[1].id").value(saleDTOList.get(1).getId().intValue()));
    }

    private void validateAndExpectBadRequest(String message, SaleDTOAdd saleDTOAdd) throws Exception {
        MvcResult result = mockMvc.perform(post("/sale/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(saleDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }
}
