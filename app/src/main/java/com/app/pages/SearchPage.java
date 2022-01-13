package com.app.pages;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("search")
@PageTitle("Search Results | Graduate Information System")
public class SearchPage extends VerticalLayout implements HasUrlParameter<String> {
    private String input;

    @Override
    public void setParameter(BeforeEvent event,
                             String input) {
        this.input = input;
    }

    public SearchPage() {
        this.add(new Text(input));

    }
}
