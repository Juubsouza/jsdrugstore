package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SaleProductDTO", description = "DTO for retrieving products from a sale.")
public class SaleProductDTO {

    private Long id;

    private Integer quantity;

    private Long productId;

    private String productName;
}
