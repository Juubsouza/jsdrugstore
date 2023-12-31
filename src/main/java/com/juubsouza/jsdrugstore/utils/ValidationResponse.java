package com.juubsouza.jsdrugstore.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {
    private String message;
    private HttpStatus status;
}
