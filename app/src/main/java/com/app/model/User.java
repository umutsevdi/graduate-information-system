package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class User {
    private Integer id;
    private String firstName;
    private String secondName;
    private String mail;
    private String phone;
    private String password;
    private char gender;
    private LocalDate dob;
    private Long registered;

    private String job;
    private String company;

    private List<String> languages;
    private Boolean openToWork;
    private List<String> skills;
    private String faculty;
    private String profession;
    private int graduationYear;
    private String description;
    private String imagePath;
}
