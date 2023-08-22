package com.juubsouza.jsdrugstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressDTOAdd", description = "DTO for adding a new address to a customer.")
public class AddressDTOAdd {
    private String details;

    private String city;

    private String state;

    private String country;

    @JsonProperty("isShipping")
    private boolean isShipping = false;

    private Long customerId;
}
