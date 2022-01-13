package com.app.pages;

import com.app.model.User;
import com.app.pages.components.PostLayout;
import com.app.service.AnnouncementService;
import com.app.service.AuthenticationService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("profile")
@PageTitle("User Profile | Graduate Information System")
public class UserView extends HorizontalLayout implements HasUrlParameter<Integer> {
    private Integer userId;
    private User user;
    private final AuthenticationService authenticationService;
    private final AnnouncementService announcementService;

    @Override
    public void setParameter(BeforeEvent event,
                             Integer userId) {
        this.userId = userId;
    }

    public UserView(AuthenticationService authenticationService, AnnouncementService announcementService) throws Exception {
        this.authenticationService = authenticationService;
        this.announcementService = announcementService;
        try {
            user = authenticationService.validateUser(
                    (String) VaadinSession.getCurrent().getAttribute("token"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Notification.show("Token is expired");
            this.getUI().ifPresent(
                    ui -> ui.navigate(LoginView.class));
        }
        VerticalLayout announcements = new VerticalLayout();
        announcementService.getAnnouncements(userId).forEach(
                iter -> announcements.add(new PostLayout(user, iter))
        );
        Tabs tabs = new Tabs();
        tabs.add(
            announcements
        );


        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Graduate Information System");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        tabs.add(
                new Tab("Home")
        );

        Image logo = new Image("https://dx5i3n065oxey.cloudfront.net/platform/9531/assets/logo.png", "Logo");
        logo.setMaxHeight(32, Unit.PIXELS);

        TextField searchbar = new TextField();
        Button searchButton = new Button("Search",
                click -> {
                    if (!searchbar.getValue().isBlank()) {
                        this.getUI().ifPresent(ui -> ui.navigate(SearchPage.class, searchbar.getValue()));
                    }
                });
        searchButton.setIcon(VaadinIcon.SEARCH.create());

        Notification notification = new Notification();
        notification.add(new Icon(VaadinIcon.SIGN_IN), new Text("Welcome " + user.getFirstName() + " " + user
                .getSecondName()));
        notification.setDuration(10);
        notification.open();

    }
}
