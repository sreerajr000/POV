package com.pazhankanjiz.pov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<SearchViewModel> searchViewModelList;

    public SearchAdapter(Context mCtx, List<SearchViewModel> searchViewModelList) {
        this.mCtx = mCtx;
        this.searchViewModelList = searchViewModelList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.search_item, null);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchViewModel searchViewModel = searchViewModelList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(searchViewModel.getTitle());
        holder.textViewShortDesc.setText(searchViewModel.getShortdesc());
        holder.textViewRating.setText(String.valueOf(searchViewModel.getRating()));
        holder.textViewPrice.setText(String.valueOf(searchViewModel.getPrice()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(searchViewModel.getImage()));
    }

    @Override
    public int getItemCount() {
        return searchViewModelList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public SearchViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
