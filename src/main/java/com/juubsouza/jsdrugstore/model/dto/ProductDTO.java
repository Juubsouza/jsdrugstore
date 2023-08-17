package com.juubsouza.jsdrugstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private Integer stock;
}
