package com.app.service;

import com.app.config.DBConfig;
import com.app.model.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
@Getter
@ToString
public class AnnouncementService {
    private DBConfig db;
    private UserService userService;

    public List<Announcement> getAnnouncements() throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM announcement order by created_at desc");
        return constructElements(set);
    }

    public List<Announcement> getAnnouncements(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery("SELECT * FROM announcement WHERE \"from\"=" + id + " order by created_at desc");
        return constructElements(set);
    }

    // Returns announcements in between given values, from can be null
    public List<Announcement> getAnnouncements(Integer from, Integer count, Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set;
        if (id != null) {
            set = statement.executeQuery("SELECT * FROM announcement WHERE from=" + id + " AND id between " + from + " AND " + (from + count) + " order by created_at desc");
        } else {
            set = statement.executeQuery("SELECT * FROM announcement WHERE id between " + from + " AND " + (from + count) + " order by created_at desc");
        }
        return constructElements(set);
    }

    public void createAnnouncement(Announcement announcement) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery(
                "INSERT INTO announcement(\"from\",title,content,link) VALUES (" +
                        announcement.getFrom() + ", '" +
                        announcement.getTitle() + "', '" +
                        announcement.getContent() + "', '" +
                        announcement.getLink() + "');"
        );
        System.out.println(set.getStatement());
        /*
      into announcement ("from", title, content, link) values
      (5, 'system engine', 'Test message', null);
        */
    }

    public void deleteAnnouncement(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery(
                "DELETE from announcement WHERE id=" + id);
        System.out.println(set.getStatement());
    }

    public void like(Integer id) throws Exception{

    }
    private List<Announcement> constructElements(ResultSet set) throws Exception {
        List<Announcement> result = new LinkedList<>();
        while (set.next()) {
            result.add(new Announcement(
                    set.getInt("id"),
                    set.getInt("\"from\""),
                    set.getString("title"),
                    set.getString("content"),
                    set.getString("link"),
                    set.getInt("like"),
                    set.getTimestamp("created_at")
            ));
        }
        return result;
    }
}
