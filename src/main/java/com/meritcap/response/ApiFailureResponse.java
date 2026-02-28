package com.meritcap.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiFailureResponse<T> {
    private T error;
    private String message;
    private Integer statusCode;
    private final Boolean success = false;
}
