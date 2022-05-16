package edu.escuelaing.ieti.jeebka.DTOs;

import java.util.List;

public class TaggedLinkDto {
    public String url;
    public String name;
    public List<String> tags;

    public TaggedLinkDto(String url, String name, List<String> tags) {
        this.url = url;
        this.name = name;
        this.tags = tags;
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

    public List<String> getTags(){ return tags;}

    public void setTags(List<String> tags){
        this.tags = tags;
    }
}
