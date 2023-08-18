package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Customer;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTO;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTOAdd;
import com.juubsouza.jsdrugstore.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerDTO addCustomer(CustomerDTOAdd customerDTOAdd) {
        Customer customer = new Customer();

        customer.setFirstName(customerDTOAdd.getFirstName());
        customer.setLastName(customerDTOAdd.getLastName());
        customer.setEmail(customerDTOAdd.getEmail());

        customerRepository.save(customer);

        return new CustomerDTO(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }

    public void deleteCustomer(Long id) throws EntityNotFoundException {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        customerRepository.deleteById(existingCustomer.getId());
    }

    public List<CustomerDTO> findAllCustomers() {
        return customerRepository.findAllDTOs();
    }

    public List<CustomerDTO> findCustomersByFirstOrLastName(String name) {
        return customerRepository.findDTOsByFirstOrLastName(name);
    }

    public CustomerDTO findCustomerById(Long id) {
        return customerRepository.findDTOById(id).orElse(null);
    }

    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws EntityNotFoundException {
        Customer existingCustomer = customerRepository.findById(customerDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        existingCustomer.setFirstName(customerDTO.getFirstName());
        existingCustomer.setLastName(customerDTO.getLastName());
        existingCustomer.setEmail(customerDTO.getEmail());

        customerRepository.save(existingCustomer);

        return customerDTO;
    }

    public boolean emailAlreadyRegistered(String email) {
        return customerRepository.existsByEmail(email);
    }

    public boolean customerExists(Long id) {
        return customerRepository.existsById(id);
    }
}
