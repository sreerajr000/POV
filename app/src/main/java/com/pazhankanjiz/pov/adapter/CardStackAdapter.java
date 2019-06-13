package com.pazhankanjiz.pov.adapter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.model.Spot;

import java.util.Collections;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.QuestionViewHolder> {

    private Context mCtx;
//    public CardStackAdapter(Context context) {
//        this.context = context;
//    }
//
    public CardStackAdapter(Context context, List spots) {
        this.spots = spots;
        this.mCtx = context;
    }
    private List<Spot> spots = Collections.emptyList();

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new QuestionViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder holder, int position) {
        final Spot spot = spots.get(position);
        holder.name.setText(spot.getId()+". " + spot.getName());
        holder.city.setText(spot.getCity());
        Glide.with(holder.image)
                .load(spot.getUrl())
                .into(holder.image);
        final View w = holder.cardView;
        final float cameraDistance = w.getCameraDistance();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.animate().withLayer()
                        .rotationY(90)
                        .setDuration(300)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                float scale = mCtx.getResources().getDisplayMetrics().density;
                                float distance = w.getCameraDistance() * (scale + scale/3);
                                w.setCameraDistance(distance);
                                if(holder.flipped) {
                                    Glide.with(holder.image)
                                            .load(spot.getUrl())
                                            .into(holder.image);
                                    holder.flipped = false;
                                } else {
                                    holder.image.setImageResource(R.drawable.logo_pov);
                                    holder.flipped = true;
                                }

                                w.setRotationY(-90);
                                w.animate().withLayer()
                                        .rotationY(0)
                                        .setDuration(300)
                                        .withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                w.setCameraDistance(cameraDistance);
                                            }
                                        })
                                        .start();
                            }
                        });


                Toast.makeText(v.getContext(), spot.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView name, city;
        View cardView;
        ImageView image;
        boolean flipped = false;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            city = itemView.findViewById(R.id.item_city);
            image = itemView.findViewById(R.id.item_image);
            cardView = itemView.findViewById(R.id.card_view_home);
        }
    }
}
