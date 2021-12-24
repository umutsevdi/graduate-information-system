package com.gis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Experiences {
    private String id;
    private String userId;
    private String company;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isPresent;

}
