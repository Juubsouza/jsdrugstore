package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.SaleDTO;
import com.juubsouza.jsdrugstore.model.dto.SaleDTOAdd;
import com.juubsouza.jsdrugstore.model.dto.SaleProductDTOAdd;
import com.juubsouza.jsdrugstore.service.SaleService;
import com.juubsouza.jsdrugstore.utils.ValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sale")
@Tag(name = "Sale", description = "API operations related to sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new sale", description = "Adds a new sale to the database")
    public ResponseEntity<?> addSale(@Parameter @RequestBody SaleDTOAdd saleDTOAdd) {
        ValidationResponse validationResponse = validateFields(saleDTOAdd.getPaymentMethod(), saleDTOAdd.getSaleProducts(),
                saleDTOAdd.getCustomerId(), saleDTOAdd.getSellerId());

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(validationResponse.getMessage());

        try {
            SaleDTO addedSale = saleService.addSale(saleDTOAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSale);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ValidationResponse validateFields(String paymentMethod, List<SaleProductDTOAdd> saleProducts, Long customerId, Long sellerId) {
        if (saleProducts == null || saleProducts.isEmpty())
            return new ValidationResponse("Sale must have at least one product.", HttpStatus.BAD_REQUEST);

        if (paymentMethod == null || paymentMethod.isEmpty())
            return new ValidationResponse("Sale must have a payment method.", HttpStatus.BAD_REQUEST);

        if (customerId == null || customerId <= 0)
            return new ValidationResponse("Sale must have a customer.", HttpStatus.BAD_REQUEST);

        if (sellerId == null || sellerId <= 0)
            return new ValidationResponse("Sale must have a seller.", HttpStatus.BAD_REQUEST);

        Set<Long> seenProductIds = new HashSet<>();

        for (SaleProductDTOAdd saleProductDTOAdd : saleProducts) {
            Long productId = saleProductDTOAdd.getProductId();
            if (productId == null)
                return new ValidationResponse("Sale product must have an ID.", HttpStatus.BAD_REQUEST);

            if (seenProductIds.contains(productId))
                return new ValidationResponse("Sale product cannot be added more than once.", HttpStatus.BAD_REQUEST);

            seenProductIds.add(productId);

            if (saleProductDTOAdd.getQuantity() == null || saleProductDTOAdd.getQuantity() <= 0)
                return new ValidationResponse("Sale product quantity must be greater than 0.", HttpStatus.BAD_REQUEST);
        }

        return new ValidationResponse("Valid", HttpStatus.OK);
    }
}
