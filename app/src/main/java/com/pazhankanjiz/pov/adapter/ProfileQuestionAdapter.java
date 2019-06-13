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
import com.pazhankanjiz.pov.model.ProfileQuestionModel;

import java.util.List;

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
        holder.image.setImageResource(profileQuestionModel.getImage());
        holder.text.setText(profileQuestionModel.getText());
    }

    @Override
    public int getItemCount() {
        return profileQuestionModelList.size();
    }

    class ProfileQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;
        public ProfileQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.profile_question_text);
            image = itemView.findViewById(R.id.profile_question_image);
        }
    }

}
