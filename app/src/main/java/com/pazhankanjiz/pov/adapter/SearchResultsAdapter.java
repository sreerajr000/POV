package com.pazhankanjiz.pov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.model.SearchResultsModel;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SearchResultsModel> searchResultsModelList;

    public SearchResultsAdapter(Context mCtx, List<SearchResultsModel> searchResultsModelList) {
        this.mCtx = mCtx;
        this.searchResultsModelList = searchResultsModelList;
    }

    @NonNull
    @Override
    public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_results_item, null);
        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsViewHolder holder, int position) {
        SearchResultsModel searchResultsModel = searchResultsModelList.get(position);

        //binding the data with the viewholder views
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(searchResultsModel.getImage()));
    }

    @Override
    public int getItemCount() {
        return searchResultsModelList.size();
    }

    class SearchResultsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public SearchResultsViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_result_image);
        }
    }
}
