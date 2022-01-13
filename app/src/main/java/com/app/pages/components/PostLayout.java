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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Optional;

@Tag("ex-card")
@JsModule("./src/components/ex-card.js")
public class PostLayout extends VerticalLayout implements HasStyle, HasComponents {
    public PostLayout(Integer userId, User user, Announcement announcement, AnnouncementService announcementService) {

        this.setAlignItems(Alignment.START);
        HorizontalLayout top = new HorizontalLayout(
                new Avatar(user.getFirstName() + " " + user.getSecondName(), user.getImagePath()),
                new H6(user.getFirstName() + " " + user.getSecondName()),
                new H4(announcement.getTitle().toUpperCase())
        );
        Button like = new Button("Like", click ->
        {
            Optional<Integer> likes = announcementService.toggleLike(userId, announcement.getId());
            if (likes.isPresent()) {
                int likeCount = (likes.get() > 0 ? likes.get() : likes.get() * -1);
                click.getSource().setText(likeCount + (likeCount > 1 ? " Likes" : " Like"));
                System.out.println(likes.get());
                if (likes.get() > 0)
                    click.getSource().setIcon(VaadinIcon.HEART.create());
                else
                    click.getSource().setIcon(VaadinIcon.HEART_O.create());
            }
        });
        like.click();
        like.click();
        add(top, new Paragraph(announcement.getContent()));
        if (announcement.getLink() != null) {
            add(new Image(announcement.getLink(), announcement.getTitle()));
        }
        add(like);
    }
}
