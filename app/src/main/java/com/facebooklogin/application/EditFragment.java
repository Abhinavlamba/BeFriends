package com.facebooklogin.application;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebooklogin.application.adapters.ImageAdapter;
import com.facebooklogin.application.entities.Images;
import com.facebooklogin.application.entities.user;
import com.facebooklogin.application.fragments.AccountFragment;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int RESULT_OK = 200;
    RequestBody update_profile_picture = null, response1, response2, response3, response4, response5, response6, response7, response8;
    File image_file = null, imagefile1, imagefile2, imagefile3, imagefile4, imagefile5, imagefile6, imagefile7, imagefile8;
    View view1 = null;

    ImageView add1, add2, add3, add4, add5, add6, add7, add8;
    ImageView cross1, cross2, cross3, cross4, cross5, cross6, cross7, cross8;
    CircleImageView image1, image2, image3, image4, image5, image6, image7, image8;
    class RetrieveFeedTask extends AsyncTask<String, Void, File> {

        private Exception exception;
        private String image;

        @Override
        protected File doInBackground(String... strings) {
            try {
                java.net.URL url = new java.net.URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                input.close();
                File f = new File(getContext().getCacheDir(), strings[1]);
                image = strings[1];
                    Bitmap bitmap = myBitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    f.createNewFile();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    return f;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }
        protected void onPostExecute(File feed) {
            if (image == null)
                return;
            if (image.equals("temp1")) {
                imagefile1 = feed;
                response1 = RequestBody.create(MediaType.parse("image/*"), imagefile1);
            }
            if (image.equals("temp2")) {
                imagefile2 = feed;
                response2 = RequestBody.create(MediaType.parse("image/*"), imagefile2);
            }
            if (image.equals("temp3")) {
                imagefile3 = feed;
                response3 = RequestBody.create(MediaType.parse("image/*"), imagefile3);
            }
            if (image.equals("temp4")) {
                imagefile4 = feed;
                response4 = RequestBody.create(MediaType.parse("image/*"), imagefile4);
            }
            if (image.equals("temp5")) {
                imagefile5 = feed;
                response5 = RequestBody.create(MediaType.parse("image/*"), imagefile5);
            }
            if (image.equals("temp6")) {
                imagefile6 = feed;
                response6 = RequestBody.create(MediaType.parse("image/*"), imagefile6);
            }
            if (image.equals("temp7")) {
                imagefile7 = feed;
                response7 = RequestBody.create(MediaType.parse("image/*"), imagefile7);
            }
            if (image.equals("temp8")) {
                imagefile8 = feed;
                response8 = RequestBody.create(MediaType.parse("image/*"), imagefile8);
            }
        }
    }
    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        view1 = view;
        TextView addJob, addEducation;
        TextView done;
        EditText heading;
        addJob = view.findViewById(R.id.add_job_view);
        addEducation = view.findViewById(R.id.add_education_view);
        done = view.findViewById(R.id.done);
        heading = view.findViewById(R.id.heading);
        add1 = view.findViewById(R.id.add1);
        add1.setOnClickListener(EditFragment.this);
        add2 = view.findViewById(R.id.add2);
        add2.setOnClickListener(EditFragment.this);
        add3 = view.findViewById(R.id.add3);
        add3.setOnClickListener(EditFragment.this);
        add4 = view.findViewById(R.id.add4);
        add4.setOnClickListener(EditFragment.this);
        add5 = view.findViewById(R.id.add5);
        add5.setOnClickListener(EditFragment.this);
        add6 = view.findViewById(R.id.add6);
        add6.setOnClickListener(EditFragment.this);
        add7 = view.findViewById(R.id.add7);
        add7.setOnClickListener(EditFragment.this);
        add8 = view.findViewById(R.id.add8);
        add8.setOnClickListener(EditFragment.this);
        cross1 = view.findViewById(R.id.cross1);
        cross1.setOnClickListener(EditFragment.this);
        cross2 = view.findViewById(R.id.cross2);
        cross2.setOnClickListener(EditFragment.this);
        cross3 = view.findViewById(R.id.cross3);
        cross3.setOnClickListener(EditFragment.this);
        cross4 = view.findViewById(R.id.cross4);
        cross4.setOnClickListener(EditFragment.this);
        cross5 = view.findViewById(R.id.cross5);
        cross5.setOnClickListener(EditFragment.this);
        cross6 = view.findViewById(R.id.cross6);
        cross6.setOnClickListener(EditFragment.this);
        cross7 = view.findViewById(R.id.cross7);
        cross7.setOnClickListener(EditFragment.this);
        cross8 = view.findViewById(R.id.cross8);
        cross8.setOnClickListener(EditFragment.this);
        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, JobFragment.newInstance("","")).addToBackStack(null).commit();
            }
        });
        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_container, EducationFragment.newInstance("","")).addToBackStack(null).commit();
            }
        });

        EditText lives_in, from, looking_for, industry, years_of_experience, education_level;
        lives_in = view.findViewById(R.id.lives_in);
        from = view.findViewById(R.id.from);
        looking_for = view.findViewById(R.id.looking_for);
        industry = view.findViewById(R.id.industry);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        education_level = view.findViewById(R.id.education_level);
        ArrayList<String> images = new ArrayList<>();
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject1.addProperty("user_id", String.valueOf(getActivity().getSharedPreferences("userpref",MODE_PRIVATE).getInt("user_id", 60)));
        APIClient.getInstance().getMyApi().getImages(jsonObject1).enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {
                Images images1 = response.body().get(0);
                if (images1 == null)
                    return;
                image1 = view.findViewById(R.id.image1);
                image2 = view.findViewById(R.id.image2);
                image3 = view.findViewById(R.id.image3);
                image4 = view.findViewById(R.id.image4);
                image5 = view.findViewById(R.id.image5);
                image6 = view.findViewById(R.id.image6);
                image7 = view.findViewById(R.id.image7);
                image8 = view.findViewById(R.id.image8);
                String img1, img2, img3, img4, img5, img6, img7, img8;
                img1 = images1.getImage1();
                img2 = images1.getImage2();
                img3 = images1.getImage3();
                img4 = images1.getImage4();
                img5 = images1.getImage5();
                img6 = images1.getImage6();
                img7 = images1.getImage7();
                img8 = images1.getImage8();
                String base_url = "https://bitsphere.in/Brig/user_image/" + getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getInt("user_id", 60) + "/";
                if (!img1.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img1).into(image1);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img1, "temp1"));
                }
                else
                    image1.setBackgroundResource(R.drawable.curve_background2);

                if (!img2.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img2).into(image2);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img2, "temp2"));
                }
                else
                    image2.setBackgroundResource(R.drawable.curve_background2);

                if (!img3.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img3).into(image3);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img3, "temp3"));
                }
                else
                    image3.setBackgroundResource(R.drawable.curve_background2);

                if (!img4.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img4).into(image4);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img4, "temp4"));
                }
                else
                    image4.setBackgroundResource(R.drawable.curve_background2);

                if (!img5.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img5).into(image5);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img5, "temp5"));
                }
                else
                    image5.setBackgroundResource(R.drawable.curve_background2);

                if (!img6.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img6).into(image6);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img6, "temp6"));
                }
                else
                    image6.setBackgroundResource(R.drawable.curve_background2);

                if (!img7.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img7).into(image7);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img7, "temp7"));
                }
                else
                    image7.setBackgroundResource(R.drawable.curve_background2);
                if (!img8.equals(""))
                {
                    Glide.with(getContext()).load(base_url+img8).into(image8);
                    AsyncTask<String, Void, File> imagefile12 = (new RetrieveFeedTask().execute(base_url+img8, "temp8"));
                }
                else
                    image8.setBackgroundResource(R.drawable.curve_background2);
            }

            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("userpref", MODE_PRIVATE);
                    String dob1 = sharedPreferences.getString("birthdate", "0") + "/" + sharedPreferences.getString("birthmonth", "0") + "/" + sharedPreferences.getString("birthyear", "0");
                    RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), dob1);
                    RequestBody api_key1 = RequestBody.create(MediaType.parse("text/plain"), "bWFnZ2llOnN1bW1lcnM");
                    RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 59)));
                    RequestBody heading1 = RequestBody.create(MediaType.parse("text/plain"), heading.getText().toString());
                    RequestBody lives_in1 = RequestBody.create(MediaType.parse("text/plain"), lives_in.getText().toString());
                    RequestBody from1 = RequestBody.create(MediaType.parse("text/plain"), from.getText().toString());
                    RequestBody industery = RequestBody.create(MediaType.parse("text/plain"), industry.getText().toString());
                    RequestBody years_of_experience1 = RequestBody.create(MediaType.parse("text/plain"), years_of_experience.getText().toString());
                    RequestBody education_level1 = RequestBody.create(MediaType.parse("text/plain"), education_level.getText().toString());
                    RequestBody skill_id = RequestBody.create(MediaType.parse("text/plain"), AccountFragment.user_details.getSkill_id());
                    RequestBody email = RequestBody.create(MediaType.parse("text/plain"), AccountFragment.user_details.getEmail());
                    RequestBody age = RequestBody.create(MediaType.parse("text/plain"), AccountFragment.user_details.getAge());
                    MultipartBody.Part profile_photo = null;
                    URL url;
                    File file;
//                    Toast.makeText(getContext(), "hello world", Toast.LENGTH_SHORT).show();
                    if (image_file != null)
                    profile_photo = MultipartBody.Part.createFormData("user_image", image_file.getName(), update_profile_picture);
                    RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getString("latitude", ""));
                    RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getString("longitude", ""));
                    RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getString("phone_no","null"));
                    RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getString("gender", ""));
                    APIClient.getInstance().getMyApi().updatedetails(profile_photo, api_key1, skill_id, id1, email, age, lives_in1, from1, null, industery, years_of_experience1, education_level1, heading1, latitude, longitude, dob, gender, phone).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            File empty = new File("");
                            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");

                            MultipartBody.Part img1 = null, img2 = null, img3 = null, img4 = null, img5 = null, img6 = null, img7 = null, img8 = null;
                            if (imagefile1 != null)
                            img1 = MultipartBody.Part.createFormData("image1", imagefile1.getName(), response1);
                            else
                                img1 = MultipartBody.Part.createFormData("image1", "", attachmentEmpty);
                            if (imagefile2 != null)
                            img2 = MultipartBody.Part.createFormData("image2", imagefile2.getName(), response2);
                            else
                                img2 = MultipartBody.Part.createFormData("image2", "", attachmentEmpty);
                            if (imagefile3 != null)
                            img3 = MultipartBody.Part.createFormData("image3", imagefile3.getName(), response3);
                            else
                                img3 = MultipartBody.Part.createFormData("image3", "", attachmentEmpty);
                            if (imagefile4 != null)
                            img4 = MultipartBody.Part.createFormData("image4", imagefile4.getName(), response4);
                            else
                                img4 = MultipartBody.Part.createFormData("image4", "", attachmentEmpty);
                            if (imagefile5 != null)
                            img5 = MultipartBody.Part.createFormData("image5", imagefile5.getName(), response5);
                            else
                                img5 = MultipartBody.Part.createFormData("image5", "", attachmentEmpty);
                            if (imagefile6 != null)
                            img6 = MultipartBody.Part.createFormData("image6", imagefile6.getName(), response6);
                            else
                                img6 = MultipartBody.Part.createFormData("image6", "", attachmentEmpty);
                            if (imagefile7 != null)
                            img7 = MultipartBody.Part.createFormData("image7", imagefile7.getName(), response7);
                            else
                                img7 = MultipartBody.Part.createFormData("image7", "", attachmentEmpty);
                            if (imagefile8 != null)
                            img8 = MultipartBody.Part.createFormData("image8", imagefile8.getName(), response8);
                            else
                                img8 = MultipartBody.Part.createFormData("image8", "", attachmentEmpty);
                            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getActivity().getSharedPreferences("userpref", MODE_PRIVATE).getInt("user_id", 60)));
                            APIClient.getInstance().getMyApi().uploadImages(api_key1, user_id,img1, img2, img3, img4, img5, img6, img7, img8).enqueue(new Callback<RequestBody>() {
                                @Override
                                public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
//                                    Toast.makeText(getContext(), "Saved1234", Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(Call<RequestBody> call, Throwable t) {
//                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("Error1234", t.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t)
                        {
//                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ImageView lives_in_tick, from_tick, looking_for_tick, industry_tick, years_of_experience_tick, education_level_tick;
        lives_in_tick = view.findViewById(R.id.lives_in_tick);
        from_tick = view.findViewById(R.id.from_tick);
        looking_for_tick = view.findViewById(R.id.looking_for_tick);
        industry_tick = view.findViewById(R.id.industry_tick);
        years_of_experience_tick = view.findViewById(R.id.years_of_experience_tick);
        education_level_tick = view.findViewById(R.id.education_level_tick);
        view.findViewById(R.id.add_job_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.findViewById(R.id.add_education_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        view.findViewById(R.id.add_gender_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        view.findViewById(R.id.lives_in).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (lives_in.isFocused())
                lives_in_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.from).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (from.isFocused())
                    from_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.looking_for).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (looking_for.isFocused())
                looking_for_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.industry).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (industry.isFocused())
                    industry_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.years_of_experience).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (years_of_experience.isFocused())
                    years_of_experience_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.education_level).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (education_level.isFocused())
                    education_level_tick.setImageResource(R.drawable.tick);
            }
        });
        view.findViewById(R.id.lives_in_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    lives_in.setFocusable(false);
                    lives_in_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        view.findViewById(R.id.from_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    from.setFocusable(false);
                    from_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        view.findViewById(R.id.looking_for_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    looking_for.setFocusable(false);
                    looking_for_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        view.findViewById(R.id.industry_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    industry.setFocusable(false);
                    industry_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        view.findViewById(R.id.years_of_experience_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    years_of_experience.setFocusable(false);
                    years_of_experience_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        view.findViewById(R.id.education_level_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    education_level.setFocusable(false);
                    education_level_tick.setImageResource(R.drawable.ic_edit_gray_24dp);
            }
        });
        String user_id = '(' + String.valueOf(getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE).getInt("user_id", 59)) + ')';
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_key", "bWFnZ2llOnN1bW1lcnM");
        jsonObject.addProperty("user_id", user_id);
        view.findViewById(R.id.update_profile_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , RESULT_OK);//one can be replaced with any action code
            }
        });
        Log.e("Responsejson1234", String.valueOf(jsonObject));
        APIClient.getInstance().getMyApi().getdetails(jsonObject).enqueue(new Callback<List<user>>() {
            @Override
            public void onResponse(Call<List<user>> call, Response<List<user>> response) {
                List<user> users = response.body();
                user user_details = users.get(0);
                heading.setText(user_details.getHeading());
                lives_in.setText(user_details.getLive_city());
                from.setText(user_details.getFrom_city());
                industry.setText(user_details.getIndustery());
                years_of_experience.setText(user_details.getExperience_year());
                education_level.setText(user_details.getEducation_level());
            }
            @Override
            public void onFailure(Call<List<user>> call, Throwable t) {
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        Toast.makeText(getActivity(), "Code " + requestCode, Toast.LENGTH_SHORT).show();
            if (imageReturnedIntent == null)
                return;
        if (requestCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image_file = new File(filepath);
//            Toast.makeText(getActivity(), filepath + " ", Toast.LENGTH_SHORT).show();
            update_profile_picture = RequestBody.create(MediaType.parse("image/*"), image_file);
        }
        if (requestCode == 1)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image1 = view1.findViewById(R.id.image1);
            image1.setImageURI(selectedImage);
            image1.setBackground(null);
            cross1.setVisibility(View.VISIBLE);
            imagefile1 = new File(filepath);
            response1 = RequestBody.create(MediaType.parse("image/*"), imagefile1);
        }
        if (requestCode == 2)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image2 = view1.findViewById(R.id.image2);
            image2.setImageURI(selectedImage);
            image2.setBackground(null);
            cross2.setVisibility(View.VISIBLE);
            imagefile2 = new File(filepath);
            response2 = RequestBody.create(MediaType.parse("image/*"), imagefile2);
        }
        if (requestCode == 3)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image3 = view1.findViewById(R.id.image3);
            image3.setImageURI(selectedImage);
            image3.setBackground(null);
            cross3.setVisibility(View.VISIBLE);
            imagefile3 = new File(filepath);
            response3 = RequestBody.create(MediaType.parse("image/*"), imagefile3);
        }
        if (requestCode == 4)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image4 = view1.findViewById(R.id.image4);
            image4.setImageURI(selectedImage);
            image4.setBackground(null);
            cross4.setVisibility(View.VISIBLE);
            imagefile4 = new File(filepath);
            response4 = RequestBody.create(MediaType.parse("image/*"), imagefile4);
        }
        if (requestCode == 5)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image5 = view1.findViewById(R.id.image5);
            image5.setImageURI(selectedImage);
            image5.setBackground(null);
            cross5.setVisibility(View.VISIBLE);
            imagefile5 = new File(filepath);
            response5 = RequestBody.create(MediaType.parse("image/*"), imagefile5);
        }
        if (requestCode == 6)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image6 = view1.findViewById(R.id.image6);
            image6.setImageURI(selectedImage);
            image6.setBackground(null);
            cross6.setVisibility(View.VISIBLE);
            imagefile6 = new File(filepath);
            response6 = RequestBody.create(MediaType.parse("image/*"), imagefile6);
        }
        if (requestCode == 7)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image7 = view1.findViewById(R.id.image7);
            image7.setImageURI(selectedImage);
            image7.setBackground(null);
            cross7.setVisibility(View.VISIBLE);
            imagefile7 = new File(filepath);
            response7 = RequestBody.create(MediaType.parse("image/*"), imagefile7);
        }
        if (requestCode == 8)
        {
            Uri selectedImage = imageReturnedIntent.getData();
            String filepath = MainActivity.getPathFromUri(getActivity(), selectedImage);
            image8 = view1.findViewById(R.id.image8);
            image8.setImageURI(selectedImage);
            image8.setBackground(null);
            cross8.setVisibility(View.VISIBLE);
            imagefile8 = new File(filepath);
            response8 = RequestBody.create(MediaType.parse("image/*"), imagefile8);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.add1:
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                break;
            case R.id.add2:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 2);//one can be replaced with any action code
                break;
            case R.id.add3:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 3);//one can be replaced with any action code
                break;
            case R.id.add4:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 4);//one can be replaced with any action code
                break;
            case R.id.add5:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 5);//one can be replaced with any action code

                break;
            case R.id.add6:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 6);//one can be replaced with any action code
                break;
            case R.id.add7:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 7);//one can be replaced with any action code
                break;
            case R.id.add8:
                pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 8);//one can be replaced with any action code
                break;
            case R.id.cross1:
                image1.setImageURI(null);
                cross1.setVisibility(View.GONE);
                image1.setBackgroundResource(R.drawable.curve_background2);
                imagefile1 = null;
                response1 = null;
                break;
            case R.id.cross2:
                cross2.setVisibility(View.GONE);
                image2.setImageURI(null);
                image2.setBackgroundResource(R.drawable.curve_background2);
                imagefile2 = null;
                response2 = null;
                break;
            case R.id.cross3:
                cross3.setVisibility(View.GONE);
                image3.setImageURI(null);
                image3.setBackgroundResource(R.drawable.curve_background2);
                imagefile3 = null;
                response3 = null;
                break;
            case R.id.cross4:
                cross4.setVisibility(View.GONE);
                image4.setImageURI(null);
                image4.setBackgroundResource(R.drawable.curve_background2);
                imagefile4 = null;
                response4 = null;
                break;
            case R.id.cross5:
                cross5.setVisibility(View.GONE);
                image5.setImageURI(null);
                image5.setBackgroundResource(R.drawable.curve_background2);
                imagefile5 = null;
                response5 = null;
                break;
            case R.id.cross6:
                cross6.setVisibility(View.GONE);
                image6.setImageURI(null);
                image6.setBackgroundResource(R.drawable.curve_background2);
                imagefile6 = null;
                response6 = null;
                break;
            case R.id.cross7:
                cross7.setVisibility(View.GONE);
                image7.setImageURI(null);
                image7.setBackgroundResource(R.drawable.curve_background2);
                imagefile7 = null;
                response7 = null;
                break;
            case R.id.cross8:
                cross8.setVisibility(View.GONE);
                image8.setImageURI(null);
                image8.setBackgroundResource(R.drawable.curve_background2);
                imagefile8 = null;
                response8 = null;
                break;
        }
    }
}