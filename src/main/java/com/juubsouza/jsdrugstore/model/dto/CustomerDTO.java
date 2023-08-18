package com.juubsouza.jsdrugstore.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CustomerDTO", description = "DTO for customer retrieval")
public class CustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
