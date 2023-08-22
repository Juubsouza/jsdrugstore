package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Customer;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTO;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTOAdd;
import com.juubsouza.jsdrugstore.repository.CustomerRepository;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private final Long CUSTOMER_ID = 1L;

    @Test
    public void testAddCustomerOk() {
        CustomerDTOAdd customerDTOAdd = MockDTOs.newMockCustomerDTOAdd();

        customerService.addCustomer(customerDTOAdd);

        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void testCustomerExists() {
        customerService.customerExists(CUSTOMER_ID);

        verify(customerRepository, times(1)).existsById(CUSTOMER_ID);
    }

    @Test
    public void testDeleteCustomerOk() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(CUSTOMER_ID);

        verify(customerRepository, times(1)).deleteById(any());
    }

    @Test
    public void testDeleteCustomerException() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(CUSTOMER_ID));
    }

    @Test
    public void testEmailAlreadyRegistered() {
        customerService.emailAlreadyRegistered("any@email.com");

        verify(customerRepository, times(1)).existsByEmail(any());
    }

    @Test
    public void testFindAllCustomersOk() {
        customerService.findAllCustomers();

        verify(customerRepository, times(1)).findAllDTOs();
    }

    @Test
    public void testFindCustomersByFirstOrLastNameOk() {
        customerService.findCustomersByFirstOrLastName("John");

        verify(customerRepository, times(1)).findDTOsByFirstOrLastName("John");
    }

    @Test
    public void testFindCustomerByIdOk() {
        customerService.findCustomerById(CUSTOMER_ID);

        verify(customerRepository, times(1)).findDTOById(CUSTOMER_ID);
    }

    @Test
    public void testUpdateCustomerOk() {
        CustomerDTO customerDTO = MockDTOs.newMockCustomerDTO();

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(new Customer()));

        customerService.updateCustomer(customerDTO);

        verify(customerRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateCustomerException() {
        CustomerDTO customerDTO = MockDTOs.newMockCustomerDTO();

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(customerDTO));
    }
}
