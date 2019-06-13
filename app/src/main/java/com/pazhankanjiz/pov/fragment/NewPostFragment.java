package com.pazhankanjiz.pov.fragment;

import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.math.MathUtils;
import com.pazhankanjiz.pov.activity.MainActivity;
import com.pazhankanjiz.pov.model.NewPostViewModel;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.util.AnimationUtils;

import java.util.Arrays;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static java.lang.Math.min;

public class NewPostFragment extends Fragment {

    public static final String TAG = "NewPostFragment";

    private NewPostViewModel mViewModel;

    private EditText editText;

    private static final int MAX_CHAR_LIMIT = 255;
    private static final int MIN_TEXT_SIZE = 32;
    private static final int MAX_TEXT_SIZE = 42;

    private int backgroundIndex = 0;
    private int fontIndex = 0;

    private List backgroundList = Arrays.asList("https://source.unsplash.com/Xq1ntWruZQI/600x800",
            "https://source.unsplash.com/NYyCqdBOKwc/600x800",
            "https://source.unsplash.com/buF62ewDLcQ/600x800",
            "https://source.unsplash.com/Xq1ntWruZQI/600x800",
            "https://source.unsplash.com/NYyCqdBOKwc/600x800",
            "https://source.unsplash.com/buF62ewDLcQ/600x800");
    private List fontList = Arrays.asList();

    private FloatingActionButton sendButton, fontButton, backgroundButton;


    public static NewPostFragment newInstance() {
        return new NewPostFragment();
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

        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setTypeface(Typeface.SANS_SERIF);
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
                    editText.setSelection(prevText.length()-1);
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

}
