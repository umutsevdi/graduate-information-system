package com.app.pages.components;

import java.util.Optional;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.App;
import com.app.pages.UserView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import lombok.Getter;

@Tag("ex-card")
@JsModule("./src/components/ex-card.js")
@Getter
public class PostLayout extends VerticalLayout {

    public PostLayout(User owner, User user, Announcement announcement, App parent, Boolean badgesOn) {
        this.setAlignItems(Alignment.START);
        HorizontalLayout top = new HorizontalLayout();
        if (owner.getImagePath() != null) {
            top.add(new Avatar(owner.getFirstName() + " " + owner.getSecondName(), owner.getImagePath()));
        } else {
            top.add(new Avatar(owner.getFirstName() + " " + owner.getSecondName()));
        }
        top.add(new H5(owner.getFirstName() + " " + owner.getSecondName()));
        add(top);
        top.addClickListener(click -> {
            parent.setContent(new UserView(owner, user, parent));
        });
        if (badgesOn) {
            add(new Text(user.getProfession()));
        }

        add(new H4(announcement.getTitle().toUpperCase()), new Paragraph(announcement.getContent()));
        if (announcement.getLink() != null) {
            add(new Image(announcement.getLink(), announcement.getTitle()), new Paragraph());
        }
        Button like = new Button("", click -> {
            Optional<Integer> likes = parent.getAnnouncementService().toggleLike(user.getId(), announcement.getId());
            if (likes.isPresent()) {
                click.getSource().setText((likes.get() > 0 ? likes.get() : likes.get() * -1) + "");
                if (likes.get() > 0)
                    click.getSource().setIcon(VaadinIcon.HEART.create());
                else
                    click.getSource().setIcon(VaadinIcon.HEART_O.create());
            }
        });
        HorizontalLayout bottomBar = new HorizontalLayout(like);

        if (user.getId().equals(announcement.getFrom())) {
            Button delete = new Button("", click -> {

                try {
                    parent.getAnnouncementService().deleteAnnouncement(announcement.getId());
                } catch (Exception e) {
                    Notification.show(e.getMessage());
                }

            });
            delete.setIcon(VaadinIcon.TRASH.create());
            bottomBar.add(delete);
        }
        like.click();
        like.click();
        add(bottomBar);
    }
}
