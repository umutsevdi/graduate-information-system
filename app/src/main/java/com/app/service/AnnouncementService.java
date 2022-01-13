package com.app.service;

import com.app.config.DBConfig;
import com.app.model.Announcement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        statement.execute(
                "INSERT INTO announcement(\"from\",title,content,link) VALUES (" +
                        announcement.getFrom() + ", '" +
                        announcement.getTitle() + "', '" +
                        announcement.getContent() + "', '" +
                        announcement.getLink() + "');"
        );
        /*
      into announcement ("from", title, content, link) values
      (5, 'system engine', 'Test message', null);
        */
    }

    public void deleteAnnouncement(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute("DELETE from announcement WHERE id=" + id);
    }

    public Optional<Integer> toggleLike(Integer id, Integer announcementId) {
        System.out.println("toggle like:"+id+","+announcementId);
        try {
            boolean state;
            Statement statement = db.getInstance().createStatement();
            ResultSet set = statement.executeQuery(
                    "SELECT count(*) as c FROM likes WHERE post_id=" + announcementId + " AND \"from\"=" + id);
            set.next();
            System.out.println(set.getInt("c"));
            if (set.getInt("c") > 0) {
                state = false;
                statement.execute(
                        "DELETE FROM likes WHERE post_id=" + announcementId + " AND \"from\"=" + id
                );
            } else {
                state = true;
                statement.execute(
                        "INSERT INTO likes(\"from\", post_id) VALUES (" + id + ", " + announcementId + ");"
                );
            }
            System.out.println(id + " like? " + announcementId + " = " + state);
            set = statement.executeQuery("SELECT count(*) as c FROM likes WHERE post_id=" + announcementId);
            set.next();
            return Optional.of(state ? set.getInt("c") : set.getInt("c") * -1);

        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private List<Announcement> constructElements(ResultSet set) throws Exception {
        List<Announcement> result = new LinkedList<>();
        int i = 0;
        while (set.next()) {
            System.out.printf("%d \t", i++);
            result.add(new Announcement(
                    set.getInt("id"),
                    set.getInt("from"),
                    set.getString("title"),
                    set.getString("content"),
                    set.getString("link"),
                    set.getInt("like"),
                    set.getTimestamp("created_at")
            ));
        }
        System.out.println("Constructed " + i + " announcements");
        result.forEach(iter -> System.out.println(iter.toString()));
        return result;
    }
}
