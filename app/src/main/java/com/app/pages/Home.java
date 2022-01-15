package com.app.pages;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.components.AnnouncementLayout;
import com.app.pages.components.PostLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Home extends VerticalLayout {
    public Home(User user, App parent) {

        Notification notification = new Notification();
        notification.add(new Icon(VaadinIcon.SIGN_IN), new Text("Welcome " + user.getFirstName() + " " + user
                .getSecondName()));
        notification.setDuration(10);
        notification.open();
        add(new H1("Welcome " + user.getFirstName()),
                new AnnouncementLayout(user, parent.getAnnouncementService()));
        try {
            for (Announcement iter : parent.getAnnouncementService().getAnnouncements()) {
                add(new PostLayout(parent.getUserService().findUserById(iter.getFrom()), user, iter, parent, true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
