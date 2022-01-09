package com.app.pages;

import com.app.model.User;
import com.app.pages.components.TopMenu;
import com.app.service.AuthenticationService;
import com.app.service.UserService;
import com.app.service.UserSession;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Home | Graduate Information System")
public class MainPage extends VerticalLayout implements HasUrlParameter<String> {
    private User user;
    private String token;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Override
    public void setParameter(BeforeEvent event,
                             String token) {

        UserSession session = authenticationService.getSessions().get(token);
        if (!authenticationService.validate(token)) {
            this.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        }
        try {
            User user = userService.getUser(
                session.getMail()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        Notification.show(String.valueOf(user));
        Notification.show("Welcome " + session.getMail());
        this.token = token;
    }

    public MainPage(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;

        VerticalLayout todosList = new VerticalLayout();
        TextField taskField = new TextField();
        Button addButton = new Button("Add");
        addButton.addClickListener(click -> {
            Checkbox checkbox = new Checkbox(taskField.getValue());
            todosList.add(checkbox);
        });
        addButton.addClickShortcut(Key.ENTER);
        add(
                new TopMenu(),
                new H1("Vaadin Todo"),
                todosList,
                new HorizontalLayout(
                        taskField,
                        addButton
                )
        );
    }
}