package com.app.model;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Announcement {
    private Integer id;
    private Integer from;
    private String title;
    private String content;
    private String link;
    private Integer like;
    private Timestamp createdAt;
    public Announcement(String title, String content,String link){
        this.title=title;
        this.content=content;
        this.link=link;
    }
}

/* Corresponding Class to

    CREATE TABLE "announcement"
        (
        "id"         SERIAL PRIMARY KEY NOT NULL,
        "from"       int                NOT NULL,
        "title"      varchar(100)       NOT NULL,
        "content"    text               NOT NULL,
        "link"       text,
        "like"       int                NOT NULL DEFAULT 0,
        "created_at" timestamp          NOT NULL DEFAULT clock_timestamp() -- automatically initialized

        );
*/