package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SellerDTO", description = "DTO for seller retrieval.")
public class SellerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String shift;

    private Date admissionDate;
}
