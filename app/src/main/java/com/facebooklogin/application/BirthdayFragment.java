package com.facebooklogin.application;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BirthdayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BirthdayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int mMonth, mDay, mYear;
    static TextView birthdate;
    static TextView birthmonth;
    static TextView birthyear;

    public BirthdayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BirthdayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BirthdayFragment newInstance(String param1, String param2) {
        BirthdayFragment fragment = new BirthdayFragment();
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
        View view = inflater.inflate(R.layout.fragment_birthday, container, false);
        birthdate = view.findViewById(R.id.birthdate);
        birthmonth = view.findViewById(R.id.birthmonth);
        birthyear = view.findViewById(R.id.birthyear);
        SharedPreferences prefs = getActivity().getSharedPreferences("userpref", MODE_PRIVATE);
        if (prefs.contains("old_dob"))
        {
            String [] args = prefs.getString("old_dob", "").split("/");
            if (args.length > 0)
            birthdate.setText(args[0]);
            if (args.length > 1)
            birthmonth.setText(args[1]);
            if (args.length > 2)
                birthyear.setText(args[2]);
        }
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        birthmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        birthyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("userpref", MODE_PRIVATE).edit();
                editor.putString("birthdate",birthdate.getText().toString());
                editor.putString("birthmonth",birthmonth.getText().toString());
                editor.putString("birthyear",birthyear.getText().toString());
                editor.apply();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.other_container, GenderFragment.newInstance("",""))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            birthdate.setText(String.valueOf(day));
            birthmonth.setText(String.valueOf(month));
            birthyear.setText(String.valueOf(year));
        }
    }
}