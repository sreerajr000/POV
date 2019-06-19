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
import com.pazhankanjiz.pov.model.ProfileQuestionModel;

import java.util.List;

import static com.pazhankanjiz.pov.constant.ResourceConstants.BACKGROUNDS;
import static com.pazhankanjiz.pov.constant.ResourceConstants.FONTS;

public class ProfileQuestionAdapter extends RecyclerView.Adapter<ProfileQuestionAdapter.ProfileQuestionViewHolder> {
    private List<ProfileQuestionModel> profileQuestionModelList;

    private Context mCtx;

    public ProfileQuestionAdapter(Context mCtx, List<ProfileQuestionModel> profileQuestionModels) {
        this.profileQuestionModelList = profileQuestionModels;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ProfileQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.profile_question_item, parent, false);
        return new ProfileQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileQuestionViewHolder holder, int position) {
        ProfileQuestionModel profileQuestionModel = profileQuestionModelList.get(position);
        Glide.with(holder.background)
                .load(BACKGROUNDS.get(profileQuestionModel.getImage()))
                .into(holder.background);
        String content = profileQuestionModel.getText().length() > 40 ?
                profileQuestionModel.getText().substring(0, 40) + "..." :
                profileQuestionModel.getText();
        holder.text.setText(content);
        holder.text.setTypeface(FONTS.get(profileQuestionModel.getFont()));
    }

    @Override
    public int getItemCount() {
        return profileQuestionModelList.size();
    }

    class ProfileQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView background;
        public ProfileQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.profile_question_text);
            background = itemView.findViewById(R.id.profile_question_background);
        }
    }

}
