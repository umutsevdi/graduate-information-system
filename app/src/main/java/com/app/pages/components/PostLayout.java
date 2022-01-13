package com.app.pages.components;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.UserView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class PostLayout extends HorizontalLayout {
    private User user;
    private Announcement announcement;

    public PostLayout(User user, Announcement announcement) {
        this.user = user;
        this.announcement = announcement;
        VerticalLayout top = new VerticalLayout();


        H3 header = new H3(user.getFirstName() + " " + user.getSecondName());
        Image image = new Image(user.getImagePath(), user.getId() + "");
        header.addClickListener(
                click -> this.getUI().ifPresent(
                        ui -> ui.navigate(UserView.class, user.getId()))
        );
        image.addClickListener(
                click -> this.getUI().ifPresent(
                        ui -> ui.navigate(UserView.class, user.getId()))
        );
        add(
                new VerticalLayout(
                        image, header, new H2(" - " + announcement.getTitle())
                ),
                new Text(announcement.getContent())
        );
        if (announcement.getLink() != null) {
            if (
                    List.of("png", "jpg", "jpeg", "svg").contains(
                            (announcement.getLink().toLowerCase().split("\\.")
                                    [announcement.getLink().toLowerCase().split("\\.").length - 1]
                            ))) {
                add(new Image(announcement.getLink(), announcement.getTitle())
                );
            } else {
                add(new Button("View Link",
                        click -> this.getUI().ifPresent(
                                ui -> ui.navigate(announcement.getLink())
                        )
                ));
            }
        }
    }
}
