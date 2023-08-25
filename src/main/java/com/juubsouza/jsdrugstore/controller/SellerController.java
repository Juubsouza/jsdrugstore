package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.SellerDTO;
import com.juubsouza.jsdrugstore.model.dto.SellerDTOAdd;
import com.juubsouza.jsdrugstore.service.SellerService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seller")
@Tag(name = "Seller", description = "API operations related to sellers")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new seller", description = "Adds a new seller to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller added successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SellerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addSeller(@Parameter @RequestBody SellerDTOAdd sellerDTOAdd) {
        ValidationResponse validationResponse = validateFields(sellerDTOAdd.getFirstName(), sellerDTOAdd.getLastName(),
                sellerDTOAdd.getShift(), sellerDTOAdd.getAdmissionDate(), true, null);

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(new ErrorResponse(validationResponse.getStatus(), validationResponse.getMessage()));

        try {
            SellerDTO addedSeller = sellerService.addSeller(sellerDTOAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSeller);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Find all sellers", description = "Returns a list of sellers")
    public List<SellerDTO> findAllSellers() {
        return sellerService.findAllSellers();
    }

    @GetMapping("/by-id={id}")
    @Operation(summary = "Find seller by id", description = "Returns a seller matching the provided id, if it exists")
    public SellerDTO findSellerById(@Parameter(description = "Seller ID") @PathVariable Long id) {
        return sellerService.findSellerById(id);
    }

    @GetMapping("/by-name={name}")
    @Operation(summary = "Find sellers by first or last name", description = "Returns a list of sellers matching the provided name, if any exist")
    public List<SellerDTO> findSellersByFirstOrLastName(@Parameter(description = "Seller first or last name") @PathVariable String name) {
        return sellerService.findSellersByFirstOrLastName(name);
    }

    @PostMapping("/update")
    @Operation(summary = "Update a seller", description = "Updates a seller in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SellerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateSeller(@Parameter @RequestBody SellerDTO sellerDTO) {
        ValidationResponse validationResponse = validateFields(sellerDTO.getFirstName(),
                sellerDTO.getLastName(), sellerDTO.getShift(), sellerDTO.getAdmissionDate(), false, sellerDTO.getId());

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(new ErrorResponse(validationResponse.getStatus(), validationResponse.getMessage()));

        try {
            SellerDTO updatedSeller = sellerService.updateSeller(sellerDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedSeller);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete={id}")
    @Operation(summary = "Delete a seller", description = "Deletes a seller from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Seller deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteSeller(@Parameter(description = "Seller ID") @PathVariable Long id) {
        try {
            sellerService.deleteSeller(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ValidationResponse validateFields(String firstName, String lastName, String shift, Date admissionDate, boolean isCreating, Long id) {
        if (!isCreating) {
            if (id == null || id <= 0)
                return new ValidationResponse("Seller ID must be greater than zero.", HttpStatus.BAD_REQUEST);

            if (!sellerService.sellerExists(id))
                return new ValidationResponse("Seller not found.", HttpStatus.NOT_FOUND);
        }

        if (firstName == null || firstName.isEmpty())
            return new ValidationResponse("Seller first name cannot be empty.", HttpStatus.BAD_REQUEST);

        if (lastName == null || lastName.isEmpty())
            return new ValidationResponse("Seller last name cannot be empty.", HttpStatus.BAD_REQUEST);

        if (shift == null || shift.isEmpty())
            return new ValidationResponse("Seller must have a valid shift: 'DAY' or 'NIGHT'.", HttpStatus.BAD_REQUEST);

        if (!shift.equals("DAY") && !shift.equals("NIGHT"))
            return new ValidationResponse("Seller must have a valid shift: 'DAY' or 'NIGHT'.", HttpStatus.BAD_REQUEST);

        if (admissionDate == null)
            return new ValidationResponse("Seller admission date cannot be empty.", HttpStatus.BAD_REQUEST);

        return new ValidationResponse("Valid", HttpStatus.OK);
    }
}
