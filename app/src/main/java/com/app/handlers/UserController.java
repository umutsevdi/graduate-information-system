package com.app.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import egaranti.webapp.config.security.PasswordEncoder;
import egaranti.webapp.model.Blog;
import egaranti.webapp.model.User;
import egaranti.webapp.model.request.EditUserRequest;
import egaranti.webapp.model.response.ShortUserResponse;
import egaranti.webapp.model.response.SuccessMessage;
import egaranti.webapp.service.AuthorizationService;
import egaranti.webapp.service.BlogService;
import egaranti.webapp.service.FileService;
import egaranti.webapp.service.FileService.directories;
import egaranti.webapp.service.UnauthorizedException;
import egaranti.webapp.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final BlogService blogService;
    private final FileService fileService;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(HttpServletRequest request, @PathVariable("id") String id) throws Exception {

        User user = userService.findById(id);
        user.setPassword(null);
        if (authorizationService.retriveUserFromHttpRequest(request).getId().equals(id)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ShortUserResponse(user.getId(), user.getName(), user.getMail(), user.getPhone()),
                    HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HashMap<String, List<String>>> put(HttpServletRequest request, @PathVariable("id") String id,
                                                             @RequestBody EditUserRequest context) throws Exception {
        List<String> changedValues = new LinkedList<>();
        User user = authorizationService.checkSelf(request, id);

        System.out.println(user.getPassword() + "\n" + context.getPassword() + "\n"
                + passwordEncoder.bCryptPasswordEncoder().matches(context.getPassword(), user.getPassword()));
        if (!passwordEncoder.bCryptPasswordEncoder().matches(context.getPassword(), user.getPassword())) {
            throw new UnauthorizedException();
        }

        if (context.getNewPassword() != null && !context.getNewPassword().isBlank()) {
            if (context.getNewPassword().length() > 9
                    && !passwordEncoder.bCryptPasswordEncoder().matches(context.getNewPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(context.getNewPassword()));
                changedValues.add("password");
            } else
                throw new Exception("InvalidNewPasswordException");

        }
        if (context.getPhone() != null) {
            if (!(context.getPhone().length() > 9))
                throw new Exception("InvalidNewPhoneException");
            try {
                userService.findByPhone(context.getPhone());
            } catch (Exception e) {
                user.setPhone(context.getPhone());
                changedValues.add("phone");
            }
        }
        // TODO Send Update Mail
        if (context.getMail() != null) {
            if (!validate(context.getMail()) || context.getMail().equals(user.getMail()))
                throw new Exception("InvalidNewMailException");
            try {
                userService.findByMail(context.getMail());
            } catch (Exception e) {
                user.setMail(context.getMail());
                changedValues.add("mail");
            }
        }

        if (context.getDateOfBirth() != null) {
            if (context.getDateOfBirth().equals(user.getDateOfBirth())
                    || LocalDate.now().getYear() - context.getDateOfBirth().getYear() < 18)
                throw new Exception("InvalidDateOfBirthException");
            user.setDateOfBirth(context.getDateOfBirth());
            changedValues.add("dob");
        }

        if (context.getAddress() != null) {
            if (context.getAddress().isBlank() || context.getAddress().equals(user.getAddress()))
                throw new Exception("InvalidAddressException");
            user.setAddress(context.getAddress());
            changedValues.add("address");
        }

        if (context.getJob() != null) {
            if (context.getJob().isBlank() || context.getJob().equals(user.getJob()))
                throw new Exception("InvalidJobException");
            user.setJob(context.getJob());
            changedValues.add("job");
        }

        userService.save(user);
        user.setPassword(null);
        HashMap<String, List<String>> response = new HashMap<>();
        response.put("changed", changedValues);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Allows user to display an image
    @GetMapping(value = "/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> download(@PathVariable("id") String id) throws Exception {
        return new ResponseEntity<>(fileService.loadFile(directories.users, id, "profile"), HttpStatus.OK);
    }

    // Allows user to upload an image
    @PostMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<SuccessMessage> upload(HttpServletRequest request, @PathVariable("id") String id,
                                                 @RequestParam("file") MultipartFile file) throws Exception {
        System.out.println("Post Image");
        User user = authorizationService.checkSelf(request, id); // CHECKS USER AUTHORITIES

        String filePath = fileService.saveFile(file, List.of("png", "jpg", "jpeg"), directories.users, id, "profile");
        return new ResponseEntity<>(new SuccessMessage(filePath, file.getName(), user.getId()), HttpStatus.OK);
    }

    // Users in the whitelist can block or unblock users
    @PutMapping("/{id}/manage")
    public ResponseEntity<SuccessMessage> manageUser(HttpServletRequest request, @PathVariable("id") String id,
                                                     @RequestParam("status") Boolean status) throws Exception {
        authorizationService.checkAdmins(request);
        User user = userService.findById(id);
        user.setEnabled(status);
        userService.save(user);

        return new ResponseEntity<>(
                new SuccessMessage("status=" + status.toString(), request.getServletPath(), user.getId()// authorizationService
                        // return

                ), HttpStatus.OK);
    }

    @PostMapping("/blog")
    public ResponseEntity<Blog> createBlog(HttpServletRequest request, @RequestBody Blog context) throws Exception {
        User user = authorizationService.checkAdmins(request);
        context.setIsPublished(false);
        context.setAuthor(user.getName());
        context.setLastEdited(LocalDateTime.now());

        return new ResponseEntity<>(blogService.create(context), HttpStatus.OK);
    }

    @PutMapping("/blog/{id}")
    public ResponseEntity<Blog> editBlog(HttpServletRequest request, @PathVariable("id") String id,
                                         @RequestBody Blog context) throws Exception {
        authorizationService.checkAdmins(request);
        Blog blog = blogService.findById(id);
        blog.setContent(context.getContent());
        blog.setTitle(context.getTitle());
        blog.setIsPublished(context.getIsPublished());
        blog.setLastEdited(LocalDateTime.now());

        return new ResponseEntity<>(blogService.save(blog), HttpStatus.OK);
    }

    @DeleteMapping("/blog/{id}")
    public ResponseEntity<String> deleteBlog(HttpServletRequest request, @PathVariable("id") String id)
            throws Exception {
        authorizationService.checkAdmins(request);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
