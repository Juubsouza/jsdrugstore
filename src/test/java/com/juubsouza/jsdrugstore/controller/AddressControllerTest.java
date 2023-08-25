package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.juubsouza.jsdrugstore.model.dto.AddressDTO;
import com.juubsouza.jsdrugstore.model.dto.AddressDTOAdd;
import com.juubsouza.jsdrugstore.service.AddressService;

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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    public void testFindAllAddressesByCustomerId() throws Exception {
        List<AddressDTO> addresses = new ArrayList<>();
        when(addressService.findAllAddressesByCustomerId(any(Long.class))).thenReturn(addresses);

        mockMvc.perform(get("/address/all-for-customer=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testActivateAddressAsShipping() throws Exception {
        mockMvc.perform(post("/address/activate-address-as-shipping=1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testActivateAddressAsShippingWithException() throws Exception {
        doThrow(new RuntimeException("Some error message"))
                .when(addressService)
                .setAddressAsShippingTrue(anyLong());

        mockMvc.perform(post("/address/activate-address-as-shipping=1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddAddress() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();

        AddressDTO addedAddress = MockDTOs.newMockAddressDTO();

        when(addressService.addAddress(any(AddressDTOAdd.class))).thenReturn(addedAddress);

        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTOAdd)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddAddressWithException() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();

        when(addressService.addAddress(any(AddressDTOAdd.class))).thenThrow(new RuntimeException("Some error message"));

        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTOAdd)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddAddressWithInvalidFields() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setDetails(null);

        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTOAdd)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteOk() throws Exception {
        mockMvc.perform(delete("/address/delete={id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteWithException() throws Exception {
        doThrow(new RuntimeException("Some error message"))
                .when(addressService)
                .deleteAddress(anyLong());

        mockMvc.perform(delete("/address/delete={id}", 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressService.addressExists(anyLong())).thenReturn(true);
        when(addressService.updateAddress(any(AddressDTO.class))).thenReturn(addressDTO);

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateAddressWithException() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressService.addressExists(anyLong())).thenReturn(true);
        when(addressService.updateAddress(any(AddressDTO.class))).thenThrow(new RuntimeException("Some error message"));

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testValidateCustomerId() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setCustomerId(null);

        validateAndExpectBadRequest("Customer ID must be greater than zero.", addressDTOAdd);

        addressDTOAdd.setCustomerId(0L);

        validateAndExpectBadRequest("Customer ID must be greater than zero.", addressDTOAdd);
    }

    @Test
    public void testValidateAddressDoesNotExist() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressService.addressExists(anyLong())).thenReturn(false);

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testValidateId() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();
        addressDTO.setId(null);

        validateAndExpectBadRequest("Address ID must be greater than zero.", addressDTO);

        addressDTO.setId(0L);

        validateAndExpectBadRequest("Address ID must be greater than zero.", addressDTO);
    }

    @Test
    public void testValidateDetails() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setDetails(null);

        validateAndExpectBadRequest("Address details cannot be empty.", addressDTOAdd);

        addressDTOAdd.setDetails("");

        validateAndExpectBadRequest("Address details cannot be empty.", addressDTOAdd);
    }

    @Test
    public void testValidateCity() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setCity(null);

        validateAndExpectBadRequest("City cannot be empty.", addressDTOAdd);

        addressDTOAdd.setCity("");

        validateAndExpectBadRequest("City cannot be empty.", addressDTOAdd);
    }

    @Test
    public void testValidateState() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setState(null);

        validateAndExpectBadRequest("State cannot be empty.", addressDTOAdd);

        addressDTOAdd.setState("");

        validateAndExpectBadRequest("State cannot be empty.", addressDTOAdd);
    }

    @Test
    public void testValidateCountry() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setCountry(null);

        validateAndExpectBadRequest("Country cannot be empty.", addressDTOAdd);

        addressDTOAdd.setCountry("");

        validateAndExpectBadRequest("Country cannot be empty.", addressDTOAdd);
    }

    private void validateAndExpectBadRequest(String message, AddressDTOAdd addressDTOAdd) throws Exception {
        MvcResult result = mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTOAdd)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }

    private void validateAndExpectBadRequest(String message, AddressDTO addressDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        String errorMessage = JsonPath.read(jsonResponse, "$.message");

        assertEquals(message, errorMessage);
    }
}