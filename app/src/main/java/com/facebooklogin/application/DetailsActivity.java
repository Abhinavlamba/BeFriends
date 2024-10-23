package com.facebooklogin.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebooklogin.application.adapters.ImageAdapter;
import com.facebooklogin.application.entities.Images;
import com.facebooklogin.application.entities.user;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    ImageAdapter mViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView name, city;
        Bundle b = getIntent().getExtras();
        String userId = b.getString("user_id");
        name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
        ArrayList<String> images = new ArrayList<>();
        mViewPagerAdapter = new ImageAdapter(DetailsActivity.this, images, userId);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
        // Initializing the ViewPagerAdapter
        // Adding the Adapter to the ViewPager
        TextView lives_in, from, looking_for, industry, years_of_experience, education_level, heading;
        lives_in = findViewById(R.id.lives_in);
        from = findViewById(R.id.from);
        industry = findViewById(R.id.industry);
        years_of_experience = findViewById(R.id.years_of_experience);
        education_level = findViewById(R.id.education_level);
        heading = findViewById(R.id.heading);
        String user_id = '(' + userId + ')';
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject1.addProperty("user_id", user_id);
        Log.e("Responsejson1234", String.valueOf(jsonObject1));
        APIClient.getInstance().getMyApi().getdetails(jsonObject1).enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                List<user> users = response.body();
                user user_details = users.get(0);
                name.setText(user_details.getName());
                city.setText("");
                images.add(user_details.getProfile_image());
                heading.setText(user_details.getHeading());
                lives_in.setText(user_details.getLive_city());
                from.setText(user_details.getFrom_city());
                industry.setText(user_details.getIndustery());
                years_of_experience.setText(user_details.getExperience_year());
                education_level.setText(user_details.getEducation_level());
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                jsonObject.addProperty("user_id", userId);
                APIClient.getInstance().getMyApi().getImages(jsonObject).enqueue(new Callback<List<Images>>() {
                    @Override
                    public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                        Images images1 = response.body().get(0);
                        if (images1 == null)
                            return;
                        if (!images1.getImage1().equals(""))
                            images.add(images1.getImage1());
                        if (!images1.getImage2().equals(""))
                            images.add(images1.getImage2());
                        if (!images1.getImage3().equals(""))
                            images.add(images1.getImage3());
                        if (!images1.getImage4().equals(""))
                            images.add(images1.getImage4());
                        if (!images1.getImage5().equals(""))
                            images.add(images1.getImage5());
                        if (!images1.getImage6().equals(""))
                            images.add(images1.getImage6());
                        if (!images1.getImage7().equals(""))
                            images.add(images1.getImage7());
                        if (!images1.getImage8().equals(""))
                            images.add(images1.getImage8());
                        mViewPagerAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<List<Images>> call, Throwable t) {

                    }
                });
                mViewPagerAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(Call<List<user>> call, Throwable t) {
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mViewPagerAdapter.notifyDataSetChanged();
    }
}