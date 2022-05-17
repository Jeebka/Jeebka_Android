package edu.escuelaing.ieti.jeebka.GroupDetailsView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;

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

    public DynamicLinksRvModel(Link link, int pos) {
        this.id = link.getId();
        this.name = link.getName();
        this.url = link.getUrl();
        this.pos = pos;
        this.groups = link.getGroups();
        this.tags = link.getTags();
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
