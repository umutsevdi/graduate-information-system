package com.app.handlers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class HomeController {

    @GetMapping
    public ResponseEntity<String> index() {
        return new ResponseEntity<>("ytu_grad_api.v0.1", HttpStatus.OK);
    }


}
