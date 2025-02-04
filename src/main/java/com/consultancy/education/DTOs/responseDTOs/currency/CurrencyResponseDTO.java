package com.consultancy.education.DTOs.responseDTOs.currency;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyResponseDTO {
    String result;
    String base_code;
    Map<String, Double> conversion_rates;
}
