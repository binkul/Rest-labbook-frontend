package com.lab.labbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LabBookDto {
    private Long id;
    private String title;
    private String description;
    private String conclusion;
    private BigDecimal density;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String status;
    private Long userId;
    private Long seriesId;
    private SeriesDto seriesDto;
}
