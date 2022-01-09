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

    public User getUser(String mail) throws Exception {
        Statement statement = db.getInstance().createStatement();
        System.out.println(mail);
        ResultSet result = statement.executeQuery(
                "SELECT * FROM \"user\" WHERE \"user\".mail='" + mail + "'");
        //"SELECT * FROM \"user\" WHERE \"user\".mail='" + mail + "'");
        result.next();
        System.out.println(result.getString("f_name"));

        System.out.println(result.getInt("id"));
        System.out.println(result.getString("f_name"));
        System.out.println(result.getString("l_name"));
        System.out.println(result.getString("mail"));
        System.out.println(result.getString("phone"));
        System.out.println(result.getString("gender"));
        System.out.println(result.getDate("dob"));
        System.out.println(result.getTimestamp("created_at"));
        System.out.println(result.getString("profession"));
        System.out.println(result.getDate("g_year"));
        System.out.println(result.getString("company"));
        System.out.println(result.getBoolean("open2work"));
        System.out.println(result.getString("about"));
        System.out.println(result.getString("image_path"));
        System.out.println(result.getString("cv_path"));
        log.info(
                result.getString("f_name"));
        /*
        System.out.println(
                result.getInt("id") );
                        result.getString("f_name") +
                        result.getString("l_name") +
                        result.getString("mail") +
                        result.getString("phone") + null +
                        result.getString("gender") +
                        result.getDate("dob") +
                        result.getTimestamp("created_at") +
                        result.getString("profession") +
                        result.getDate("g_year") +
                        result.getString("company") +
                        result.getBoolean("open2work") +
                        result.getString("about") +
                        result.getString("image_path") +
                        result.getString("cv_path")
        );
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

         */
        return new User();
    }

}
