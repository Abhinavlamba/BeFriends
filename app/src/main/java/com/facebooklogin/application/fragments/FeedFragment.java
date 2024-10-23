package com.facebooklogin.application.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebooklogin.application.APIClient;
import com.facebooklogin.application.R;
import com.facebooklogin.application.MainActivity;
import com.facebooklogin.application.adapters.MatchListAdapter;
import com.facebooklogin.application.entities.Match;
import com.facebooklogin.application.entities.message;
import com.facebooklogin.application.entities.user;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    View rootLayout;
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Match> matchList;
    private MatchListAdapter mAdapter;
    private String[] matchDates = {"11 Jan. 2020", "26 Dec. 2019", "12 Dec. 2019", "17 Nov. 2019", "06 Oct. 2019"};
    private int[] matchPictures = {R.drawable.user_woman_3, R.drawable.user_woman_4, R.drawable.user_woman_5, R.drawable.user_woman_6 , R.drawable.user_woman_7};
    private String[] matchNames = {"Name", "Name", "Name", "Name", "Name"};
    private String[] matchLocations = {"80 km", "80 km", "80 km", "80 km","80 6 km"};

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_feed, container, false);
        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view_matchs);
        matchList = new ArrayList<>();
        mAdapter = new MatchListAdapter(getContext(), matchList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("user_id", String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 60)));
        ArrayList<String> userMatch = new ArrayList();
        APIClient.getInstance().getMyApi().getMatchUsers(jsonObject).enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                List<user> user_list = response.body();
                for (int i = 0; i < user_list.size(); i++)
                {
                    userMatch.add(user_list.get(i).getId());
                    JsonObject jsonObject1 = new JsonObject();
                    jsonObject1.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                    jsonObject1.addProperty("user_id1", String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 60)));
                    jsonObject1.addProperty("user_id2", userMatch.get(i));
                    int finalI = i;
                    APIClient.getInstance().getMyApi().checkUserMatch(jsonObject1).enqueue(new Callback<message>()  {
                        @Override
                        public void onResponse(Call<message> call, Response<message> response) {
                            message message1 = response.body();
                            boolean check = message1.isStatus();
                            if (check)
                            {
                                JsonObject jsonObject2 = new JsonObject();
                                jsonObject2.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                                jsonObject2.addProperty("user_id", '(' +userMatch.get(finalI) + ')');
                                APIClient.getInstance().getMyApi().getdetails(jsonObject2).enqueue(new Callback<List<user>>() {
                                    @Override
                                    public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                                        user user1 = response.body().get(0);
                                        if (!(String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 60)).equals(userMatch.get(finalI)))) {
                                            Match match = new Match(Integer.parseInt(userMatch.get(finalI)), user1.getName(), "https://bitsphere.in/Brig/profile/" + user1.getProfile_image(), user1.getLive_city(), "");
                                            matchList.add(match);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<user>> call, Throwable t) {

                                    }
                                });
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<message> call, Throwable t) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<user>> call, Throwable t) {

            }
        });
        return rootLayout;
    }

    private void prepareMatchList(){
    }


}
