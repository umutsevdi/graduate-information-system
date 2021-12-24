package com.gis.db;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

// This component will be initialized
@Configuration
@Getter
@ToString
@Log4j2
public class DBInstance {
    @Value("${spring.datasource.url}")
    private String url;// = "jdbc:postgresql://localhost:5432/grad";
    @Value("${spring.datasource.username}")
    private String username;// = "postgres";
    @Value("${spring.datasource.password}")
    private String password;// = "password";

    private Connection connection;

    public DBInstance() {
        System.out.println("@init::dbInstance");
    }

    @PostConstruct
    public void init() throws Exception {

        System.out.println("DBInstance was constructed");
        connection = DriverManager.getConnection(url, username, password);
        DBInstance.log.info(
                "connecting to " + url + " using :{\n'username' : " + username + ",\n'password' : " + password + "\n}");
        DBInstance.log.info(connection.toString() + " started...");
        DBInstance.log.debug("username=" + username);
        DBInstance.log.info("Tricle Üyeleri : Oğuzhan Ercan, Semih Yazıcı, Umutcan Sevdi");
    }

}
