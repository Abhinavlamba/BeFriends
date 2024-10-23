package com.facebooklogin.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebooklogin.application.adapters.ListAdapter;
import com.facebooklogin.application.adapters.ViewPagerAdapter;
import com.facebooklogin.application.entities.Item;
import com.facebooklogin.application.entities.Profile;
import com.facebooklogin.application.entities.user;
import com.facebooklogin.application.fragments.AccountFragment;
import com.facebooklogin.application.fragments.ActivityFragment;
import com.facebooklogin.application.fragments.SwipeViewFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SkillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillsFragment extends Fragment implements onItemClick {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Profile> profiles = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HashSet<Integer> ids = new HashSet<>();
    public SkillsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkillsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SkillsFragment newInstance(String param1, String param2) {
        SkillsFragment fragment = new SkillsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AndroidNetworking.initialize(getContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        View view = inflater.inflate(R.layout.fragment_skills, container, false);
        RangeSeekBar seekBar = view.findViewById(R.id.rangeSeekBar);
        RelativeLayout distance = view.findViewById(R.id.distance_layout);
        TextView min_distance = view.findViewById(R.id.min_distance);
        TextView max_distance = view.findViewById(R.id.max_distance);
        TextView heading = view.findViewById(R.id.heading0);
        if (getArguments().getString(ARG_PARAM1).equals("No seek"))
        {

            distance.setVisibility(View.GONE);
            heading.setText("Select your skills");
        }
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) {
                min_distance.setText(String.valueOf(progressStart));
                max_distance.setText(String.valueOf(progressEnd));
                seekBar.setProgress(0, progressEnd);
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) { }
        });
        List<Item> list = new ArrayList<>();
        ListView listView = view.findViewById(R.id.listview);
        ListAdapter listAdapter = new ListAdapter(getContext(), R.layout.item_list_row, list, this);
        listView.setAdapter(listAdapter);
        SharedPreferences p = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        String skills = p.getString("old_skills", "");
        String [] skills_id = skills.split(",");
        APIClient.getInstance().getMyApi().getSkills().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> list1 = response.body();
                Log.v("Itemslist", String.valueOf(list1));
                for (int i = 0; i < list1.size(); i++)
                {
                    Item item = new Item();
                    item.setId(list1.get(i).getId());
                    item.setSkills(list1.get(i).getSkills());
                    int o = 0;
                    for (o = 0; o < skills_id.length; o++)
                    {
                        if (skills_id[o].equals(item.getId()))
                            break;
                    }
                    if (heading.getText().equals("Select your skills")) {
                        if (o == skills_id.length)
                            item.setChecked(false);
                        else
                            item.setChecked(true);
                    }
                    else
                    {
                        item.setChecked(false);
                        ids.clear();
                    }
                        Log.v("Skills1234", list1.get(i).getSkills());
                        list.add(item);
                }
                listAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
            }
        });
        listAdapter.notifyDataSetChanged();
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getArguments().getString(ARG_PARAM1).equals("No seek")) {
                    JsonArray arrayList = new JsonArray();
                    Iterator<Integer> itr = ids.iterator();
                    while (itr.hasNext()) {
                        arrayList.add(itr.next());
                    }
                    String skills_list = String.valueOf(arrayList);
                    skills_list = skills_list.replace('[', '(');
                    skills_list = skills_list.replace(']', ')');
                    getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).edit().putString("skills_id", skills_list).apply();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, EmailFragment.newInstance("", "")).addToBackStack(null).commit();
                }
                else {
                    JsonArray arrayList = new JsonArray();
                    Iterator<Integer> itr = ids.iterator();
                    while (itr.hasNext()) {
                        arrayList.add(itr.next());
                    }
                    JsonObject jsonObject = new JsonObject();
                    String skills_list = String.valueOf(arrayList);
                    skills_list = skills_list.replace('[', '(');
                    skills_list = skills_list.replace(']', ')');
                    jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                    jsonObject.addProperty("user_id", String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 60)));
                    jsonObject.addProperty("skills_id", skills_list);
                    jsonObject.addProperty("distance", max_distance.getText().toString());
                    jsonObject.addProperty("latitude", getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getString("latitude", ""));
                    jsonObject.addProperty("longitude", getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getString("longitude", ""));
                    Log.v("Responsejson", String.valueOf(jsonObject));
                    APIClient.getInstance().getMyApi().getUsersUnderLocation(jsonObject).enqueue(new Callback<List<user>>() {
                        @Override
                        public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                            Log.e("Response1234", String.valueOf(response.body()));
                            // do anything with response
                            List<user> response1 = response.body();
//                            Toast.makeText(getContext(), String.valueOf(response1.size()), Toast.LENGTH_SHORT).show();
                            profiles.clear();
                            for (int i = 0; i < response1.size(); i++) {
                                user jsonObject1 = response1.get(i);
                                Profile profile = new Profile();
                                profile.setName(jsonObject1.getName());
                                if (jsonObject1.getAge() != null)
                                    profile.setAge(Integer.parseInt(jsonObject1.getAge()));
                                if (jsonObject1.getProfile_image() != null)
                                    profile.setImageUrl("https://bitsphere.in/Brig/profile/" + jsonObject1.getProfile_image());
                                profile.setLocation("");
                                profile.setId(jsonObject1.getId());
                                Log.e("Profile1234", profile.getId());
                                profiles.add(profile);
                            }
//                            Toast.makeText(getContext(), String.valueOf(profiles), Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, new SwipeViewFragment(profiles)).commit();
                        }

                        @Override
                        public void onFailure(Call<List<user>> call, Throwable t) {
                            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }
    @Override
    public void onClick(int value)
    {
        Integer p = new Integer(value);
//        Toast.makeText(getContext(), "Name " + value, Toast.LENGTH_SHORT).show();
        if (ids.contains(p))
        {
            ids.remove(p);
        }
        else
        {
            ids.add(p);
        }
    }
}