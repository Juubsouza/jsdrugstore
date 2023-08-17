package com.juubsouza.jsdrugstore.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String manufacturer;
    private Double price;
    private Integer stock;
}
