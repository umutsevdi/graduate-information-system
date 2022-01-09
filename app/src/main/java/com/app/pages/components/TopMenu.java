package com.app.pages.components;

import com.app.pages.MainPage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLink;


public class TopMenu extends HorizontalLayout {
    public TopMenu() {
        Image schoolIcon = new Image("https://cdn.freelogovectors.net/wp-content/uploads/2018/03/yildiz-teknik-universitesi-logo-ytu.png", "yildiz-technical-university");
        schoolIcon.setMaxWidth("24px");
        schoolIcon.setMaxHeight("24px");
        MenuBar menuBar = new MenuBar();
        menuBar.addItem("Home");
        menuBar.addItem(new RouterLink("Home", MainPage.class));
        Accordion profileMenubar = new Accordion();
        profileMenubar.add("Profile", profileMenubar);
        profileMenubar.add("Edit Profile", profileMenubar);
        profileMenubar.add("Logout", profileMenubar);
        menuBar.addItem(profileMenubar);
        HorizontalLayout searchBox = new HorizontalLayout();
        TextField searchField = new TextField();
        searchField.setPlaceholder("Search");
        searchField.setMinWidth(700, Unit.PIXELS);
        Button searchButton = new Button("", click -> {
            searchBox.getUI().ifPresent(ui -> ui.navigate(searchField.getValue()));
        });
        searchButton.setIcon(new Icon("search"));
        searchBox.add(
                (Component) searchField,
                (Component) searchButton
        );
        this.add(

                new Button(schoolIcon, click -> {
                    this.getUI().ifPresent(ui -> ui.navigate(MainPage.class));
                }),
                new Button(
                        new H2("Mezun Bilgilendirme Sistemi"),
                        click -> getUI().ifPresent(ui -> ui.navigate(MainPage.class))),
                searchBox,
                menuBar
        );
    }
}
