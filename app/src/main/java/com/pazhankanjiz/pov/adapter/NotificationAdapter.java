package com.pazhankanjiz.pov.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.model.NotificationViewModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationViewModel> notificationViewModelList;

    private Context mCtx;

    public NotificationAdapter(Context mCtx, List<NotificationViewModel> notificationViewModelList) {
        this.notificationViewModelList = notificationViewModelList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationViewModel notificationViewModel = notificationViewModelList.get(position);
        holder.image.setImageResource(notificationViewModel.getImage());
        holder.text.setText(notificationViewModel.getNotificationText());

        /*

        //Animate text TODO remove
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(holder.background,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.start();
        */
    }

    @Override
    public int getItemCount() {
        return notificationViewModelList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.notification_text);
            image = itemView.findViewById(R.id.notification_image);
        }
    }
}
