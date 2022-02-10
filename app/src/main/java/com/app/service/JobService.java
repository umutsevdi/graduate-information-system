package com.app.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.app.config.DBConfig;
import com.app.model.Job;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@AllArgsConstructor
@Getter
public class JobService {
    private DBConfig db;
    private UserService userService;

    public List<Job> getJobs() throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM job_ad order by created_at desc");
        return constructElements(set);
    }

    public List<Job> getAnnouncements(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM job_ad WHERE \"from\"=" + id + " order by created_at desc");
        return constructElements(set);
    }

    public void createJob(Job job) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute(
                "INSERT INTO job_ad(\"from\",title,content) VALUES (" +
                        job.getFrom() + ", '" +
                        job.getTitle() + "', '" +
                        job.getContent() + "'  );"
        );
    }

    public void deleteAnnouncement(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute("DELETE from job_ad WHERE id=" + id);
    }

    private List<Job> constructElements(ResultSet set) throws Exception {
        List<Job> result = new LinkedList<>();
        int i = 0;
        while (set.next()) {
            System.out.printf("%d \t", i++);
            result.add(new Job(
                    set.getInt("id"),
                    set.getInt("from"),
                    set.getString("title"),
                    set.getString("content"),
                    set.getTimestamp("created_at")
            ));
        }
        System.out.println("Constructed " + i + " jobs");
        result.forEach(iter -> System.out.println(iter.toString()));
        return result;
    }
}
