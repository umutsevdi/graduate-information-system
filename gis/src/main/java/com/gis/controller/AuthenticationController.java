package com.gis.controller;

import java.sql.ResultSet;
import java.sql.Statement;

import com.gis.db.DBInstance;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private DBInstance db;

    @GetMapping("/login")
    public ResponseEntity<String> login() throws Exception {
        String query = "SELECT * from user";
        Statement s = db.getConnection().createStatement();
        ResultSet r = s.executeQuery(query);
        return new ResponseEntity<>(r.toString(), HttpStatus.OK);

    }
}
