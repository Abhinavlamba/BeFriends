package com.facebooklogin.application.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.APIClient;
import com.facebooklogin.application.EditFragment;
import com.facebooklogin.application.ProfileActivity;
import com.facebooklogin.application.SettingsFragment;
import com.facebooklogin.application.SkillsFragment;
import com.facebooklogin.application.entities.Profile;
import com.facebooklogin.application.entities.user;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.jsibbold.zoomage.ZoomageView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.facebooklogin.application.R;
import com.facebooklogin.application.adapters.SliderAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {


    View rootLayout;
    boolean open = false;
    private SliderView sliderView;
    ZoomageView imageView;
    public static user user_details;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_account, container, false);
        rootLayout.setFocusableInTouchMode(true);
        rootLayout.requestFocus();
        rootLayout.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, SkillsFragment.newInstance("", "")).commit();
                    BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                    bnv.setSelectedItemId(R.id.fire);
                    return true;
                }
                return false;
            }
        } );
        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        sliderView = rootLayout.findViewById(R.id.slider_view);
        final SliderAdapter adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.startAutoCycle();
        LinearLayout settings = rootLayout.findViewById(R.id.settings);
        imageView = rootLayout.findViewById(R.id.preview_image);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.view_pager).setVisibility(View.GONE);
                getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
                getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.other_container, SettingsFragment.newInstance("",""))
                            .addToBackStack(null)
                            .commit();
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        String name_age = null;
        if (sharedPreferences.contains("username"))
            name_age = sharedPreferences.getString("username", "");
        TextView name = rootLayout.findViewById(R.id.name_age);
        name.setText(name_age);
        LinearLayout edit = rootLayout.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.view_pager).setVisibility(View.GONE);
                getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.other_container, EditFragment.newInstance("",""))
                        .addToBackStack(null)
                        .commit();
            }
        });
        onSaveInstanceState(rootLayout);

        return rootLayout;
    }

    private void onSaveInstanceState(View rootLayout) {
        String user_id = '(' + String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 59)) + ')';
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("user_id", user_id);
        Log.e("Responsejson1234", String.valueOf(jsonObject));
        CircleImageView circleImageView = rootLayout.findViewById(R.id.profile_image);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        Context context = getContext();
        APIClient.getInstance().getMyApi().getdetails(jsonObject).enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                List<user> users = response.body();
                user_details = users.get(0);
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putString("image0", user_details.getProfile_image());
                    editor.apply();
                    Glide.with(context).load("https://bitsphere.in/Brig/profile/" + user_details.getProfile_image()).into(circleImageView);
                    Glide.with(context).load("https://bitsphere.in/Brig/profile/" + user_details.getProfile_image()).into(imageView);
                    TextView job_info = rootLayout.findViewById(R.id.job_info);
                    job_info.setText(user_details.getJobInformation());
                    TextView education_info = rootLayout.findViewById(R.id.education_info);
                    education_info.setText(user_details.getEducation());
            }
            @Override
            public void onFailure(Call<List<user>> call, Throwable t) {
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ImageView back = rootLayout.findViewById(R.id.back_view);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    startActivity(intent);
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (open)
                    {
                        imageView.setVisibility(View.GONE);
                        back.setVisibility(View.GONE);
                        getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
                        open = false;
                    }
                }
            });

    }

}
