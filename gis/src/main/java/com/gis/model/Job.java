package com.gis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Job {
    int id;
    private String ownerId;
    private String title;
    private String content;
    private LocalDateTime registerDate;

}
