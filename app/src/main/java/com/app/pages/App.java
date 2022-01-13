package com.app.pages;

import com.app.model.User;
import com.app.service.AuthenticationService;
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
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
@PageTitle("Home | Graduate Information System")
public class App extends AppLayout
        implements BeforeEnterObserver {
    private User user;
    private String token;
    private final AuthenticationService authenticationService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        token = ((String) VaadinSession.getCurrent().getAttribute("token"));
        if (token == null || token.isBlank()) {
            event.rerouteTo(LoginView.class);
        }
    }

    public App(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
        Image logo = new Image("https://dx5i3n065oxey.cloudfront.net/platform/9531/assets/logo.png", "Logo");
        logo.setMaxHeight(44, Unit.PIXELS);
        addToNavbar(new DrawerToggle(),logo);
        TextField searchbar = new TextField();
        Button searchButton = new Button("Search",
                click -> {
                    if (!searchbar.getValue().isBlank()) {
                        this.getUI().ifPresent(ui -> ui.navigate(UserView.class,
                                Integer.valueOf(searchbar.getValue())));
                    }
                });
        searchButton.setIcon(VaadinIcon.SEARCH.create());
        Tabs tabs = new Tabs();
        tabs.addSelectedChangeListener(event -> {
            setContent(event.getSelectedTab());
        });
        addToDrawer(tabs);
        addToNavbar(
                title, searchbar, searchButton
        );

        Notification notification = new Notification();
        notification.add(new Icon(VaadinIcon.SIGN_IN), new Text("Welcome " + user.getFirstName() + " " + user
                .getSecondName()));
        notification.setDuration(10);
        notification.open();

    }

}