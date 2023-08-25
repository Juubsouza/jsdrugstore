package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.AddressDTO;
import com.juubsouza.jsdrugstore.model.dto.AddressDTOAdd;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.service.AddressService;
import com.juubsouza.jsdrugstore.utils.ErrorResponse;
import com.juubsouza.jsdrugstore.utils.ValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@Tag(name = "Address", description = "API operations related to addresses")
public class AddressController {
    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/all-for-customer={customerId}")
    @Operation(summary = "Find all addresses for a customer", description = "Returns a list of addresses for a customer")
    public List<AddressDTO> findAllAddressesByCustomerId(@Parameter(description = "Customer ID") @PathVariable Long customerId) {
        return addressService.findAllAddressesByCustomerId(customerId);
    }

    @PostMapping("/activate-address-as-shipping={id}")
    @Operation(summary = "Activates an address to be the shipping address", description = "All other addresses for this customer will be deactivated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address activated successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> activateAddressAsShipping(@Parameter(description = "Address ID") @PathVariable Long id) {
        try {
            addressService.setAddressAsShippingTrue(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new address for a customer", description = "Adds a new address to a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> add(@Parameter @RequestBody AddressDTOAdd addressDTOADD) {
        ValidationResponse validationResponse = validateFields(addressDTOADD.getDetails(),
                addressDTOADD.getCity(), addressDTOADD.getState(), addressDTOADD.getCountry(), addressDTOADD.getCustomerId(),
                true, null);

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(new ErrorResponse(validationResponse.getStatus(), validationResponse.getMessage()));

        try {
            AddressDTO addedAddress = addressService.addAddress(addressDTOADD);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    @Operation(summary = "Update an address", description = "Updates an address in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateAddress(@Parameter @RequestBody AddressDTO addressDTO) {
        ValidationResponse validationResponse = validateFields(addressDTO.getDetails(),
                addressDTO.getCity(), addressDTO.getState(), addressDTO.getCountry(), 0L,
                false, addressDTO.getId());

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(new ErrorResponse(validationResponse.getStatus(), validationResponse.getMessage()));

        try {
            AddressDTO updatedAddress = addressService.updateAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete={id}")
    @Operation(summary = "Delete a Address", description = "Deletes a Address from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteAddress(@Parameter(description = "Address ID") @PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ValidationResponse validateFields(String details, String city, String state, String country, Long customerId, boolean isCreating, Long id) {
        if (isCreating) {
            if (customerId == null || customerId <= 0)
                return new ValidationResponse("Customer ID must be greater than zero.", HttpStatus.BAD_REQUEST);
        } else {
            if (id == null || id <= 0)
                return new ValidationResponse("Address ID must be greater than zero.", HttpStatus.BAD_REQUEST);

            if (!addressService.addressExists(id))
                return new ValidationResponse("Address not found.", HttpStatus.NOT_FOUND);
        }

        if (details == null || details.isEmpty())
            return new ValidationResponse("Address details cannot be empty.", HttpStatus.BAD_REQUEST);

        if (city == null || city.isEmpty())
            return new ValidationResponse("City cannot be empty.", HttpStatus.BAD_REQUEST);

        if (state == null || state.isEmpty())
            return new ValidationResponse("State cannot be empty.", HttpStatus.BAD_REQUEST);

        if (country == null || country.isEmpty())
            return new ValidationResponse("Country cannot be empty.", HttpStatus.BAD_REQUEST);

        return new ValidationResponse("Valid.", HttpStatus.OK);
    }
}
