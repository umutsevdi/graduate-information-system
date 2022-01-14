package com.app.model;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Form {
    private Integer id;
    private Integer from;
    private Integer to;
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