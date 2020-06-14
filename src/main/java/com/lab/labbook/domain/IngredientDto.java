package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientDto {
    private Long id;
    private Integer ordinal;
    private BigDecimal amount;
    private String comment;
    private Long labId;
    private Long materialId;
    private MaterialDto material;
}
