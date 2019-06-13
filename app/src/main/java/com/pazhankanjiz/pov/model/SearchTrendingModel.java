package com.pazhankanjiz.pov.model;

import java.util.List;

public class SearchTrendingModel {
    private String tag;
    List<SearchTrendingItemModel> searchTrendingItemModels;

    public SearchTrendingModel(String tag, List<SearchTrendingItemModel> searchTrendingItemModels) {
        this.tag = tag;
        this.searchTrendingItemModels = searchTrendingItemModels;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<SearchTrendingItemModel> getSearchTrendingItemModels() {
        return searchTrendingItemModels;
    }

    public void setSearchTrendingItemModels(List<SearchTrendingItemModel> searchTrendingItemModels) {
        this.searchTrendingItemModels = searchTrendingItemModels;
    }
}
