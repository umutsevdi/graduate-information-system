package com.app.pages.components;

import com.app.model.User;
import com.app.pages.SearchPage;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

public class TopMenu extends HorizontalLayout {
    public TopMenu(User user) {
        this.setAlignItems(Alignment.STRETCH);
        Image logo = new Image("https://dx5i3n065oxey.cloudfront.net/platform/9531/assets/logo.png", "Logo");
        logo.setMaxHeight(32, Unit.PIXELS);

        HorizontalLayout rightBar = new HorizontalLayout();
        Avatar avatar = new Avatar(user.getFirstName() + " " + user.getSecondName());
        if (user.getImagePath() != null) {
            avatar.setImage(user.getImagePath());
        } else {
            avatar.setImage("https://tradingbeasts.com/wp-content/uploads/2019/05/avatar.jpg");
        }
        rightBar.setAlignItems(Alignment.END);

        Accordion profileMenubar = new Accordion();
        profileMenubar.add("Profile", new Button());
        profileMenubar.add("Edit Profile", new Button());
        profileMenubar.add("Logout", new Button());
        rightBar.add(avatar, profileMenubar);

        this.add(
                new HorizontalLayout(logo, new H1("GIS")),
                new MessageInput(event -> Notification.show(event.getValue())),
                profileMenubar,
                avatar,
                new H1("Welcome "+user.getFirstName())
        );

    }
}
