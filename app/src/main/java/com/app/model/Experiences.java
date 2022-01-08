package com.app.model;

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
    private String owner;
    private String title;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isPresent;
}
