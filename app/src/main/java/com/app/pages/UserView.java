package com.app.pages;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.components.PostLayout;
import com.app.service.AnnouncementService;
import com.app.service.AuthenticationService;
import com.app.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.log4j.Log4j2;

@Route("profile")
@PageTitle("User Profile | Graduate Information System")
@Log4j2
public class UserView extends AppLayout implements HasUrlParameter<Integer>, AfterNavigationObserver {
    private final AuthenticationService authenticationService;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private Integer userId;
    private User user;
    private Tabs tabs;
    private VerticalLayout main;

    @Override
    public void setParameter(BeforeEvent event,
                             Integer parameter) {
        if (parameter != null) {
            this.userId = parameter;
        } else {
            event.rerouteTo(App.class);
        }
    }

    public UserView(AuthenticationService authenticationService, AnnouncementService announcementService, UserService userService) {
        this.authenticationService = authenticationService;
        this.announcementService = announcementService;
        this.userService = userService;
        try {
            user = authenticationService.validateUser(
                    (String) VaadinSession.getCurrent().getAttribute("token"));
        } catch (Exception e) {
            System.out.println("User is unauthorized");
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
        main = new VerticalLayout();

        setContent(main);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (user == null || userId == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        if (userId.equals(user.getId())) {
            main.add(new MessageInput(ev -> {
                try {
                    announcementService.createAnnouncement(new Announcement(userId, "Title", ev.getValue(), ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        VerticalLayout announcements = new VerticalLayout();
        try {
            for (Announcement iter : announcementService.getAnnouncements(userId)) {
                announcements.add(new PostLayout(user.getId(), userService.findUserById(userId), iter, announcementService));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.add(announcements);
        announcements.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

    }
}
