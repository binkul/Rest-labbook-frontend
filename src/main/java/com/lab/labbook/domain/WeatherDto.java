package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {
    private String city_name;
    private String pres;
    private String wind_spd;
    private String rh;
    private String temp;
    private String clouds;
    private String icon;
    private String description;
}
