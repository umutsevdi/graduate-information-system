package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

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
    private String gender;
    private Date dob;
    private Timestamp createdAt;

    private String profession;
    private Date graduationYear;
    private String company;

    private Boolean openToWork;
    private String about;
    private String imagePath;
    private String cvPath;

}
/*
Corresponding class to
CREATE TABLE "user"
(
    "id"         SERIAL PRIMARY KEY NOT NULL,
    "f_name"     varchar(30)        NOT NULL,
    "l_name"     varchar(30)        NOT NULL,
    "mail"       varchar(60) UNIQUE NOT NULL,
    "phone"      varchar(15) UNIQUE NOT NULL,
    "password"   varchar(30)        NOT NULL,
    "gender"     char(1)            NOT NULL,         -- enum : male female other
    "dob"        date               NOT NULL,
    "created_at" timestamp DEFAULT clock_timestamp(), -- automatically initialized
    -- "faculty" varchar(40) NOT NULL,
    "profession" varchar(100)       NOT NULL,
    "g_year"     date               NOT NULL,

    "company"    varchar(100),
    "open2work"  boolean NOT NULL DEFAULT false,
    "about"      text,
    "image_path" varchar(100),
    "cv_path"    varchar(100)
);
 */