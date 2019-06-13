package com.pazhankanjiz.pov.model;

public class SearchTrendingItemModel {
    private String url;
    private String title;

    public SearchTrendingItemModel(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
