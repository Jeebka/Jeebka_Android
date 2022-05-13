package edu.escuelaing.ieti.jeebka.DTOs;

public class GroupDto {
    public String name;
    public String description;
    public boolean isPublic;
    public String color;

    public GroupDto(String name, String description, boolean isPublic, String color){
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.color = color;
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

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
