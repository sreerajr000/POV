package com.pazhankanjiz.pov.model;

public class AnswerModel {
    private String content;
    private String id;
    private String postedBy;
    private Long likeCount;
    private Long dislikeCount;
    private Integer rating;
    private Integer font;
    private Integer background;

    public Integer getFont() {
        return font;
    }

    public void setFont(Integer font) {
        this.font = font;
    }

    public Integer getBackground() {
        return background;
    }

    public void setBackground(Integer background) {
        this.background = background;
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

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(Long dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public AnswerModel(String content, String id, String postedBy, Long likeCount, Long dislikeCount, Integer rating, Integer font, Integer background) {
        this.content = content;
        this.id = id;
        this.postedBy = postedBy;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.rating = rating;
        this.font = font;
        this.background = background;
    }
}
