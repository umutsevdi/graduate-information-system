package com.app.model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    private Date dob;
    private String phone;
    private Character gender;
    private String profession;
    private Date graduationYear;
    private String company;
}
