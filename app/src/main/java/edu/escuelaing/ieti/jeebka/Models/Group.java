package edu.escuelaing.ieti.jeebka.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Group {
    public String id;
    public String name;
    public String description;
    @SerializedName("public")
    @Expose
    public boolean isPublic;
    public String color;
    public HashSet<String> linksTags;
    public List<String> members;
    public List<Link> links;

    public Group(String name, boolean isPublic, String color) {
        this.name = name;
        this.isPublic = isPublic;
        this.color = color;
        this.linksTags = new HashSet<>();
        this.members = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public HashSet<String> getLinksTags() {
        return linksTags;
    }

    public void setLinksTags(HashSet<String> linksTags) {
        this.linksTags = linksTags;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String toString(){
        return "Group:{id= " + id + " name= "+ name + " description= " + description + " isPublic= " + isPublic + " color= " + color  + " links= " + links.size() +"}";
    }
}
