package com.pazhankanjiz.pov.model;

public class ProfileQuestionModel {
    private int image;
    private int font;
    private String text;

    public ProfileQuestionModel(int image, String text, int font) {
        this.image = image;
        this.text = text;
        this.font = font;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
