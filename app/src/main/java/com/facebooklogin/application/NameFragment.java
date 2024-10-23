package com.facebooklogin.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NameFragment newInstance(String param1, String param2) {
        NameFragment fragment = new NameFragment();
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
        View view = inflater.inflate(R.layout.fragment_name, container, false);
        ImageView next = view.findViewById(R.id.next);
        EditText name = view.findViewById(R.id.name);
        SharedPreferences prefs = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        if (prefs.contains("old_name"))
        {
            name.setText(prefs.getString("old_name", ""));
        }
        else if (prefs.contains("username"))
        {
            name.setText(prefs.getString("username", ""));
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name1 = view.findViewById(R.id.name);
                prefs.edit().putString("username", name1.getText().toString()).apply();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", String.valueOf(name.getText()));
                jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
                jsonObject.addProperty("user_id",String.valueOf(prefs.getInt("user_id", 60)));
//                Toast.makeText(getActivity(), String.valueOf(jsonObject), Toast.LENGTH_SHORT).show();
                APIClient.getInstance().getMyApi().signUpName(jsonObject).enqueue(new Callback<NameApi>() {
                    @Override
                    public void onResponse(Call<NameApi> call, retrofit2.Response<NameApi> response) {
//                        Toast.makeText(getActivity(), String.valueOf(response.body()), Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.other_container, FirstPhotoFragment.newInstance("",""))
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onFailure(Call<NameApi> call, Throwable t) {
                        Toast.makeText(getActivity(), "Failed to add name", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return view;
    }
}