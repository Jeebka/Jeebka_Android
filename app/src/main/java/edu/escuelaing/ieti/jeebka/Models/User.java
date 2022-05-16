package edu.escuelaing.ieti.jeebka.Models;

import java.util.List;

public class User {
    public String id;
    public String name;
    public String password;
    public String email;
    public List<Group> groups;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString(){
        return "User{ id: " + id + ", name: "+ name + ", password: "+ password + ", email: "+ email + ", groups: "+ groups.toString() +"}";
    }
}
