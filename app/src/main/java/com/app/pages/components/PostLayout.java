package com.app.pages.components;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.service.AnnouncementService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

import java.util.Optional;

@Tag("ex-card")
@JsModule("./src/components/ex-card.js")
@Getter
public class PostLayout extends VerticalLayout implements HasStyle, HasComponents {
    private HorizontalLayout top;

    public PostLayout(User owner, User user, Announcement announcement, AnnouncementService announcementService, Boolean badgesOn) {
        this.setAlignItems(Alignment.START);
        top = new HorizontalLayout();
        if (owner.getImagePath() != null) {
            top.add(new Avatar(owner.getFirstName() + " " + owner.getSecondName(), owner.getImagePath()));
        } else {
            top.add(new Avatar(owner.getFirstName() + " " + owner.getSecondName()));
        }
        top.add(new H5(owner.getFirstName() + " " + owner.getSecondName()));
        if (badgesOn) {
            add(user.getProfession());
        }
        add(top, new H4(announcement.getTitle().toUpperCase()), new Paragraph(announcement.getContent()));
        if (announcement.getLink() != null) {
            add(new Image(announcement.getLink(), announcement.getTitle()), new Paragraph());
        }
        Button like = new Button("", click ->
        {
            Optional<Integer> likes = announcementService.toggleLike(user.getId(), announcement.getId());
            if (likes.isPresent()) {
                click.getSource().setText((likes.get() > 0 ? likes.get() : likes.get() * -1) + "");
                if (likes.get() > 0)
                    click.getSource().setIcon(VaadinIcon.HEART.create());
                else
                    click.getSource().setIcon(VaadinIcon.HEART_O.create());
            }
        });
        like.click();
        like.click();
        add(like);
    }
}
