package com.app.pages.components;

import com.app.model.User;
import com.app.pages.LoginView;
import com.app.pages.UserView;
import com.app.service.AnnouncementService;
import com.app.service.AuthenticationService;
import com.app.service.UserService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;

@Getter
public class BasicLayout extends AppLayout {
    private AuthenticationService authenticationService;
    private final UserService userService;
    User user;
    public BasicLayout(AuthenticationService authenticationService,UserService userService){
        this.authenticationService = authenticationService;
        this.userService = userService;
        try {
            user = authenticationService.validateUser(
                    (String) VaadinSession.getCurrent().getAttribute("token"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Notification.show("Token is expired");
            this.getUI().ifPresent(
                    ui -> ui.navigate(LoginView.class));
        }

        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Graduate Information System");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Tabs tabs = new Tabs();
        tabs.add(
                new Tab("Home")
        );

        Image logo = new Image("https://dx5i3n065oxey.cloudfront.net/platform/9531/assets/logo.png", "Logo");
        logo.setMaxHeight(32, Unit.PIXELS);

        TextField searchbar = new TextField();
        Button searchButton = new Button("Search",
                click -> {
                    if (!searchbar.getValue().isBlank()) {
                        this.getUI().ifPresent(ui -> ui.navigate(UserView.class,
                                Integer.valueOf(searchbar.getValue())));
                    }
                });
        searchButton.setIcon(VaadinIcon.SEARCH.create());
        addToDrawer(tabs);

        addToNavbar(
                toggle, logo, title, searchbar, searchButton
        );

        setContent(new TopMenu(user));
        Notification notification = new Notification();
        notification.add(new Icon(VaadinIcon.SIGN_IN), new Text("Welcome " + user.getFirstName() + " " + user
                .getSecondName()));
        notification.setDuration(10);
        notification.open();

    }
}
