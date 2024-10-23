package com.facebooklogin.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebooklogin.application.adapters.JobAdapter;
import com.facebooklogin.application.entities.Education;
import com.facebooklogin.application.entities.Job;
import com.facebooklogin.application.entities.Jobobject;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EducationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EducationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EducationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EducationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EducationFragment newInstance(String param1, String param2) {
        EducationFragment fragment = new EducationFragment();
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
        View view = inflater.inflate(R.layout.fragment_education, container, false);
        View view1 = view;
        ImageView done, back;
        getData(view);
        done = view.findViewById(R.id.done);
        back = view.findViewById(R.id.back);
        EditText field_of_study, school, graduation_year;
        RelativeLayout newEducation = view.findViewById(R.id.new_education);
        field_of_study = view.findViewById(R.id.field_of_study);
        school = view.findViewById(R.id.institution);
        graduation_year = view.findViewById(R.id.graduation_year);
        RecyclerView myList = (RecyclerView) view.findViewById(R.id.education_list);
        TextView addEducation = view.findViewById(R.id.education1);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativelayout2);
        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEducation.setVisibility(View.GONE);
                myList.setVisibility(View.GONE);
                newEducation.setVisibility(View.VISIBLE);
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
                jsonObject.addProperty("field_of_study", field_of_study.getText().toString());
                jsonObject.addProperty("institution", school.getText().toString());
                jsonObject.addProperty("graduation_year", graduation_year.getText().toString());
//                Toast.makeText(getActivity(), String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                APIClient.getInstance().getMyApi().addEducation(jsonObject).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        newEducation.setVisibility(View.GONE);
                        addEducation.setVisibility(View.VISIBLE);
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
        APIClient.getInstance().getMyApi().getEducation(jsonObject).enqueue(new Callback<List<Education>>() {
            @Override
            public void onResponse(Call<List<Education>> call, Response<List<Education>> response) {
                List<Education> jsonObject1 = response.body();
                ArrayList<Job> joblist = new ArrayList<>();
                JobAdapter jobAdapter = new JobAdapter(getActivity(), joblist);
                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                RecyclerView myList = view.findViewById(R.id.education_list);
                myList.setLayoutManager(layoutManager);
                myList.setAdapter(jobAdapter);
                for (int i = 0; i < jsonObject1.size(); i++)
                {
                    Job job = new Job(jsonObject1.get(i).getField_of_study(), jsonObject1.get(i).getInstitution(), jsonObject1.get(i).getGraduation_year());
                    joblist.add(job);
                }
                jobAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Education>> call, Throwable t) {
            }
        });
    }
}