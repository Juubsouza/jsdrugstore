package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductDTO", description = "DTO for product retrieval")
public class ProductDTO {
    private Long id;
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private Integer stock;
}
