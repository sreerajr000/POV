package com.pazhankanjiz.pov;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.QuestionViewHolder> {
//
//    private Context context;
//    public CardStackAdapter(Context context) {
//        this.context = context;
//    }
//
    CardStackAdapter(List spots) {
        this.spots = spots;
    }
    private List<Spot> spots = Collections.emptyList();

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new QuestionViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        final Spot spot = spots.get(position);
        holder.name.setText("${spot.id}. ${spot.name}");
        holder.city.setText(spot.getCity());
        Glide.with(holder.image)
                .load(spot.getUrl())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ImageView image;
        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            city = itemView.findViewById(R.id.item_city);
            image = itemView.findViewById(R.id.item_image);
        }
    }
}
