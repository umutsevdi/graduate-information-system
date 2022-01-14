package com.app.service;

import com.app.config.DBConfig;
import com.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@AllArgsConstructor
@Getter
@ToString
@Log4j2
public class UserService {
    private DBConfig db;

    public User findByName(String firstName, String secondName) {
        try {
            Statement statement = db.getInstance().createStatement();
            return constructUser(statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE f_name='" + firstName + "' AND l_name='" + secondName + "'"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserById(Integer id) {
        try {
            Statement statement = db.getInstance().createStatement();
            return constructUser(statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE id=" + id
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByMail(String mail) {
        try {
            Statement statement = db.getInstance().createStatement();
            return constructUser(statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE mail='" + mail + "'"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User constructUser(ResultSet result) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
