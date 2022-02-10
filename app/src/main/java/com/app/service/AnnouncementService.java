package com.app.service;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.app.config.DBConfig;
import com.app.model.Announcement;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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

    public List<Announcement> getAnnouncements(String proficiency) throws Exception {
        Statement statement = db.getInstance().createStatement();
        ResultSet set = statement.executeQuery("SELECT announcement.* FROM announcement,\"user\" as u WHERE announcement.\"from\"=u.id AND  u.profession='" + proficiency + "' order by announcement.created_at desc");
        return constructElements(set);
    }

    public void createAnnouncement(Announcement announcement) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute("INSERT INTO announcement(\"from\",title,content,link) VALUES (" + announcement.getFrom() + ", '" + announcement.getTitle() + "', '" + announcement.getContent() + "', '" + announcement.getLink() + "');");
    }

    public void deleteAnnouncement(Integer id) throws Exception {
        Statement statement = db.getInstance().createStatement();
        statement.execute("DELETE from announcement WHERE id=" + id);
    }

    public Optional<Integer> toggleLike(Integer id, Integer announcementId) {
        try {
            boolean state;
            Statement statement = db.getInstance().createStatement();
            ResultSet set = statement.executeQuery("SELECT count(*) as c FROM likes WHERE post_id=" + announcementId + " AND \"from\"=" + id);
            set.next();
            if (set.getInt("c") > 0) {
                state = false;
                statement.execute("DELETE FROM likes WHERE post_id=" + announcementId + " AND \"from\"=" + id);
            } else {
                state = true;
                statement.execute("INSERT INTO likes(\"from\", post_id) VALUES (" + id + ", " + announcementId + ");");
            }
            set = statement.executeQuery("SELECT count(*) as c FROM likes WHERE post_id=" + announcementId);
            set.next();
            return Optional.of(state ? set.getInt("c") : set.getInt("c") * -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private List<Announcement> constructElements(ResultSet set) throws Exception {
        List<Announcement> result = new LinkedList<>();
        int i = 0;
        while (set.next() && i < 50) {
            System.out.printf("%d \t", i++);
            result.add(new Announcement(set.getInt("id"), set.getInt("from"), set.getString("title"), set.getString("content"), set.getString("link"), set.getInt("like"), set.getTimestamp("created_at")));
        }
        System.out.println("Constructed " + i + " announcements");
        result.forEach(iter -> System.out.println(iter.toString()));
        return result;
    }
}
