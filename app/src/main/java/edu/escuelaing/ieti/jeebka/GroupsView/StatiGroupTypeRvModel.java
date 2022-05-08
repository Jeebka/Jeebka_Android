package edu.escuelaing.ieti.jeebka.GroupsView;

public class StatiGroupTypeRvModel {

    private String text;
    private int image;

    public StatiGroupTypeRvModel(int image, String text) {
        this.image = image;
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}
