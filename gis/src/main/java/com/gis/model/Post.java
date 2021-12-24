package com.gis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    private String id;
    private String authorId;
    private String title;
    private String context;
    private Integer like;
}
