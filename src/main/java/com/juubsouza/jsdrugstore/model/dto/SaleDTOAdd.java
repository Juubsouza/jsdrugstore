package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SaleDTO", description = "DTO for adding a sale.")
public class SaleDTOAdd {

    private String paymentMethod = "CASH";

    private List<SaleProductDTOAdd> saleProducts;

    private Long customerId;

    private Long sellerId;
}
