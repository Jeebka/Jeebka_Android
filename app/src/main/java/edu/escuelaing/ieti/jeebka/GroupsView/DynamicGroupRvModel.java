package edu.escuelaing.ieti.jeebka.GroupsView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DynamicGroupRvModel {
    public String id;
    public String name;
    public String description;
    public boolean isPublic;
    public String color;
    public HashSet<String> linksTags;
    public List<String> members;
    public List<String> links;
    int pos;

    public DynamicGroupRvModel(String name, String description, boolean isPublic, String color, int pos, List<String> members, List<String> links) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.color = color;
        this.pos = pos;
        this.links = links;
        this.members = members;
        this.linksTags = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic(){
        return isPublic;
    }

    public String getColor(){
        return color;
    }

    public HashSet<String> getLinksTags() {
        return linksTags;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<String> getLinks() {
        return links;
    }

    public int getPos() {
        return pos;
    }
}