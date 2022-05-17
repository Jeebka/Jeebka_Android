package edu.escuelaing.ieti.jeebka.Models;

import java.util.List;

public class LinkUpdateRequest {
    public String name;
    public List<String> tags;

    public LinkUpdateRequest(String name, List<String> tags) {
        this.name = name;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
