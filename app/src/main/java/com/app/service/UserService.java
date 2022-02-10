package com.app.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.app.config.DBConfig;
import com.app.model.User;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Service
@AllArgsConstructor
@Getter
@ToString
public class UserService {
    private DBConfig db;

    public User findByName(String firstName, String secondName) {
        try {
            Statement statement = db.getInstance().createStatement();
            return constructUser(statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE f_name='" + firstName + "' AND l_name='" + secondName + "'"
            )).get(0);
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
            )).get(0);
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
            )).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findByProfession(String profession) {
        try {
            Statement statement = db.getInstance().createStatement();
            return constructUser(statement.executeQuery(
                    "SELECT * FROM \"user\" WHERE profession='" + profession + "'"
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFaculty(String profession) {
        try {
            Statement statement = db.getInstance().createStatement();
            ResultSet set = statement.executeQuery(
                    "SELECT faculty_name FROM university WHERE profession_name='" + profession + "'"
            );
            set.next();
            return set.getString("faculty_name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getFaculties() {
        List<String> result = new LinkedList<>();
        try {
            Statement statement = db.getInstance().createStatement();
            ResultSet set = statement.executeQuery(
                    "SELECT profession_name FROM university"
            );
            while (set.next()) {
                result.add(set.getString("profession_name"));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(User user) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute(
                "UPDATE \"user\" set " +
                        "f_name='" + user.getFirstName() +
                        "',l_name='" + user.getSecondName() +
                        "',mail='" + user.getMail() +
                        "',phone='" + user.getPhone() +
                        "',dob='" + user.getDob() +
                        "',about='" + user.getAbout() +
                        "',image_path='" + user.getImagePath() +
                        "',cv_path='" + user.getCvPath() +
                        "' WHERE id=" + user.getId()
        );
    }

    public void createUser(User user) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute(
                "INSERT INTO \"user\"(f_name, l_name, mail, phone, password, gender, dob, profession, g_year, company, about, image_path, cv_path)  VALUES (" +
                        "'" + user.getFirstName() +
                        "','" + user.getSecondName() +
                        "','" + user.getMail() +
                        "','" + user.getPhone() +
                        "','" + user.getPassword() +
                        "','" + user.getGender() +
                        "','" + user.getDob() +
                        "','" + user.getProfession() +
                        "','" + user.getGraduationYear() +
                        "','" + user.getCompany() +
                        "','" + user.getAbout() +
                        "','" + user.getImagePath() +
                        "','" + user.getCvPath() + "')");
    }


    public List<User> constructUser(ResultSet result) {
        List<User> users = new LinkedList<>();
        try {
            while (result.next()) {
                users.add(new User(
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
                        result.getString("cv_path")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
