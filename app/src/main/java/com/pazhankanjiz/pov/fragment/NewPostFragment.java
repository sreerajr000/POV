package com.pazhankanjiz.pov.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.pazhankanjiz.pov.activity.MainActivity;
import com.pazhankanjiz.pov.activity.UserCreationActivity;
import com.pazhankanjiz.pov.model.HomeCardModel;
import com.pazhankanjiz.pov.model.NewPostViewModel;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.util.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.ANSWERS;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.BACKGROUND;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.CLASS_QUESTION;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.CONTENT;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.DISLIKE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.FONT;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.HASHTAG;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.ID;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.LIKE;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.POSTED_BY;
import static com.pazhankanjiz.pov.constant.DatabaseConstants.RATING;
import static com.pazhankanjiz.pov.constant.FragmentConstants.FRAGMENT_HOME;
import static com.pazhankanjiz.pov.constant.ResourceConstants.BACKGROUNDS;
import static com.pazhankanjiz.pov.constant.ResourceConstants.FONTS;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_INFO;
import static java.lang.Math.min;

public class NewPostFragment extends Fragment {

    public static final String TAG = "NewPostFragment";

    private NewPostViewModel mViewModel;

    private EditText editText;

    private ImageView background;

    private ViewPager viewPager;


    private static final int MAX_CHAR_LIMIT = 255;
    private static final int MIN_TEXT_SIZE = 32;
    private static final int MAX_TEXT_SIZE = 42;

    private int backgroundIndex = 0;
    private int fontIndex = 0;

    private FloatingActionButton sendButton, fontButton, backgroundButton;


    public NewPostFragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendButton = view.findViewById(R.id.newpost_create_fab);
        fontButton = view.findViewById(R.id.newpost_font_fab);
        backgroundButton = view.findViewById(R.id.newpost_bg_fab);
        editText = view.findViewById(R.id.newpost_edit_text);
        background = view.findViewById(R.id.new_post_background);

        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundIndex = ++backgroundIndex % BACKGROUNDS.size();
                Glide.with(background)
                        .load(BACKGROUNDS.get(backgroundIndex))
                        .into(background);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                createNewPost();
            }
        });

        fontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fontIndex = (++fontIndex) % FONTS.size();
                editText.setTypeface(FONTS.get(fontIndex));
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            String prevText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevText = editText.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer textSizeBasedOnLineCount = AnimationUtils.map(editText.getLineCount(), 0, 16, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
                Integer textSize = AnimationUtils.map(editText.getText().length(), 0, MAX_CHAR_LIMIT, MAX_TEXT_SIZE, MIN_TEXT_SIZE);
                editText.setTextSize(min(textSize, textSizeBasedOnLineCount));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getLineCount() > 16) {
                    editText.setText(prevText);
                    editText.setSelection(prevText.length() - 1);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewPostViewModel.class);
        // TODO: Use the ViewModel
    }


    private void createNewPost() {
        final ParseObject newPost = new ParseObject(CLASS_QUESTION);
        newPost.put(CONTENT, editText.getText().toString());
        String userId = this.getActivity().getSharedPreferences(USER_INFO, MODE_PRIVATE).getString(ID, null);
        newPost.put(POSTED_BY, userId);
        newPost.put(BACKGROUND, backgroundIndex);
        newPost.put(LIKE, 0);
        newPost.put(FONT, fontIndex);
        newPost.put(DISLIKE, 0);
        newPost.put(RATING, 0);
        newPost.put(HASHTAG, getHashtags(editText.getText().toString()));
        newPost.put(ANSWERS, new ArrayList<>());

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Please Wait ...");

        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                progressDialog.dismiss();
                if (null != e) {
                    e.printStackTrace();
                } else {
                    HomeCardModel newItem = new HomeCardModel(newPost.getString(CONTENT), newPost.getObjectId(),
                            newPost.getString(POSTED_BY), newPost.getInt(RATING), newPost.getInt(LIKE),
                            newPost.getInt(DISLIKE), newPost.getInt(BACKGROUND), newPost.getInt(FONT), newPost.getList(HASHTAG));
                    viewPager.setCurrentItem(0);
                    ((MainActivity) getActivity()).addFirstHome(1, newItem);

                }
            }
        });


    }

    private List<String> getHashtags(String text) {
        List<String> hashtags = new ArrayList<>();

        String regexPattern = "(#\\w+)";

        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String hashtag = m.group(1);
            // Add hashtag to ArrayList
            hashtags.add(hashtag.substring(1));
        }
        return hashtags;
    }


}
