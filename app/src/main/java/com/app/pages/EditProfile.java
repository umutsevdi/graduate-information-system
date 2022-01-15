package com.app.pages;

import com.app.model.User;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.log4j.Log4j2;

import java.sql.Date;

@Log4j2
public class EditProfile extends VerticalLayout {
    public EditProfile(User user, App parent) {
        Image pImage = new Image("https://tradingbeasts.com/wp-content/uploads/2019/05/avatar.jpg", "profile");
        if (user.getImagePath() != null)
            pImage = new Image(user.getImagePath(), "");
        Accordion personalInformation = new Accordion();
        TextField firstName = new TextField("First Name");
        firstName.setPlaceholder(user.getFirstName());
        TextField lastName = new TextField("Last Name");
        lastName.setPlaceholder(user.getSecondName());
        TextField phone = new TextField("Phone");
        phone.setPlaceholder(user.getPhone());
        EmailField mail = new EmailField("Email Address");
        mail.setPlaceholder(user.getMail());
        TextArea aboutData = new TextArea("About");
        aboutData.setPlaceholder(user.getAbout());

        TextField profileURL = new TextField("Profile Picture");
        profileURL.setPlaceholder(user.getImagePath());
        DatePicker dob = new DatePicker();
        dob.setInitialPosition(user.getDob().toLocalDate());
        TextField cv = new TextField("CV");
        cv.setPlaceholder(user.getImagePath());
        add(pImage);
        personalInformation.add("Personal Information",
                new VerticalLayout(
                        new HorizontalLayout(firstName, lastName),
                        dob,
                        new HorizontalLayout(
                                VaadinIcon.PHONE.create(), phone
                        ),
                        new HorizontalLayout(
                                VaadinIcon.MAILBOX.create(), mail)
                )
        );
        Accordion about = new Accordion();
        about.add("About", aboutData);
        add(personalInformation, about);
        Accordion cvData = new Accordion();
        cvData.add("CV", new VerticalLayout(profileURL, cv));
        add(cvData,
                new Button("Update Profile", click -> {
                    User updatedUser = new User(
                            user.getId(),
                            firstName.isEmpty() ? user.getFirstName() : firstName.getValue(),
                            lastName.isEmpty() ? user.getSecondName() : lastName.getValue(),
                            mail.isEmpty() ? user.getMail() : mail.getValue(),
                            phone.isEmpty() ? user.getPhone() : phone.getValue(),
                            user.getPassword(),
                            user.getGender(),
                            dob.isEmpty() ? user.getDob() : Date.valueOf(dob.getValue()),
                            user.getCreatedAt(),
                            user.getProfession(),
                            user.getGraduationYear(),
                            user.getCompany(),
                            user.getOpenToWork(),
                            aboutData.isEmpty() ? user.getAbout() : aboutData.getValue(),
                            profileURL.isEmpty() ? user.getImagePath() : profileURL.getValue(),
                            cv.isEmpty() ? user.getImagePath() : cv.getValue()
                    );
                    try {
                        parent.getUserService().updateUser(updatedUser);
                    } catch (Exception e) {
                        Notification.show(e.getMessage());
                    }
                    parent.setContent(new UserView(user, user, parent));
                }));
    }
}
