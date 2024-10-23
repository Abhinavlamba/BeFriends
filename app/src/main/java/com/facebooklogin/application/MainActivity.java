package com.facebooklogin.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.facebook.login.LoginManager;
import com.facebooklogin.application.adapters.ViewPagerAdapter;
import com.facebooklogin.application.fragments.AccountFragment;
import com.facebooklogin.application.fragments.ActivityFragment;
import com.facebooklogin.application.fragments.ChatFragment;
import com.facebooklogin.application.fragments.SwipeViewFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private ViewPager viewPager;

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        if (ageInt < 0)
            ageInt = 0;
        String ageS = ageInt.toString();
        return ageS;
    }

    BottomNavigationView bnv;
    FirebaseUser user;
    LocationManager locationManager;
    LocationListener locationListener;

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(this);
//        bnv.getMenu().findItem(R.id.fire).setChecked(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            GPSTracker gpsTracker = new GPSTracker(this);
            if (gpsTracker.getIsGPSTrackingEnabled())
            {
                String stringLatitude = String.valueOf(gpsTracker.latitude);
                SharedPreferences.Editor sharedPreferences = getSharedPreferences("userpref", MODE_PRIVATE).edit();
                sharedPreferences.putString("latitude", stringLatitude);
                String stringLongitude = String.valueOf(gpsTracker.longitude);
                sharedPreferences.putString("longitude", stringLongitude);

                String country = gpsTracker.getCountryName(this);

                String city = gpsTracker.getLocality(this);
                sharedPreferences.putString("city", city);
                sharedPreferences.apply();
                String postalCode = gpsTracker.getPostalCode(this);

                String addressLine = gpsTracker.getAddressLine(this);
            }
            else
            {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gpsTracker.showSettingsAlert();
            }
        }
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();
            bnv.setVisibility(View.VISIBLE);
            bnv.setSelectedItemId(R.id.account);
//            saveUser();
        } else {
            goLoginScreen();
        }
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            if (getIntent().getExtras().containsKey("Login")) {
                bnv.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.other_container, LocationFragment.newInstance("", ""))
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int mMenuId = menuItem.getItemId();
        for (int i = 0; i < bnv.getMenu().size(); i++) {
            MenuItem menuIt = bnv.getMenu().getItem(i);
            boolean isChecked = menuIt.getItemId() == menuItem.getItemId();
            menuItem.setChecked(isChecked);
        }
        switch (menuItem.getItemId()) {
            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.other_container, new AccountFragment()).commit();
                break;
            case R.id.fire:
                getSupportFragmentManager().beginTransaction().replace(R.id.other_container, SkillsFragment.newInstance("", "")).commit();
                break;
            case R.id.chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.other_container, new ActivityFragment()).commit();
                break;
        }
        return true;
    }

    private void goLoginScreen() {
        bnv.setVisibility(View.GONE);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    GPSTracker gpsTracker = new GPSTracker(this);
                    if (gpsTracker.getIsGPSTrackingEnabled()) {
                        String stringLatitude = String.valueOf(gpsTracker.latitude);
                        SharedPreferences.Editor sharedPreferences = getSharedPreferences("userpref", MODE_PRIVATE).edit();
                        sharedPreferences.putString("latitude", stringLatitude);
                        String stringLongitude = String.valueOf(gpsTracker.longitude);
                        sharedPreferences.putString("longitude", stringLongitude);

                        String country = gpsTracker.getCountryName(this);

                        String city = gpsTracker.getLocality(this);
                        sharedPreferences.putString("city", city);
                        sharedPreferences.apply();
                        String postalCode = gpsTracker.getPostalCode(this);

                        String addressLine = gpsTracker.getAddressLine(this);

                    }
                }
                break;

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public void saveUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
//        Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getSharedPreferences("userpref", MODE_PRIVATE);
        String image = sharedPreferences.getString("profilephoto", "0");
        Uri fileUri = Uri.parse(image);
        int day, month, year;
        day = Integer.parseInt(sharedPreferences.getString("birthdate", "0"));
        month = Integer.parseInt(sharedPreferences.getString("birthmonth", "0"));
        year = Integer.parseInt(sharedPreferences.getString("birthyear", "0"));
        String age1 = getAge(year, month, day);
        sharedPreferences.edit().putString("age", age1).apply();
        String dob1 = sharedPreferences.getString("birthdate", "0") + "/" + sharedPreferences.getString("birthmonth", "0") + "/" + sharedPreferences.getString("birthyear", "0");
        String path = getPathFromUri(this, fileUri);
        if (path == null)
            path = fileUri.getPath();
        File file = null;
        file = new File(path);
        RequestBody user_image1 = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part user_image = MultipartBody.Part.createFormData("user_image", file.getName(), user_image1);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString("username", ""));
        RequestBody api_key = RequestBody.create(MediaType.parse("text/plain"), "bWFnZ2llOnN1bW1lcnM");
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), age1);
        RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), dob1);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("email", ""));
        RequestBody skill_id = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("skills_id", ""));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getSharedPreferences("userpref", MODE_PRIVATE).getInt("user_id", 60)));
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("latitude", ""));
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("longitude", ""));
        RequestBody live_city = null;
        RequestBody from_city = null;
        RequestBody looking_for = null;
        RequestBody industery = null;
        RequestBody years_of_experience = null;
        RequestBody education_level = null;
        RequestBody heading = null;
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("phone_no",""));
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), getSharedPreferences("userpref", MODE_PRIVATE).getString("gender", ""));
        APIClient.getInstance().getMyApi().updatedetails(user_image, api_key, skill_id, user_id, email, age, live_city, from_city, looking_for, industery, years_of_experience, education_level, heading, latitude, longitude, dob, gender, phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                bnv.setSelectedItemId(R.id.account);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MainActivity.this,  call.toString() + t.getMessage() + " Error", Toast.LENGTH_SHORT).show();
                Log.v("Error ", call.toString() + "    " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}