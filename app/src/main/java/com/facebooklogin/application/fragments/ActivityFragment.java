package com.facebooklogin.application.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebooklogin.application.R;
import com.facebooklogin.application.SkillsFragment;
import com.facebooklogin.application.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {


    private Context mContext;
    private ViewPager viewPager;
    private View rootLayout;
    private TextView chatText, feedText;
    private LinearLayout chatLayout, feedLayout;

    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_activity, container, false);
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
        mContext = getContext();
        chatLayout = rootLayout.findViewById(R.id.layout_chat);
        feedLayout = rootLayout.findViewById(R.id.layout_feed);
        chatText = rootLayout.findViewById(R.id.text_chat);
        feedText = rootLayout.findViewById(R.id.text_feed);

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new ChatFragment());
        fragList.add(new FeedFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(fragList, getActivity().getSupportFragmentManager());
        viewPager = rootLayout.findViewById(R.id.view_pager_frag);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(this);
        chatLayout.setOnClickListener(this);
        feedLayout.setOnClickListener(this);

        return rootLayout;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_chat:
                viewPager.setCurrentItem(0);
                chatText.setTextColor(getResources().getColor(R.color.colorPrimary));
                feedText.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case R.id.layout_feed:
                viewPager.setCurrentItem(1);
                chatText.setTextColor(getResources().getColor(R.color.light_gray));
                feedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                chatText.setTextColor(getResources().getColor(R.color.colorPrimary));
                feedText.setTextColor(getResources().getColor(R.color.light_gray));
                break;
            case 1:
                chatText.setTextColor(getResources().getColor(R.color.light_gray));
                feedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
