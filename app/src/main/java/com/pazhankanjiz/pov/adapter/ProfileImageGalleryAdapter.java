package com.pazhankanjiz.pov.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pazhankanjiz.pov.R;

import java.util.Arrays;
import java.util.List;

public class ProfileImageGalleryAdapter extends RecyclerView.Adapter<ProfileImageGalleryAdapter.ProfileImageGalleryViewHolder> {

    private static final String TAG = "ImageGalleryAdapter";

    private Context mCtx;
    private View.OnClickListener mClickListener;

    public ProfileImageGalleryAdapter(Context mCtx, List imageUrls, View.OnClickListener callback) {
        this.mCtx = mCtx;
        this.mClickListener = callback;
        this.imageUrls = imageUrls;
    }

    private List<String> imageUrls;
    @NonNull
    @Override
    public ProfileImageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View view = inflater.inflate(R.layout.profile_image_gallery_item, parent, false);
        RecyclerView.ViewHolder viewHolder = new ProfileImageGalleryViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClick(view);
                Log.d(TAG, "onClick: " + view);
            }
        });
        return (ProfileImageGalleryViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageGalleryViewHolder holder, int position) {
        Glide.with(holder.profileImageItem)
                .load(imageUrls.get(position))
                .into(holder.profileImageItem);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    class ProfileImageGalleryViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImageItem;
        public ProfileImageGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.profileImageItem = itemView.findViewById(R.id.profile_image_item);
        }

    }
}
