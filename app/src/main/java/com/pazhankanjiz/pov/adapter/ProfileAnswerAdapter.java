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
import com.pazhankanjiz.pov.model.ProfileAnswerModel;

import java.util.List;

public class ProfileAnswerAdapter extends RecyclerView.Adapter<ProfileAnswerAdapter.ProfileAnswerViewHolder>{

    private List<ProfileAnswerModel> profileAnswerModelList;

    private Context mCtx;

    public ProfileAnswerAdapter(Context mCtx, List<ProfileAnswerModel> profileAnswerModelList) {
        this.profileAnswerModelList = profileAnswerModelList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ProfileAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.profile_answer_item, parent, false);
        return new ProfileAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAnswerViewHolder holder, int position) {
        ProfileAnswerModel profileAnswerModel = profileAnswerModelList.get(position);
        holder.image.setImageResource(profileAnswerModel.getImage());
        holder.text.setText(profileAnswerModel.getText());
    }

    @Override
    public int getItemCount() {
        return profileAnswerModelList.size();
    }

    class ProfileAnswerViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView image;

        public ProfileAnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.profile_answer_text);
            image = itemView.findViewById(R.id.profile_answer_image);
        }
    }
}
