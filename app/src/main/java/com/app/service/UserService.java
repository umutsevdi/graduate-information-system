package com.app.service;

import com.app.config.DBConfig;
import com.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Statement;

@Service
@AllArgsConstructor
@Getter
@ToString
@Log4j2
public class UserService {
    private DBConfig db;

    public User findUserById(Integer id) throws Exception {
        return executeSelectQuery("id", id);
    }

    public User findUserByMail(String mail) throws Exception {
        return executeSelectQuery("mail", mail);
    }


    public <T> User executeSelectQuery(String by, T value) throws Exception {
        Statement statement = db.getInstance().createStatement();
        System.out.println("Executing query for " + by);
        ResultSet result;
        if (value.getClass().equals(String.class)) {
            result = statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE \"user\"." + by + " = '" + value + "'");
        } else {
            result = statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE \"user\"." + by + " = " + value);
        }
        result.next();

        return new User(
                result.getInt("id"),
                result.getString("f_name"),
                result.getString("l_name"),
                result.getString("mail"),
                result.getString("phone"), null,
                result.getString("gender"),
                result.getDate("dob"),
                result.getTimestamp("created_at"),
                result.getString("profession"),
                result.getDate("g_year"),
                result.getString("company"),
                result.getBoolean("open2work"),
                result.getString("about"),
                result.getString("image_path"),
                result.getString("cv_path")
        );
    }
}
