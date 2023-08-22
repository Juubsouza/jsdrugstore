package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressDTO", description = "DTO for address retrieval.")
public class AddressDTO {
    private Long id;

    private String details;

    private String city;

    private String state;

    private String country;

    private boolean isShipping;
}
