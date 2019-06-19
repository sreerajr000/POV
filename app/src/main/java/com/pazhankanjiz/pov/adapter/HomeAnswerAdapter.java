package com.pazhankanjiz.pov.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.constant.ResourceConstants;
import com.pazhankanjiz.pov.model.AnswerModel;

import java.util.List;

public class HomeAnswerAdapter extends RecyclerView.Adapter<HomeAnswerAdapter.HomeAnswerViewHolder>{

    private List homeAnswerList;
    private Context mCtx;

    public HomeAnswerAdapter(List homeAnswerList, Context mCtx) {
        this.homeAnswerList = homeAnswerList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public HomeAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.home_answer_item, parent, false);
        return new HomeAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAnswerViewHolder holder, int position) {
        AnswerModel model = (AnswerModel) homeAnswerList.get(position);
        float ratio = 0.0f;
        if (model.getDislikeCount() + model.getLikeCount() != 0)
            ratio = model.getLikeCount() / (model.getDislikeCount() + model.getLikeCount());
        holder.ratio.setText(String.valueOf(ratio));

        holder.postedBy.setText(model.getPostedBy());
        holder.content.setText(model.getContent());
        holder.content.setTypeface(ResourceConstants.FONTS.get(model.getFont()));

        Glide.with(holder.background)
                .load(ResourceConstants.BACKGROUNDS.get(model.getBackground()))
                .into(holder.background);

    }

    @Override
    public int getItemCount() {
        return homeAnswerList.size();
    }

    class HomeAnswerViewHolder extends RecyclerView.ViewHolder {

        TextView postedBy, content, ratio;
        ImageView background;

        public HomeAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            postedBy = itemView.findViewById(R.id.answer_posted_by);
            content = itemView.findViewById(R.id.answer_content);
            ratio = itemView.findViewById(R.id.answer_rating);
            background =itemView.findViewById(R.id.answer_background);
        }
    }
}
