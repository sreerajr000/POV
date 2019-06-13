package com.pazhankanjiz.pov.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pazhankanjiz.pov.R;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.SSLContext;

import static com.pazhankanjiz.pov.constant.UserInfoConstants.ACCOUNT_ID;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.PARSE_APPLICATION_ID;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.PARSE_SERVER;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.PHONE_NUMBER_STRING;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_INFO;
import static com.pazhankanjiz.pov.constant.UserInfoConstants.USER_LOGGED_IN;

public class SplashActivity extends AppCompatActivity {

    private static final Integer DELAY = 3000;

    public static final String TAG = "SplashActivity";

    private ImageView logo;

    public static int APP_REQUEST_CODE = 99;



    public void logOut() {
        AccountKit.logOut();
        SharedPreferences.Editor editor = getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();
        editor.putString(ACCOUNT_ID, "");
        editor.putString(PHONE_NUMBER_STRING, "");
        editor.putBoolean(USER_LOGGED_IN, false);
        editor.apply();
    }

    public void phoneLogin(/*final View view*/) {
        final Intent intent = new Intent(SplashActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
//                showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
//                saveUserInfo();
                Intent i = new Intent(SplashActivity.this, UserCreationActivity.class);
                startActivity(i);
                finish();
            }

            // Surface the result to your user in an appropriate way.
//            Toast.makeText(
//                    this,
//                    toastMessage,
//                    Toast.LENGTH_LONG)
//                    .show();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATION_ID).server(PARSE_SERVER).build());

        logo = findViewById(R.id.splash_screen_logo);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(logo,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1.1f));
        scaleDown.setDuration(DELAY / 4);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleDown.start();

        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        SharedPreferences.Editor editor = getSharedPreferences(USER_INFO, MODE_PRIVATE).edit();

        if (accessToken != null && getSharedPreferences(USER_INFO, MODE_PRIVATE).getBoolean(USER_LOGGED_IN, false)) {
            //Handle Returning User
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, DELAY / 2);

        } else {
            phoneLogin();
        }

    }
}
