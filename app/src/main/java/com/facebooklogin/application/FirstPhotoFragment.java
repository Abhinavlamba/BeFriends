package com.facebooklogin.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static android.content.Context.MODE_PRIVATE;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstPhotoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int RESULT_OK = 200;
    ImageView previewImage;
    TextView choose;
    Uri selectedImage;
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
                File f = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), strings[1]);
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
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("userpref", MODE_PRIVATE).edit();
            selectedImage = Uri.parse(feed.toString());
            editor.putString("profilephoto", selectedImage.toString());
            editor.apply();

        }
    }
    public FirstPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstPhotoFragment newInstance(String param1, String param2) {
        FirstPhotoFragment fragment = new FirstPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_first_photo, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("userpref", MODE_PRIVATE);
        choose = view.findViewById(R.id.choose_photo_text);
        previewImage = view.findViewById(R.id.preview);
        if (prefs.contains("old_image") && (!(prefs.getString("old_image", "").equals(""))))
        {
            String url = "https://bitsphere.in/Brig/profile/" + prefs.getString("old_image", "");
            Glide.with(getContext()).load(url).into(previewImage);
            choose.setText(prefs.getString("old_image", ""));
            AsyncTask<String, Void, File> p = new RetrieveFeedTask().execute(url, prefs.getString("old_image", ""));
        }
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.other_container, BirthdayFragment.newInstance("",""))
                        .addToBackStack(null)
                        .commit();
            }
        });
        view.findViewById(R.id.choose_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , RESULT_OK);//one can be replaced with any action code
            }
        });
        ImageView image_button = view.findViewById(R.id.choose_photo);
        view.findViewById(R.id.choose_photo_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_button.performClick();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == -1) {
            selectedImage = imageReturnedIntent.getData();
            Uri imgUri=Uri.parse(String.valueOf(selectedImage));
            previewImage.setImageURI(null);
            previewImage.setImageURI(selectedImage);
            String uriString = selectedImage.toString();
            File file = new File(uriString);
            String path = file.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(selectedImage, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = file.getName();
            }
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("userpref", MODE_PRIVATE).edit();
            editor.putString("profilephoto", selectedImage.toString());
//            Toast.makeText(getActivity(), "Hey", Toast.LENGTH_SHORT).show();
            choose.setText(displayName);
            editor.apply();
        }
    }
}