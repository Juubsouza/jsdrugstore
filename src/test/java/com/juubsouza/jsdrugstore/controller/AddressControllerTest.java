package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

        validateAndExpectBadRequest(addressDTOAdd, "Customer ID must be greater than zero.");

        addressDTOAdd.setCustomerId(0L);

        validateAndExpectBadRequest(addressDTOAdd, "Customer ID must be greater than zero.");
    }

    @Test
    public void testValidateAddressDoesNotExist() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressService.addressExists(anyLong())).thenReturn(false);

        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Address not found."));
    }

    @Test
    public void testValidateId() throws Exception {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();
        addressDTO.setId(null);

        validateAndExpectBadRequest(addressDTO, "Address ID must be greater than zero.");

        addressDTO.setId(0L);

        validateAndExpectBadRequest(addressDTO, "Address ID must be greater than zero.");
    }

    @Test
    public void testValidateDetails() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setDetails(null);

        validateAndExpectBadRequest(addressDTOAdd, "Address details cannot be empty.");

        addressDTOAdd.setDetails("");

        validateAndExpectBadRequest(addressDTOAdd, "Address details cannot be empty.");
    }

    @Test
    public void testValidateCity() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setCity(null);

        validateAndExpectBadRequest(addressDTOAdd, "City cannot be empty.");

        addressDTOAdd.setCity("");

        validateAndExpectBadRequest(addressDTOAdd, "City cannot be empty.");
    }

    @Test
    public void testValidateState() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setState(null);

        validateAndExpectBadRequest(addressDTOAdd, "State cannot be empty.");

        addressDTOAdd.setState("");

        validateAndExpectBadRequest(addressDTOAdd, "State cannot be empty.");
    }

    @Test
    public void testValidateCountry() throws Exception {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setCountry(null);

        validateAndExpectBadRequest(addressDTOAdd, "Country cannot be empty.");

        addressDTOAdd.setCountry("");

        validateAndExpectBadRequest(addressDTOAdd, "Country cannot be empty.");
    }

    private void validateAndExpectBadRequest(AddressDTOAdd addressDTOAdd, String expectedMessage) throws Exception {
        mockMvc.perform(post("/address/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(expectedMessage));
    }

    private void validateAndExpectBadRequest(AddressDTO addressDTO, String expectedMessage) throws Exception {
        mockMvc.perform(post("/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(expectedMessage));
    }
}