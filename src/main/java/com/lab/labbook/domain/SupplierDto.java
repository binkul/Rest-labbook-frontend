package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierDto {
    private Long id;
    private String name;
    private String shortName;
    private String address;
    private String phones;
    private boolean producer;
    private String comments;
}
