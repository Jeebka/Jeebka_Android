package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DynamicLinksRvModel {
    public String id;
    public String url;
    public String name;
    public Date date;
    public List<String> groups;
    public List<String> tags;
    private int pos;

    public DynamicLinksRvModel(String name, String url, int pos) {
        this.name = name;
        this.url = url;
        this.pos = pos;
        this.groups = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public int getPos() {
        return pos;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getTags() {
        return tags;
    }
}
