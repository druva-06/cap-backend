package com.meritcap.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiSuccessResponse<T>{
    private T response;
    private String message;
    private Integer statusCode;
    private final Boolean success = true;
}
