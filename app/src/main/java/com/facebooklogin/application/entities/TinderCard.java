package com.facebooklogin.application.entities;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.APIClient;
import com.google.gson.JsonObject;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.facebooklogin.application.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Layout(R.layout.adapter_tinder_card)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    private Profile mProfile;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private String userId1;
    private String userId2;

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView, String userId1, String userId2) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(mProfile.getImageUrl()).into(profileImageView);
        nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
        locationNameTxt.setText(mProfile.getLocation());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut 1234");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("match_id1", userId1);
        jsonObject.addProperty("match_id2", userId2);
        jsonObject.addProperty("match", userId1);
        jsonObject.addProperty("like", "0");
        APIClient.getInstance().getMyApi().match(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Event ", "done");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn 1234");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("match_id1", userId1);
        jsonObject.addProperty("match_id2", userId2);
        jsonObject.addProperty("match", userId1);
        jsonObject.addProperty("like", "1");
        APIClient.getInstance().getMyApi().match(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JsonObject jsonObject1 = new JsonObject();
                jsonObject1.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                jsonObject1.addProperty("user_id1", userId1);
                jsonObject1.addProperty("user_id2", userId2);
                APIClient.getInstance().getMyApi().checkUserMatch(jsonObject1).enqueue(new Callback<message>() {
                    @Override
                    public void onResponse(Call<message> call, Response<message> response) {
                        message message1 = response.body();
                        if (message1.isStatus())
                        {
                            Toast.makeText(mContext, "It's a match", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<message> call, Throwable t) {
                    }
                });
                Log.v("Event ", "done");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState 123456");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState 123456");
    }
}