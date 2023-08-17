package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(name = "ProductDTOAdd", description = "DTO for adding a product")
public class ProductDTOAdd {
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private Integer stock;
}