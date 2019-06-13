package com.pazhankanjiz.pov.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pazhankanjiz.pov.R;
import com.pazhankanjiz.pov.adapter.ProfileImageGalleryAdapter;

import java.util.Arrays;
import java.util.List;

import static com.pazhankanjiz.pov.constant.UserInfoConstants.ACCOUNT_ID;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.BACKGROUND;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.PHONE_NUMBER_STRING;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USERNAME;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_INFO;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_LOGGED_IN;

public class UserCreationActivity extends AppCompatActivity {

    private static final String TAG = "UserCreationActivity";

    private TextInputEditText userName, userBio;
    private ContentLoadingProgressBar progressBar;
    private RoundedImageView profileImage;
    private Button nextButton;
    private ImageButton profileImageButton;
    private RecyclerView profileImageGallery;

    private int backgroundIndex = -1;

    private ProgressDialog progressDialog;
    private List<String> imageUrls = Arrays.asList("https://source.unsplash.com/Xq1ntWruZQI/600x800",
            "https://source.unsplash.com/NYyCqdBOKwc/600x800",
            "https://source.unsplash.com/buF62ewDLcQ/600x800",
            "https://source.unsplash.com/Xq1ntWruZQI/600x800",
            "https://source.unsplash.com/NYyCqdBOKwc/600x800",
            "https://source.unsplash.com/buF62ewDLcQ/600x800");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        userName = findViewById(R.id.username_edit_text);
        userBio = findViewById(R.id.bio_edit_text);
        profileImage = findViewById(R.id.new_user_image);
        progressBar = findViewById(R.id.user_progress);
        nextButton = findViewById(R.id.user_create_next_button);
        profileImageButton = findViewById(R.id.user_image_edit);
        ProgressChangeListener progressChangeListener = new ProgressChangeListener();
        userName.addTextChangedListener(progressChangeListener);
        userBio.addTextChangedListener(progressChangeListener);
        profileImageGallery = findViewById(R.id.profile_image_gallery);

        findViewById(R.id.user_creation_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileImageGallery.getVisibility() == View.VISIBLE) {
                    Animation fadeOut = AnimationUtils.loadAnimation(UserCreationActivity.this, R.anim.fade_out_down);
                    profileImageGallery.startAnimation(fadeOut);
                    profileImageGallery.setVisibility(View.GONE);
                }
            }
        });

        profileImageGallery.setAdapter(new ProfileImageGalleryAdapter(UserCreationActivity.this, imageUrls, new View.OnClickListener() {
            View prevSelectedImage = null;
            @Override
            public void onClick(View v) {
                int position = profileImageGallery.indexOfChild(v);
                v.setBackgroundColor(getResources().getColor(R.color.selection));
                if (null != prevSelectedImage) {
                    prevSelectedImage.setBackgroundColor(getResources().getColor(R.color.background_black));
                }
                prevSelectedImage = v;
                Log.d(TAG, "onClick: " + position);
                Glide.with(profileImage)
                        .load(imageUrls.get(position))
                        .into(profileImage);
                backgroundIndex = position;
                updateProgress();
            }
        }));
        profileImageGallery.setLayoutManager(new LinearLayoutManager(UserCreationActivity.this, RecyclerView.HORIZONTAL, false));

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileImageGallery.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(UserCreationActivity.this, R.anim.fade_in_down);
                profileImageGallery.startAnimation(fadeIn);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret = false;
                if (TextUtils.isEmpty(userName.getText())) {
                    userName.setError("This field cannot be blank!");
                    ret = true;
                }
                if (TextUtils.isEmpty(userBio.getText())) {
                    userBio.setError("This field cannot be blank!");
                    ret = true;
                }
                if (null == profileImage.getDrawable()) {
                    new AlertDialog.Builder(UserCreationActivity.this)
                            .setMessage("Profile Image cannot be blank")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    ret = true;
                }

                if (ret) return;
                progressDialog = ProgressDialog.show(UserCreationActivity.this, "","Please Wait ...");
                saveUserInfo();
            }
        });
    }

    private class ProgressChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateProgress();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    @SuppressLint("NewApi")
    private void updateProgress() {
        int completed = 0;
        if (userName.getText().length() > 0) {
            completed++;
        }
        if (userBio.getText().length() > 0) {
            completed++;
        }
        if (backgroundIndex > -1) {
            completed++;
        }

        progressBar.setProgress(completed * 100 / 3, true);
    }

    public void saveUserInfo() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {

                Log.d(TAG, "onSuccess: Entered onSuccess saveUserInfo");
                // Get Account Kit ID
                final String accountKitId = account.getId();

                // Get phone number
                final PhoneNumber phoneNumber = account.getPhoneNumber();
                if (phoneNumber != null) {
                    String phoneNumberString = phoneNumber.toString();
                    SharedPreferences.Editor editor = getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
                    editor.putString(ACCOUNT_ID, accountKitId);
                    editor.putString(PHONE_NUMBER_STRING, phoneNumberString);
                    editor.putBoolean(USER_LOGGED_IN, true);
                    editor.apply();
                }

                //Save it in parse server TODO do it after user details are entered
                ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("User");

                userQuery = userQuery.whereEqualTo(PHONE_NUMBER_STRING, phoneNumber.toString());
                userQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.isEmpty()) {
                            ParseObject userObject = new ParseObject("User");
                            userObject.put(ACCOUNT_ID, accountKitId);
                            userObject.put(PHONE_NUMBER_STRING, phoneNumber.toString());
                            userObject.put(USERNAME, userName.getText().toString());
                            userObject.put(BACKGROUND, backgroundIndex);
                            userObject.saveInBackground();
                        } else {
                            Log.d(TAG, "onSuccess: " + objects);
                        }
                        progressDialog.dismiss();
                        Intent intent = new Intent(UserCreationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
            }
        });

    }
}
