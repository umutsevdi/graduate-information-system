package com.app.service;

import com.app.config.DBConfig;
import com.app.model.Requests.RegisterRequest;
import com.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@AllArgsConstructor
@Getter
public class AuthenticationService {
    private HashMap<String, UserSession> sessions;
    private DBConfig db;
    private UserService userService;

    public void register(RegisterRequest request) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.executeQuery(String.format(
                """
                                insert into "user" (f_name, l_name, mail, phone, password, gender, dob, profession, g_year, company)"+
                                 "values ('%s', '%s', '%s', '%s', '%s', '%c', '%s','%s','%s','%s')
                        """,
                request.getFirstName(),
                request.getLastName(),
                request.getMail(),
                request.getPhone(),
                request.getPassword(),
                request.getGender(),
                request.getDob(),
                request.getProfession(),
                request.getGraduationYear(),
                request.getCompany()
        ));
        System.out.println(request);
    }

    public String login(String mail, String password) throws Exception {

        Statement statement = db.getInstance().createStatement();
        ResultSet result = statement.executeQuery(
                "SELECT * FROM \"user\" WHERE \"user\".mail='" + mail + "'");
        while (result.next()) {
            if (result.getString("password").equals(password)) {
                String token = java.util.UUID.randomUUID().toString();
                UserSession session = new UserSession(
                        LocalDateTime.now(),
                        userService.findUserByMail(mail).getId()
                );
                sessions.put(token, session);
                System.out.println("@createSession : " + session);
                return token;
            }
        }
        return null;
    }

    public Boolean validate(String token) {
        if (sessions.get(token) != null) {
            if (sessions.get(token).getTime().plusDays(1).isBefore(LocalDateTime.now())) {
                sessions.remove(token);
                return false;
            }
            return true;
        }
        return false;
    }

    public User validateUser(String token) throws Exception {
        if (!validate(token)) {
            throw new Exception("InvalidToken");
        }
        return userService.findUserById(sessions.get(token).getId());
    }

}

