package com.pazhankanjiz.pov.model;

import androidx.lifecycle.ViewModel;

public class SearchResultsModel extends ViewModel {
    // TODO: Implement the ViewModel
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private double price;
    private int image;

    public SearchResultsModel(int id, String title, String shortdesc, double rating, double price, int image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
