package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.CustomerDTO;
import com.juubsouza.jsdrugstore.model.dto.CustomerDTOAdd;
import com.juubsouza.jsdrugstore.service.CustomerService;
import com.juubsouza.jsdrugstore.utils.EmailValidator;
import com.juubsouza.jsdrugstore.utils.ValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "API operations related to customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all customers", description = "Returns a list of customers")
    public List<CustomerDTO> findAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/by-id={id}")
    @Operation(summary = "Find customer by id", description = "Returns a customer matching the provided id, if it exists")
    public CustomerDTO findProductById(@Parameter(description = "Customer ID") @PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    @GetMapping("/by-name={name}")
    @Operation(summary = "Find customer by first or last name", description = "Returns a list of customers matching the provided name, if any exist")
    public List<CustomerDTO> findProductByFirstOrLastName(@Parameter(description = "Customer first or last name") @PathVariable String name) {
        return customerService.findCustomersByFirstOrLastName(name);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new customer", description = "Adds a new customer to the database")
    public ResponseEntity<?> addCustomer(@Parameter @RequestBody CustomerDTOAdd customerDTOAdd) {
        ValidationResponse validationResponse = validateFields(customerDTOAdd.getFirstName(),
                customerDTOAdd.getLastName(), customerDTOAdd.getEmail(), true, null);

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(validationResponse.getMessage());

        try {
            CustomerDTO addedCustomer = customerService.addCustomer(customerDTOAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    @Operation(summary = "Update a customer", description = "Updates a customer in the database")
    public ResponseEntity<?> updateCustomer(@Parameter @RequestBody CustomerDTO customerDTO) {
        ValidationResponse validationResponse = validateFields(customerDTO.getFirstName(),
                customerDTO.getLastName(), customerDTO.getEmail(), false, customerDTO.getId());

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(validationResponse.getMessage());

        try {
            CustomerDTO updatedCustomer = customerService.updateCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete={id}")
    @Operation(summary = "Delete a customer", description = "Deletes a customer from the database")
    public ResponseEntity<?> deleteCustomer(@Parameter(description = "Customer ID") @PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ValidationResponse validateFields(String firstName, String lastName, String email, boolean isCreating, Long id) {
        if (isCreating) {
            if (customerService.emailAlreadyRegistered(email))
                return new ValidationResponse("There already is a customer with this email in the database.", HttpStatus.BAD_REQUEST);
        } else {
            if (id == null || id <= 0)
                return new ValidationResponse("Customer ID must be greater than zero.", HttpStatus.BAD_REQUEST);

            if (!customerService.customerExists(id))
                return new ValidationResponse("Customer not found.", HttpStatus.NOT_FOUND);
        }

        if (firstName == null || firstName.isEmpty())
            return new ValidationResponse("Customer first name cannot be empty.", HttpStatus.BAD_REQUEST);

        if (lastName == null || lastName.isEmpty())
            return new ValidationResponse("Customer last name cannot be empty.", HttpStatus.BAD_REQUEST);

        if (email == null || email.isEmpty())
            return new ValidationResponse("Customer email cannot be empty.", HttpStatus.BAD_REQUEST);

        if (!EmailValidator.isValid(email))
            return new ValidationResponse("Customer email is invalid.", HttpStatus.BAD_REQUEST);

        return new ValidationResponse("Valid", HttpStatus.OK);
    }
}
