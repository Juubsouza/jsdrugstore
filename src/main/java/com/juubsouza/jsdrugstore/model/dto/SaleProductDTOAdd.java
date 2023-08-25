package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SaleProductDTOAdd", description = "DTO for adding a products to a sale.")
public class SaleProductDTOAdd {

    private Integer quantity;

    private Long productId;
}
