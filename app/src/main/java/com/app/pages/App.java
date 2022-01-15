package com.app.pages;

import com.app.model.User;
import com.app.service.AnnouncementService;
import com.app.service.AuthenticationService;
import com.app.service.JobService;
import com.app.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.Getter;

@Route("")
@PageTitle("Home | Graduate Information System")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Getter
public class App extends AppLayout
        implements BeforeEnterObserver, AfterNavigationObserver {
    private User user;
    private String token;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final JobService jobService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        token = ((String) VaadinSession.getCurrent().getAttribute("token"));
        if (token == null || token.isBlank()) {
            event.rerouteTo(LoginView.class);
        }
    }

    public App(AuthenticationService authenticationService, AnnouncementService announcementService, UserService userService, JobService jobService) {
        this.announcementService = announcementService;
        this.userService = userService;
        this.jobService = jobService;
        try {
            user = authenticationService.validateUser(
                    (String) VaadinSession.getCurrent().getAttribute("token"));
        } catch (
                Exception e) {
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
        Image logo = new Image("https://www.yildiz.edu.tr/images/files/ytulogopng.png", "Logo");
        logo.setMaxHeight(44, Unit.PIXELS);


        Button toggleButton = new Button();
        toggleButton.setIcon(VaadinIcon.MOON_O.create());
        toggleButton.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList(); // (1)

            if (themeList.contains(Lumo.DARK)) {
                toggleButton.setIcon(VaadinIcon.MOON_O.create());
                themeList.remove(Lumo.DARK);
            } else {
                toggleButton.setIcon(VaadinIcon.MOON.create());
                themeList.add(Lumo.DARK);
            }
        });
        addToNavbar(toggle, toggleButton);
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout left = new HorizontalLayout();
        logo.setHeight(64, Unit.PIXELS);
        left.add(logo);
        left.addClickListener(click -> UI.getCurrent().getPage().reload());

        addToNavbar(left);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (user == null) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        TextField searchbar = new TextField();
        searchbar.setMinWidth(400, Unit.PIXELS);
        Button searchButton = new Button("Search",
                click -> {
                    if (!searchbar.getValue().isBlank()) {
                        User found = userService.findByName(searchbar.getValue().split(" ")[0], searchbar.getValue().split(" ")[1]);
                        if (found != null) {
                            setContent(new UserView(found, user, this));
                        } else {
                            Notification.show(searchbar.getValue() + " not found");
                            searchbar.setValue("");
                        }
                    }
                });
        searchButton.setIcon(VaadinIcon.SEARCH.create());

        Avatar avatar = new Avatar(user.getFirstName() + " " + user.getSecondName());
        if (user.getImagePath() != null) {
            avatar.setImage(user.getImagePath());
        }
        HorizontalLayout userBar = new HorizontalLayout(avatar, new H6(user.getFirstName() + " " + user.getSecondName()));
        userBar.setAlignItems(FlexComponent.Alignment.CENTER);
        userBar.setAlignSelf(FlexComponent.Alignment.STRETCH);

        userBar.addClickListener(click -> setContent(new UserView(user, user, this)));
        Tab home = new Tab(VaadinIcon.HOME.create(), new Span("Home"));
        Tab profile = new Tab(VaadinIcon.USER.create(), new Span("Profile"));
        Tab myClass = new Tab(VaadinIcon.BUILDING.create(), new Span("My Class"));
        Tab jobs = new Tab(VaadinIcon.BRIEFCASE.create(), new Span("Jobs"));
        Tab settings = new Tab(VaadinIcon.COG.create(), new Span("Settings"));
        Tab logout = new Tab(VaadinIcon.SIGN_OUT.create(), new Span("Log Out"));
        Tabs drawerTab = new Tabs(
                home, profile, myClass, jobs, settings, logout);
        drawerTab.setOrientation(Tabs.Orientation.VERTICAL);
        drawerTab.addSelectedChangeListener(selectedChangeEvent -> {
            if (!selectedChangeEvent.getSelectedTab().equals(selectedChangeEvent.getPreviousTab())) {

                Tab selectedTab = selectedChangeEvent.getSelectedTab();
                user = userService.findUserById(user.getId());
                if (home.equals(selectedTab)) {
                    setContent(new Home(user, this));
                } else if (profile.equals(selectedTab)) {
                    setContent(new UserView(user, user, this));
                } else if (myClass.equals(selectedTab)) {
                    setContent(new MyClass(user, this));
                } else if (jobs.equals(selectedTab)) {
                    setContent(new JobEntries(user, this));
                } else if (settings.equals(selectedTab)) { //TODO
                    setContent(new Home(user, this));
                } else if (logout.equals(selectedTab)) {
                    AuthenticationService.getSessions().remove("token");
                    UI.getCurrent().getPage().setLocation("login");
                    UI.getCurrent().getPage().reload();
                }
                drawerTab.setSelectedTab(selectedChangeEvent.getSelectedTab());
            }
        });
        addToNavbar(searchbar, searchButton, userBar);

        setContent(new Home(user, this));


        addToDrawer(drawerTab);
    }
}