package com.app.config;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;

// This component will be initialized
@Configuration
@Getter
@ToString
@Log4j2
public class DBConfig {
    @Value("${spring.datasource.url}")
    private String url;
    // = "jdbc:postgresql://localhost:5432/grad";
    @Value("${spring.datasource.username}")
    private String username;
    // = "postgres";
    @Value("${spring.datasource.password}")
    private String password;
    // = "password";
    private Connection connection;

    public DBConfig() {
        System.out.println("@init::dbInstance");
    }

    @PostConstruct
    public void init() throws Exception {

        System.out.println("DBInstance was constructed");
        connection = DriverManager.getConnection(url, username, password);
        DBConfig.log.info(
                "connecting to " + url + " using :{\n'username' : " + username + ",\n'password' : " + password + "\n}");
        DBConfig.log.info(connection.toString() + " started...");
        DBConfig.log.debug("username=" + username);
        DBConfig.log.info("Connection Successful");
    }

}
