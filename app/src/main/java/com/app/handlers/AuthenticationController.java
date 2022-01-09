package com.app.handlers;

import com.app.model.Requests.LoginRequest;
import com.app.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final BlacklistService blackListService;

    @PostMapping
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginRequest body)
            throws Exception {
        try {
            return new ResponseEntity<>(authenticationService.login(body.getMail(),body.getPassword()),HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {

        final String token = request.getHeader("Authorization").substring(7);
        System.out.println("Blacklisting : ->" + token);
        blackListService.insertJWTtoBlacklist(token);
        return new ResponseEntity<>(new SuccessMessage("BlackListingComplete", request.getContextPath(), token),
                HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessMessage> register(HttpServletRequest request, @RequestBody RegistrationRequest body)
            throws Exception {
        try {
            if (body.getName() == null || body.getMail() == null || body.getPassword() == null
                    || body.getPassword().length() < 8)
                throw new NullPointerException();
            userService.create(new User(body.getName(), body.getMail(), body.getPhone(), body.getPassword()));
            authenticationService.sendToken(body);
            return new ResponseEntity<>(new SuccessMessage("MailSent", request.getContextPath(), body.getMail()),
                    HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @GetMapping("/verify")
    public ResponseEntity<SuccessMessage> verify(HttpServletRequest request, @RequestParam("token") String token)
            throws Exception {
        try {
            String mail = authenticationService.confirmMail(token);
            return new ResponseEntity<>(new SuccessMessage("MailConfirmed", request.getContextPath(), mail),
                    HttpStatus.OK);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
