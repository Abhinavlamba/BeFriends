package com.facebooklogin.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebooklogin.application.adapters.JobAdapter;
import com.facebooklogin.application.entities.Job;
import com.facebooklogin.application.entities.Jobobject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JobFragment newInstance(String param1, String param2) {
        JobFragment fragment = new JobFragment();
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
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        View view1 = view;
        ImageView done, back;
        getData(view);
        done = view.findViewById(R.id.done);
        back = view.findViewById(R.id.back);
        EditText title, company, from_date, to_date;
        RelativeLayout newJob = view.findViewById(R.id.new_job);
        title = view.findViewById(R.id.title);
        company = view.findViewById(R.id.company);
        from_date = view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);
        RecyclerView myList = (RecyclerView) view.findViewById(R.id.job_list);
        TextView addjob = view.findViewById(R.id.job1);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayout2);
        addjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addjob.setVisibility(View.GONE);
                myList.setVisibility(View.GONE);
                newJob.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject jsonObject = new JsonObject();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userpref", getContext().MODE_PRIVATE);
                jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                jsonObject.addProperty("user_id", String.valueOf(sharedPreferences.getInt("user_id", 4)));
                jsonObject.addProperty("job_title", title.getText().toString());
                jsonObject.addProperty("company", company.getText().toString());
               jsonObject.addProperty("from", from_date.getText().toString());
                jsonObject.addProperty("to", to_date.getText().toString());
//                Toast.makeText(getActivity(), String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                APIClient.getInstance().getMyApi().addJob(jsonObject).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        newJob.setVisibility(View.GONE);
                        addjob.setVisibility(View.VISIBLE);
                        myList.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        getData(view1);
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    public void getData(View view)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("user_id",String.valueOf(sharedPreferences.getInt("user_id", 4)));
        APIClient.getInstance().getMyApi().getJob(jsonObject).enqueue(new Callback<List<Jobobject>>() {
            @Override
            public void onResponse(Call<List<Jobobject>> call, Response<List<Jobobject>> response) {
               List<Jobobject> jsonObject1 = response.body();
                ArrayList<Job> joblist = new ArrayList<>();
                JobAdapter jobAdapter = new JobAdapter(getActivity(), joblist);
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                RecyclerView myList = view.findViewById(R.id.job_list);
                myList.setLayoutManager(layoutManager);
                myList.setAdapter(jobAdapter);
               for (int i = 0; i < jsonObject1.size(); i++)
               {
                   Job job = new Job(jsonObject1.get(i).getJob_title(), jsonObject1.get(i).getCompany(), jsonObject1.get(i).getFrom() + " " + jsonObject1.get(i).getTo());
                   joblist.add(job);
               }
               jobAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Jobobject>> call, Throwable t) {
            }
        });
    }
}