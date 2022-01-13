package com.app.service;

import com.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserSession {
    private LocalDateTime time;
    private Integer id;
}
