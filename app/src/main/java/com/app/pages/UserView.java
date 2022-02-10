package com.app.pages;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.components.AnnouncementLayout;
import com.app.pages.components.PostLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class UserView extends VerticalLayout {
    public UserView(User searchedUser, User user, App parent) {
        Image pImage = new Image("https://tradingbeasts.com/wp-content/uploads/2019/05/avatar.jpg", "profile");
        if (searchedUser.getImagePath() != null)
            pImage = new Image(searchedUser.getImagePath(), "");
        pImage.setMinHeight(128, Unit.PIXELS);
        add(pImage, new H3(searchedUser.getFirstName() + " " + searchedUser.getSecondName()),
                new H5(searchedUser.getCompany() != null
                        ? (searchedUser.getProfession() + " at " + searchedUser.getCompany())
                        : searchedUser.getProfession()));

        Accordion personalInformation = new Accordion();
        personalInformation.add("Personal Information",
                new VerticalLayout(
                        new Span(searchedUser.getFirstName() + " " + searchedUser.getSecondName()),
                        new HorizontalLayout(
                                VaadinIcon.PHONE.create(), new Span(searchedUser.getPhone())),
                        new HorizontalLayout(
                                VaadinIcon.MAILBOX.create(),
                                new Anchor(searchedUser.getMail(), searchedUser.getMail()))));
        Accordion about = new Accordion();
        about.add("About", new Paragraph(searchedUser.getAbout()));
        add(personalInformation, about);
        if (searchedUser.getCvPath() != null) {
            Accordion cv = new Accordion();
            cv.add("CV", new Anchor(searchedUser.getCvPath(), "CV"));
            add(cv);
        }
        if (searchedUser.getId().equals(user.getId())) {
            add(
                    new Button("Edit Profile", click -> parent.setContent(new EditProfile(user, parent))),
                    new AnnouncementLayout(user, parent.getAnnouncementService()));
        }
        VerticalLayout announcements = new VerticalLayout();
        try {
            for (Announcement iter : parent.getAnnouncementService().getAnnouncements(searchedUser.getId())) {
                PostLayout post = new PostLayout(searchedUser, user, iter, parent, false);
                announcements.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(announcements);
        announcements.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

    }
}