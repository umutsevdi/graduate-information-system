package com.app.pages;

import com.app.service.AuthenticationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

@Route("login")
@PageTitle("Login | Graduate Information System")
public class LoginView extends VerticalLayout {
    AuthenticationService authenticationService;
    private int maxWidth;

    public LoginView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Image logo = new Image("https://dx5i3n065oxey.cloudfront.net/platform/9531/assets/logo.png", "Logo");
        logo.setMaxHeight(256, Unit.PIXELS);
        TextField mail = new TextField("Email Address");
        PasswordField password = new PasswordField("Password");
        Button login = new Button(
                "Login",
                click -> {
                    try {
                        if (mail.getValue().isBlank() || password.getValue().isBlank()) {
                            Notification.show("Missing email address or password");
                        } else {
                            String token = authenticationService.login(mail.getValue(), password.getValue());
                            if (token != null) {
                                Notification.show(token);
                                Notification.show("Login Successful, redirecting...");
                                this.getUI().ifPresent(
                                        ui -> ui.navigate(MainPage.class,token));
                            } else {
                                Notification.show("Wrong email address or password. Please try again");
                            }
                        }


                    } catch (Exception e) {
                        Notification.show(e.getMessage());
                    }
                }
        );
        login.addClickShortcut(Key.ENTER);
        login.setIcon(VaadinIcon.SIGN_IN.create());
        login.setAutofocus(true);
        Button register = new Button("Register", click -> this.getUI().ifPresent(ui -> ui.navigate(MainPage.class)));
        register.setIcon(VaadinIcon.USER.create());

        add(logo, new H1("Graduate Information System"), mail, password, new HorizontalLayout(
                login, register));

    }
}