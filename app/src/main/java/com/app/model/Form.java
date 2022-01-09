package com.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Form {
    private String id;
    private String from;
    private String to;
    private Timestamp createdAt;
}

/* Corresponding Class to

CREATE TABLE "form"
(
    "id"         SERIAL PRIMARY KEY,
    "from"       int NOT NULL,
    "to"         int NOT NULL,
    "created_at" timestamp DEFAULT clock_timestamp() -- automatically initialized

);
 */