package com.app.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Job {
    Integer id;
    private Integer from;
    private String title;
    private String content;
    private Timestamp createdAt;

    public Job(Integer from, String title, String content) {
        this.from = from;
        this.title = title;
        this.content = content;
    }
}
/* Corresponding Class to
    CREATE TABLE "job_ad"
        (
        "id"         SERIAL PRIMARY KEY NOT NULL,
        "from"       int                NOT NULL,
        "title"      varchar(100)       NOT NULL,
        "content"    text               NOT NULL,
        "created_at" timestamp DEFAULT clock_timestamp() -- automatically initialized
        );
 */
