package com.app.pages.components;

import com.app.model.Announcement;
import com.app.model.User;
import com.app.service.AnnouncementService;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;


@Tag("ex-card")
@JsModule("./src/components/ex-card.js")
public class AnnouncementLayout extends VerticalLayout {
    public AnnouncementLayout(User user, AnnouncementService announcementService) {

        TextField title = new TextField("Title");
        TextArea content = new TextArea("Share what do you think...");
        TextField link = new TextField("Add a link");
        link.setAutocomplete(Autocomplete.URL);
        link.setAutocorrect(true);
        Button sendButton = new Button("Share", click -> {
            try {
                announcementService.createAnnouncement(new Announcement(user.getId(),
                        title.getValue(), content.getValue(), link.getValue()
                ));
            } catch (Exception e) {
                Notification.show(e.getMessage());
            }
        });
        sendButton.setIcon(VaadinIcon.SHARE.create());
        add(title, new Paragraph(), content,new Paragraph(), link, new Paragraph(), sendButton);

    }
}
