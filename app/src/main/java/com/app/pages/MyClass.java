package com.app.pages;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Route("profile")
@AllArgsConstructor
@PageTitle("User Profile | Graduate Information System")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Log4j2
public class MyClass extends VerticalLayout implements AfterNavigationObserver {
    private String profession;

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

    }

}
