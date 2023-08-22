package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SellerDTOAdd", description = "DTO for adding a new seller.")
public class SellerDTOAdd {
    private String firstName;

    private String lastName;

    private String shift;

    private Date admissionDate;
}