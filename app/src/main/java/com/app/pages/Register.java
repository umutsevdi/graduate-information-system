package com.app.pages;

import com.app.model.User;
import com.app.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.sql.Date;
import java.util.List;

@Route("register")
@PageTitle("Register | Graduate Information System")
public class Register extends VerticalLayout {

    public Register(UserService userService) {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        Image logo = new Image("https://www.yildiz.edu.tr/images/files/ytulogopng.png", "Logo");
        logo.setMinHeight(64, Unit.PIXELS);

        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        EmailField emailAddress = new EmailField("Mail Address");
        TextField phone = new TextField("Phone Number");
        phone.setPlaceholder("(___) _______");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        DatePicker dob = new DatePicker("Date of Birth");
        ComboBox<String> gender = new ComboBox<>();
        gender.setItems(List.of("Male", "Female", "Other"));
        TextArea aboutData = new TextArea("About");
        aboutData.setPlaceholder("Tell us about yourself");

        ComboBox<String> profession = new ComboBox<>();
        profession.setItems(userService.getFaculties());
        DatePicker gYear = new DatePicker("Date of Graduation");
        TextField company = new TextField();
        TextField profileURL = new TextField("Profile Picture");
        profileURL.setAutocomplete(Autocomplete.URL);

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstName, lastName,
                emailAddress, phone,
                password, confirmPassword,
                dob, gender,
                profession, gYear,
                company, aboutData,
                profileURL
        );
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );
// Stretch the username field over 2 columns
        formLayout.setColspan(aboutData, 2);
        formLayout.setColspan(company, 2);
        formLayout.setColspan(profession, 2);
        Button login = new Button(
                "Login", click -> UI.getCurrent().navigate(LoginView.class));

        login.setIcon(VaadinIcon.SIGN_IN.create());
        login.setAutofocus(true);
        Button register = new Button("Register", click -> {
            User updatedUser = new User(
                    firstName.getValue(),
                    lastName.getValue(),
                    emailAddress.getValue(),
                    phone.getValue(),
                    password.getValue(),
                    gender.getValue(),
                    Date.valueOf(dob.getValue()),
                    profession.getValue(),
                    Date.valueOf(gYear.getValue()),
                    company.getValue(),
                    aboutData.getValue(),
                    profileURL.getValue()
            );
            try {
                userService.createUser(updatedUser);
                UI.getCurrent().navigate(LoginView.class);
            } catch (Exception e) {
                Notification.show(e.getMessage());
            }
        });
        register.setIcon(VaadinIcon.USER.create());
        register.addClickShortcut(Key.ENTER);

        add(new H2("Graduate Information System"), logo, formLayout, new HorizontalLayout(
                register, login));


    }
}