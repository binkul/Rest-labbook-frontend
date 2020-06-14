package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDto {
    private Long id;
    private String symbol;
    private BigDecimal exchange;
    private BigDecimal commercial;
}
