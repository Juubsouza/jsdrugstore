package com.juubsouza.jsdrugstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTO;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTOAdd;
import com.juubsouza.jsdrugstore.service.CustomerService;
import org.hamcrest.Matchers;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerservice;

    @Test
    public void testFindAllCustomers() throws Exception {
        List<CustomerDTO> customers = new ArrayList<>();
        when(customerservice.findAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testFindCustomerById() throws Exception {
        CustomerDTO customer = newMockCustomerDTO();
        when(customerservice.findCustomerById(any(Long.class))).thenReturn(customer);

        mockMvc.perform(get("/customer/by-id={id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(customer.getId().intValue())));
    }

    @Test
    public void testFindCustomerByFirstOrLastName() throws Exception {
        List<CustomerDTO> customers = new ArrayList<>();
        customers.add(newMockCustomerDTO());
        when(customerservice.findCustomersByFirstOrLastName(any(String.class))).thenReturn(customers);

        mockMvc.perform(get("/customer/by-name={name}", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testAddCustomerOk() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        CustomerDTO customerDTO = newMockCustomerDTO();
        when(customerservice.addCustomer(any(CustomerDTOAdd.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/customer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTOAdd)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(customerDTO.getId().intValue())));
    }

    @Test
    public void testAddCustomerEmailAlreadyRegistered() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        when(customerservice.emailAlreadyRegistered(any(String.class))).thenReturn(true);

        validateAndExpectBadRequest("There already is a customer with this email in the database.", customerDTOAdd);
    }

    @Test
    public void testAddCustomerFirstNameNullOrEmpty() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        customerDTOAdd.setFirstName(null);

        validateAndExpectBadRequest("Customer first name cannot be empty.", customerDTOAdd);

        customerDTOAdd.setFirstName("");

        validateAndExpectBadRequest("Customer first name cannot be empty.", customerDTOAdd);
    }

    @Test
    public void testAddCustomerLastNullOrEmpty() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        customerDTOAdd.setLastName(null);

        validateAndExpectBadRequest("Customer last name cannot be empty.", customerDTOAdd);

        customerDTOAdd.setLastName("");

        validateAndExpectBadRequest("Customer last name cannot be empty.", customerDTOAdd);
    }

    @Test
    public void testAddCustomerEmailNullOrEmpty() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        customerDTOAdd.setEmail(null);

        validateAndExpectBadRequest("Customer email cannot be empty.", customerDTOAdd);

        customerDTOAdd.setEmail("");

        validateAndExpectBadRequest("Customer email cannot be empty.", customerDTOAdd);
    }

    @Test
    public void testAddCustomerEmailInvalid() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        customerDTOAdd.setEmail("invalidemail");

        validateAndExpectBadRequest("Customer email is invalid.", customerDTOAdd);
    }

    @Test
    public void testUpdateAddWithException() throws Exception {
        CustomerDTOAdd customerDTOAdd = newMockCustomerDTOAdd();
        when(customerservice.customerExists(any(Long.class))).thenReturn(false);
        when(customerservice.addCustomer(any(CustomerDTOAdd.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/customer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTOAdd)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteCustomerOk() throws Exception {
        mockMvc.perform(delete("/customer/delete={id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCustomerWithException() throws Exception {
        doThrow(new RuntimeException()).when(customerservice).deleteCustomer(any(Long.class));

        mockMvc.perform(delete("/customer/delete={id}", 1L))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateCustomerOk() throws Exception {
        CustomerDTO customerDTO = newMockCustomerDTO();
        when(customerservice.customerExists(any(Long.class))).thenReturn(true);
        when(customerservice.updateCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(customerDTO.getId().intValue())));
    }

    @Test
    public void testUpdateCustomerWithException() throws Exception {
        CustomerDTO customerDTO = newMockCustomerDTO();
        when(customerservice.customerExists(any(Long.class))).thenReturn(true);
        when(customerservice.updateCustomer(any(CustomerDTO.class))).thenThrow(new RuntimeException());

        mockMvc.perform(post("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateCustomerDoesNotExist() throws Exception {
        CustomerDTO customerDTO = newMockCustomerDTO();
        when(customerservice.customerExists(any(Long.class))).thenReturn(false);

        mockMvc.perform(post("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.is("Customer not found.")));
    }

    @Test
    public void testUpdateCustomerIdNullOrZero() throws Exception {
        CustomerDTO customerDTO = newMockCustomerDTO();
        customerDTO.setId(null);

        validateAndExpectBadRequest("Customer ID must be greater than zero.", customerDTO);

        customerDTO.setId(0L);

        validateAndExpectBadRequest("Customer ID must be greater than zero.", customerDTO);
    }

    @Test
    public void testUpdateCustomerDoesntNotExist() throws Exception {
        CustomerDTO customerDTO = newMockCustomerDTO();
        when(customerservice.customerExists(any(Long.class))).thenReturn(false);

        mockMvc.perform(post("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", Matchers.is("Customer not found.")));
    }

    private void validateAndExpectBadRequest(String message, CustomerDTO customerDTO) throws Exception {
        mockMvc.perform(post("/customer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is(message)));
    }

    private void validateAndExpectBadRequest(String message, CustomerDTOAdd customerDTOAdd) throws Exception {
        mockMvc.perform(post("/customer/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerDTOAdd)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", Matchers.is(message)));
    }

    private CustomerDTO newMockCustomerDTO() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setEmail("johndoe@email.com");
        return customerDTO;
    }

    private CustomerDTOAdd newMockCustomerDTOAdd() {
        CustomerDTOAdd customerDTOAdd = new CustomerDTOAdd();
        customerDTOAdd.setFirstName("John");
        customerDTOAdd.setLastName("Doe");
        customerDTOAdd.setEmail("johndoe@email.com");
        return customerDTOAdd;
    }
}
