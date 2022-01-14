package com.app.pages;

import com.app.model.Job;
import com.app.model.User;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;

public class JobEntries extends VerticalLayout {
    private static App parent;

    public JobEntries(User user, App parent) {
        JobEntries.parent = parent;

        if (user.getCompany() != null) {
            add(new MessageInput(ev -> {
                try {
                    parent.getJobService().createJob(new Job(user.getId(), "Title", ev.getValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));
        }
        Grid<Job> jobs = new Grid<>(Job.class, false);
        jobs.setSelectionMode(Grid.SelectionMode.MULTI);
        jobs.addColumn(Job::getId).setHeader("Id");
        jobs.addColumn(fromRenderer()).setHeader("From");
        jobs.addColumn(companyRenderer()).setHeader("Company");
        jobs.addColumn(Job::getTitle).setHeader("Title");
        jobs.addColumn(Job::getContent).setHeader("Content");
        try {
            jobs.setItems(parent.getJobService().getJobs());
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(jobs);
    }

    private static ComponentRenderer<Span, Job> fromRenderer() {
        return new ComponentRenderer<>(Span::new, fromUpdater);
    }

    private static ComponentRenderer<Span, Job> companyRenderer() {
        return new ComponentRenderer<>(Span::new, companyUpdater);
    }

    private static final SerializableBiConsumer<Span, Job> companyUpdater = ((span, job) -> {
        span.add(new Text(parent.getUserService().findUserById(job.getFrom()).getCompany()));
    });
    private static final SerializableBiConsumer<Span, Job> fromUpdater = ((span, job) -> {
        HorizontalLayout item = new HorizontalLayout();
        User owner = parent.getUserService().findUserById(job.getFrom());
        item.add(
                new Avatar(owner.getFirstName() + " " + owner.getSecondName(), owner.getImagePath()),
                new Text(owner.getFirstName() + " " + owner.getSecondName()));
        item.addClickListener(click -> {
            parent.setContent(new UserView(owner, parent.getUser(), parent));
        });
        span.add(item);
    });
}