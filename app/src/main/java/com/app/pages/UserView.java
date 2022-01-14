package com.app.pages;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.components.PostLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserView extends VerticalLayout {
    public UserView(User searchedUser, User user, App parent) {
        Image pImage = new Image("https://tradingbeasts.com/wp-content/uploads/2019/05/avatar.jpg", "profile");
        if (searchedUser.getImagePath() != null)
            pImage = new Image(searchedUser.getImagePath(), "");
        pImage.setMinHeight(128, Unit.PIXELS);
        add(pImage, new H3(searchedUser.getFirstName() + " " + searchedUser.getSecondName()),
                new H5(searchedUser.getCompany() != null ? (searchedUser.getProfession() + " at " + searchedUser.getCompany()) : searchedUser.getProfession()));

        Accordion personalInformation = new Accordion();
        personalInformation.add("Personal Information",
                new VerticalLayout(
                        new Span(searchedUser.getFirstName() + " " + searchedUser.getSecondName()),
                        new HorizontalLayout(
                                VaadinIcon.PHONE.create(), new Span(searchedUser.getPhone())
                        ),
                        new HorizontalLayout(
                                VaadinIcon.MAILBOX.create(), new Anchor(searchedUser.getMail(), searchedUser.getMail())
                        )
                ));
        Accordion about = new Accordion();
        about.add("About", new Paragraph(searchedUser.getAbout()));
        add(personalInformation, about);

        if (searchedUser.getId().equals(user.getId())) {
            add(new MessageInput(ev -> {
                try {
                    parent.getAnnouncementService().createAnnouncement(new Announcement("Title", ev.getValue(), ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        VerticalLayout announcements = new VerticalLayout();
        try {
            for (Announcement iter : parent.getAnnouncementService().getAnnouncements(searchedUser.getId())) {
                PostLayout post = new PostLayout(searchedUser, user, iter, parent.getAnnouncementService(), false);
                post.getTop().addClickListener(click -> {
                    parent.setContent(new UserView(
                            parent.getUserService().findUserById(iter.getFrom()), user, parent));
                    UI.getCurrent().getPage().reload();
                });
                announcements.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(announcements);
        announcements.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

    }
}
