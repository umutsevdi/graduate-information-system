package com.app.pages;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.pages.components.AnnouncementLayout;
import com.app.pages.components.PostLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import lombok.AllArgsConstructor;

@Route("profile")
@AllArgsConstructor
@PageTitle("User Profile | Graduate Information System")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MyClass extends VerticalLayout {
    public MyClass(User user, App parent) {
        HorizontalLayout userList = new HorizontalLayout();
        Scroller users = new Scroller(userList);
        users.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        users.setHeightFull();
        for (User iter : parent.getUserService().findByProfession(user.getProfession())) {
            Avatar avatar = new Avatar(iter.getFirstName() + " " + iter.getSecondName());
            if (iter.getImagePath() != null) {
                avatar.setImage(iter.getImagePath());
            }
            Span cover = new Span(avatar);
            cover.addClickListener(click -> {
                parent.setContent(new UserView(iter, user, parent));
            });
            userList.add(cover);
        }
        add(new H2(parent.getUserService().getFaculty(user.getProfession())),
                new H3(user.getProfession()),
                users,
                new AnnouncementLayout(user, parent.getAnnouncementService())
        );


        try {
            for (Announcement iter : parent.getAnnouncementService().getAnnouncements(user.getProfession())) {
                add(new PostLayout(parent.getUserService().findUserById(iter.getFrom()), user, iter, parent, false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
