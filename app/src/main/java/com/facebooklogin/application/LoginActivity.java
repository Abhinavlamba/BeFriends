package com.facebooklogin.application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebooklogin.application.entities.message;
import com.facebooklogin.application.entities.user;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.facebooklogin.application.entities.user;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private ProgressBar progressBar;
    private TextView mobile, facebook;
    private int RC_SIGN_IN = 200;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        facebook = findViewById(R.id.facebook);
        loginButton.setReadPermissions(Arrays.asList("email"));
        mobile = findViewById(R.id.mobile);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });
        mobile.setOnClickListener(this);
        facebook.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        loginButton.setVisibility(View.GONE);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (requestCode == RC_SIGN_IN) {

            }
            else {
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
    private void updateToken(JsonObject jsonObject)
    {
        APIClient.getInstance().getMyApi().checkUser(jsonObject).enqueue(new Callback<PhoneApi>() {
            @Override
            public void onResponse(Call<PhoneApi> call, Response<PhoneApi> response) {
            }
            @Override
            public void onFailure(Call<PhoneApi> call, Throwable t) {

            }
        });
    }
    private void goMainScreen() {
        String authToken = user.getUid();
        FirebaseUser mUser = user;
        if (mUser.getPhoneNumber() != null && mUser.getPhoneNumber().length() > 0) {
            getSharedPreferences("userpref", MODE_PRIVATE).edit().putString("phone_no", mUser.getPhoneNumber()).apply();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("api_key", "");
            jsonObject.addProperty("token_key", authToken);
            jsonObject.addProperty("email", mUser.getPhoneNumber());
            updateToken(jsonObject);
        }
        else if (mUser.getEmail() != null && mUser.getEmail().length() > 0)
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("api_key", "");
            jsonObject.addProperty("token_key", authToken);
            jsonObject.addProperty("email", mUser.getEmail());
            updateToken(jsonObject);
        }
        getSharedPreferences("userpref", MODE_PRIVATE).edit().putString("auth", authToken).apply();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("token_key", authToken);
        APIClient.getInstance().getMyApi().checkUser(jsonObject).enqueue(new Callback<PhoneApi>() {
            @Override
            public void onResponse(Call<PhoneApi> call, Response<PhoneApi> response) {
                PhoneApi phoneApi = response.body();
                if (!(String.valueOf(phoneApi.getUserId()).equals("-1")))
                {
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                    jsonObject1.addProperty("user_id", "(" + String.valueOf(phoneApi.getUserId())+")" );
                    APIClient.getInstance().getMyApi().getdetails(jsonObject1).enqueue(new Callback<List<user>>() {
                        @Override
                        public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                            com.facebooklogin.application.entities.user user1 = response.body().get(0);
                            SharedPreferences sharedPreferences = getSharedPreferences("userpref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("old_name", user1.getName());
                            editor.putString("old_email", user1.getEmail())
                                    .putString("old_image", user1.getProfile_image())
                                    .putString("old_dob", user1.getDob())
                                    .putString("old_skills", user1.getSkill_id())
                                    .putString("old_gender", user1.getGender())
                                    .apply();
                        }

                        @Override
                        public void onFailure(Call<List<user>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PhoneApi> call, Throwable t) {

            }
        });
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Login", "Yes");
        SharedPreferences.Editor editor = getSharedPreferences("userpref", MODE_PRIVATE).edit();
        editor.putString("username", user.getDisplayName());
        editor.putString("useremail", user.getEmail());
        editor.apply();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mobile:
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders( Arrays.asList(
                                        new AuthUI.IdpConfig.PhoneBuilder().build()))
                                .setTheme(R.style.LoginTheme)
                                .build(),
                        RC_SIGN_IN);
                break;
            case R.id.facebook:
                loginButton.performClick();
                break;
        }
    }
}