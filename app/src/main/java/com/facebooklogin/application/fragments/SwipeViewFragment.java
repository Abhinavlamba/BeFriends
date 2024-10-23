package com.facebooklogin.application.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.SkillsFragment;
import com.facebooklogin.application.adapters.ViewPagerAdapter;
import com.facebooklogin.application.entities.Match;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.facebooklogin.application.R;
import com.facebooklogin.application.Utils;
import com.facebooklogin.application.entities.Profile;
import com.facebooklogin.application.entities.TinderCard;

import org.json.JSONArray;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwipeViewFragment extends Fragment {


    private View rootLayout;
    private FloatingActionButton fabBack, fabLike, fabSkip, fabSkills, fabBoost;

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private ArrayList<Profile> profiles = new ArrayList<>();
    public SwipeViewFragment(ArrayList<Profile> profiles)
    {
        this.profiles = profiles;
    }
    public SwipeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_swipe_view, container, false);

        return rootLayout;
    }


    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeView = view.findViewById(R.id.swipeView);
        fabBack = view.findViewById(R.id.fabBack);
        fabLike = view.findViewById(R.id.fabLike);
        fabSkip = view.findViewById(R.id.fabSkip);
        fabSkills = view.findViewById(R.id.fabSkills);
        fabBoost = view.findViewById(R.id.fabBoost);
        mContext = getActivity();
        int bottomMargin = Utils.dpToPx(100);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        String url = "https://bitsphere.in/Brig/profile/" + getContext().getSharedPreferences("userpref",Context.MODE_PRIVATE).getString("image0", "");
        CircleImageView circleImageView = view.findViewById(R.id.image0);
        Glide.with(getContext()).load(url).into(circleImageView);
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        String user_id1 = String.valueOf(sharedPreferences.getInt("user_id", 60));
        for(Profile profile : profiles){
            if (!(String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 60)).equals(profile.getId()))) {
                mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView, user_id1, profile.getId()));
            }
        }

        fabSkip.setOnClickListener(v -> {
            animateFab(fabSkip);
            mSwipeView.doSwipe(false);
        });

        fabLike.setOnClickListener(v -> {
            animateFab(fabLike);
            mSwipeView.doSwipe(true);
        });

        fabBoost.setOnClickListener(v -> animateFab(fabBoost));
        fabSkills.setOnClickListener(v -> {
            animateFab(fabSkills);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, SkillsFragment.newInstance("","")).commit();
        });
        fabBack.setOnClickListener(v -> animateFab(fabBack));
    }


    private void animateFab(final FloatingActionButton fab){
        fab.animate().scaleX(0.7f).setDuration(100).withEndAction(() -> fab.animate().scaleX(1f).scaleY(1f));
    }

}
