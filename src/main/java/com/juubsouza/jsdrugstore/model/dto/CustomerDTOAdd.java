package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CustomerDTOAdd", description = "DTO for adding a customer")
public class CustomerDTOAdd {
    private String firstName;
    private String lastName;
    private String email;
}
