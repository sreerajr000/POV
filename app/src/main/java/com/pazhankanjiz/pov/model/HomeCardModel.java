package com.pazhankanjiz.pov.model;

import java.util.ArrayList;
import java.util.List;

public class HomeCardModel {
    private String content, id, postedBy;
    private int rating, likes, dislikes, background, font;
    private List<String> hashTags;

    @Override
    public String toString() {
        return "HomeCardModel{" +
                "content='" + content + '\'' +
                '}';
    }

    public HomeCardModel(String content, String id, String postedBy) {
        this.content = content;
        this.id = id;
        this.postedBy = postedBy;
        this.hashTags = new ArrayList<>();
    }

    public HomeCardModel(String content, String id, String postedBy, int rating, int likes, int dislikes, int background, int font, List hashTags) {
        this.content = content;
        this.hashTags = hashTags;
        this.id = id;
        this.postedBy = postedBy;
        this.rating = rating;
        this.likes = likes;
        this.dislikes = dislikes;
        this.background = background;
        this.font = font;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }
}
