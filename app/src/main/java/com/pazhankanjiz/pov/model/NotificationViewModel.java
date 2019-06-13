package com.pazhankanjiz.pov.model;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {
    private int image;
    private String notificationText;


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public NotificationViewModel(int image, String notificationText) {
        this.image = image;
        this.notificationText = notificationText;
    }
}
