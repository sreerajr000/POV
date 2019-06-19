package com.pazhankanjiz.pov.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.model.AnswerModel;
import com.pazhankanjiz.pov.model.HomeCardModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.pazhankanjiz.pov.constant.ResourceConstants.BACKGROUNDS;
import static com.pazhankanjiz.pov.constant.ResourceConstants.FONTS;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.QuestionViewHolder> {

    private Context mCtx;

    private View homeHeader, buttonContainer;

    public CardStackAdapter(Context context, List spots, View homeHeader, View buttonContainer) {
        this.homeCardModels = spots;
        this.mCtx = context;
        this.homeHeader = homeHeader;
        this.buttonContainer = buttonContainer;
    }

    private List<HomeCardModel> homeCardModels = Collections.emptyList();

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new QuestionViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final QuestionViewHolder holder, int position) {
        final HomeCardModel homeCardModel = homeCardModels.get(position);
        final Object background = BACKGROUNDS.get(homeCardModel.getBackground());
        Typeface font = FONTS.get(homeCardModel.getFont());
        holder.content.setText(homeCardModel.getContent());
        holder.content.setTypeface(font);
        holder.postedBy.setText(homeCardModel.getPostedBy());
        holder.contentFlipped.setText(homeCardModel.getContent());
        holder.postedByFlipped.setText(homeCardModel.getPostedBy());

        AnswerModel model = new AnswerModel("THis is us", "lfldnf","klnlfsf",0L,0L,0,0,0);
        holder.answerView.setAdapter(new HomeAnswerAdapter(Arrays.asList(model), mCtx));
        holder.answerView.setLayoutManager(new LinearLayoutManager(mCtx, RecyclerView.HORIZONTAL, false));

        LayoutInflater li = LayoutInflater.from(mCtx);

        for (String hashtag : homeCardModel.getHashTags()) {
            Chip chip = (Chip)li.inflate(R.layout.item_chip, null);
            chip.setSelected(true);
            chip.setText(hashtag);
            holder.chipView.addView(chip);
        }

        float ratio = 0.0f;
        if (homeCardModel.getDislikes() + homeCardModel.getLikes() != 0)
            ratio = homeCardModel.getLikes() / (homeCardModel.getDislikes() + homeCardModel.getLikes());
        holder.ratio.setText(String.valueOf(ratio));

        Glide.with(holder.image)
                .load(background)
                .into(holder.image);
    }

    public List<HomeCardModel> getHomeCardModels() {
        return homeCardModels;
    }

    public void setHomeCardModels(List<HomeCardModel> homeCardModels) {
        this.homeCardModels = homeCardModels;
    }

    @Override
    public int getItemCount() {
        return homeCardModels.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView content, postedBy, contentFlipped, postedByFlipped, ratio;
        View cardView;
        ImageView image;
        ChipGroup chipView;
        RecyclerView answerView;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.item_content);
            postedBy = itemView.findViewById(R.id.item_postedby);
            image = itemView.findViewById(R.id.item_image);
            cardView = itemView.findViewById(R.id.card_view_home);
            contentFlipped = itemView.findViewById(R.id.content_flipped);
            postedByFlipped = itemView.findViewById(R.id.posted_by_flipped);
            ratio = itemView.findViewById(R.id.ratio);
            chipView = itemView.findViewById(R.id.chip_layout);
            answerView = itemView.findViewById(R.id.item_answers_recycle_view);
        }
    }
}
