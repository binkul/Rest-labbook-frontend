package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal voc;
    private Long currencyId;
    private String symbol;
    private Long supplierId;
    private Map<String, String> symbols;
    private CurrencyDto currency;
    private SupplierDto supplier;
}
