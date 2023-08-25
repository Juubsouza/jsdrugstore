package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SaleDTO", description = "DTO for sale retrieval.")
public class SaleDTO {

    private Long id;

    private String paymentMethod = "CASH";

    private String paymentStatus = "PENDING";

    private String shippingStatus = "PENDING";

    private List<SaleProductDTO> saleProducts;

    private BigDecimal total;

    private Long customerId;

    private Long sellerId;

    public SaleDTO(Long id, String paymentMethod, String paymentStatus, String shippingStatus, BigDecimal total, Long customerId, Long sellerId) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.shippingStatus = shippingStatus;
        this.total = total;
        this.customerId = customerId;
        this.sellerId = sellerId;
    }
}
