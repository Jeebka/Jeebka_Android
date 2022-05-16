package edu.escuelaing.ieti.jeebka.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.escuelaing.ieti.jeebka.GroupDetailsView.DynamicLinksRvModel;

public class Link {
    public String id;
    public String url;
    public String name;
    public Date date;
    public List<String> groups;
    public List<String> tags;

    public Link(String url, String name, List<String> tags, List<String> groups){
        id = UUID.randomUUID().toString();
        this.url = url;
        this.name = name;
        date = new Date();
        this.tags = tags;
        this.groups = groups;
    }
    public Link(DynamicLinksRvModel rvModel){
        url = rvModel.getUrl();
        name = rvModel.getName();
        tags = rvModel.getTags();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        tags = tags;
    }
}
