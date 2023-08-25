package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.juubsouza.jsdrugstore.model.dto.SellerDTO;
import com.juubsouza.jsdrugstore.model.dto.SellerDTOAdd;
import com.juubsouza.jsdrugstore.service.SellerService;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerController.class)
public class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @Test
    public void testFindAllSellers() throws Exception {
        List<SellerDTO> sellers = new ArrayList<>();
        when(sellerService.findAllSellers()).thenReturn(sellers);

        mockMvc.perform(get("/seller/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testFindSellerById() throws Exception {
        SellerDTO seller = MockDTOs.newMockSellerDTO();
        when(sellerService.findSellerById(any(Long.class))).thenReturn(seller);

        mockMvc.perform(get("/seller/by-id={id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(seller.getId().intValue())));
    }

    @Test
    public void testFindSellerByFirstOrLastName() throws Exception {
        List<SellerDTO> sellers = new ArrayList<>();
        sellers.add(MockDTOs.newMockSellerDTO());
        when(sellerService.findSellersByFirstOrLastName(any(String.class))).thenReturn(sellers);

        mockMvc.perform(get("/seller/by-name={name}", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testAddSellerOk() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        when(sellerService.addSeller(any(SellerDTOAdd.class))).thenReturn(sellerDTO);

        mockMvc.perform(post("/seller/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(sellerDTO.getId().intValue())));
    }

    @Test
    public void testAddSellerOkWithNightShift() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setShift("NIGHT");
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        when(sellerService.addSeller(any(SellerDTOAdd.class))).thenReturn(sellerDTO);

        mockMvc.perform(post("/seller/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(sellerDTO.getId().intValue())));
    }

    @Test
    public void testAddSellerFirstNameNullOrEmpty() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setFirstName(null);

        validateAndExpectBadRequest("Seller first name cannot be empty.", sellerDTOAdd);

        sellerDTOAdd.setFirstName("");

        validateAndExpectBadRequest("Seller first name cannot be empty.", sellerDTOAdd);
    }

    @Test
    public void testAddSellerLastNullOrEmpty() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setLastName(null);

        validateAndExpectBadRequest("Seller last name cannot be empty.", sellerDTOAdd);

        sellerDTOAdd.setLastName("");

        validateAndExpectBadRequest("Seller last name cannot be empty.", sellerDTOAdd);
    }

    @Test
    public void testAddSellerShiftNullOrEmpty() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setShift(null);

        validateAndExpectBadRequest("Seller must have a valid shift: 'DAY' or 'NIGHT'.", sellerDTOAdd);

        sellerDTOAdd.setShift("");

        validateAndExpectBadRequest("Seller must have a valid shift: 'DAY' or 'NIGHT'.", sellerDTOAdd);
    }

    @Test
    public void testAddSellerShiftInvalid() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setShift("SOMETHING");

        validateAndExpectBadRequest("Seller must have a valid shift: 'DAY' or 'NIGHT'.", sellerDTOAdd);

        sellerDTOAdd.setShift("");

        validateAndExpectBadRequest("Seller must have a valid shift: 'DAY' or 'NIGHT'.", sellerDTOAdd);
    }

    @Test
    public void testAddSellerAdmissionDateNull() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        sellerDTOAdd.setAdmissionDate(null);

        validateAndExpectBadRequest("Seller admission date cannot be empty.", sellerDTOAdd);
    }

    @Test
    public void testUpdateAddWithException() throws Exception {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();
        when(sellerService.sellerExists(any(Long.class))).thenReturn(false);
        when(sellerService.addSeller(any(SellerDTOAdd.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/seller/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTOAdd)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteSellerOk() throws Exception {
        mockMvc.perform(delete("/seller/delete={id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteSellerWithException() throws Exception {
        doThrow(new RuntimeException()).when(sellerService).deleteSeller(any(Long.class));

        mockMvc.perform(delete("/seller/delete={id}", 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateSellerOk() throws Exception {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        when(sellerService.sellerExists(any(Long.class))).thenReturn(true);
        when(sellerService.updateSeller(any(SellerDTO.class))).thenReturn(sellerDTO);

        mockMvc.perform(post("/seller/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(sellerDTO.getId().intValue())));
    }

    @Test
    public void testUpdateSellerWithException() throws Exception {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        when(sellerService.sellerExists(any(Long.class))).thenReturn(true);
        when(sellerService.updateSeller(any(SellerDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/seller/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateSellerIdNullOrZero() throws Exception {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        sellerDTO.setId(null);

        validateAndExpectBadRequest("Seller ID must be greater than zero.", sellerDTO);

        sellerDTO.setId(0L);

        validateAndExpectBadRequest("Seller ID must be greater than zero.", sellerDTO);
    }

    @Test
    public void testUpdateSellerDoesNotExist() throws Exception {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();
        when(sellerService.sellerExists(any(Long.class))).thenReturn(false);

        mockMvc.perform(post("/seller/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTO)))
                .andExpect(status().isNotFound());
    }

    private void validateAndExpectBadRequest(String message, SellerDTO sellerDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("/seller/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }

    private void validateAndExpectBadRequest(String message, SellerDTOAdd sellerDTOAdd) throws Exception {
        MvcResult result = mockMvc.perform(post("/seller/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sellerDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }
}
