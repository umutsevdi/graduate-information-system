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
public class User {
    private String id;
    private String firstName;
    private String secondName;
    private String mail;
    private Long phone;
    private String password;
    private LocalDate dob;
    private String job;
    private String company;

}
