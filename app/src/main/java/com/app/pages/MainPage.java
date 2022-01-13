package com.app.pages;

import com.app.model.User;
import com.app.pages.components.TopMenu;
import com.app.service.AuthenticationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
@PageTitle("Home | Graduate Information System")
public class MainPage extends AppLayout
        implements BeforeEnterObserver {
    private User user;
    private String token;
    private final AuthenticationService authenticationService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        token = ((String) VaadinSession.getCurrent().getAttribute("token"));
        if (token == null || token.isBlank()) {
            System.out.println(
                    "I'd just like to interject for a moment. What you're referring to as Linux,\n" +
                            "is in fact, GNU/Linux, or as I've recently taken to calling it, GNU plus Linux.\n" +
                            "Linux is not an operating system unto itself, but rather another free component\n" +
                            "of a fully functioning GNU system made useful by the GNU core libs, shell\n" +
                            "utilities and vital system components comprising a full OS as defined by POSIX"
            );
            event.rerouteTo(LoginView.class);
        }
    }

    public MainPage(AuthenticationService authenticationService) {
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