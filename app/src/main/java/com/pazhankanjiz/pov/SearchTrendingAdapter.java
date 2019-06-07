package com.pazhankanjiz.pov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchTrendingAdapter extends RecyclerView.Adapter<SearchTrendingAdapter.SearchTrendingViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;

    private List<SearchTrendingModel> searchTrendingModels;

    public SearchTrendingAdapter(Context mCtx, List<SearchTrendingModel> searchTrendingModels) {
        this.mCtx = mCtx;
        this.searchTrendingModels = searchTrendingModels;
    }

    @NonNull
    @Override
    public SearchTrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_trending, parent, false);
        return new SearchTrendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTrendingViewHolder holder, int position) {
        SearchTrendingModel searchTrendingModel = searchTrendingModels.get(position);

        SearchTrendingItemAdapter searchTrendingItemAdapter = new SearchTrendingItemAdapter(mCtx, searchTrendingModel.getSearchTrendingItemModels());

        holder.sliderRecyclerView.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false));
        holder.sliderRecyclerView.setAdapter(searchTrendingItemAdapter);
        holder.textViewTag.setText(searchTrendingModel.getTag());
    }

    @Override
    public int getItemCount() {
        return searchTrendingModels.size();
    }

    class SearchTrendingViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTag;
        RecyclerView sliderRecyclerView;

        public SearchTrendingViewHolder(View itemView) {
            super(itemView);

            textViewTag = itemView.findViewById(R.id.tag_text_view);
            sliderRecyclerView = itemView.findViewById(R.id.slider_recycler_view);
        }
    }
}
