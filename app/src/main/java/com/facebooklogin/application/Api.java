package com.facebooklogin.application;

import com.facebooklogin.application.entities.Education;
import com.facebooklogin.application.entities.Images;
import com.facebooklogin.application.entities.Item;
import com.facebooklogin.application.entities.Jobobject;
import com.facebooklogin.application.entities.message;
import com.facebooklogin.application.entities.user;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.jar.Attributes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    String BASE_URL = "https://bitsphere.in/Brig/";
    @POST("signup-phone")
    Call<PhoneApi>  loginPhone(@Body JsonObject jsonObject);
    @POST("signup-name")
    Call<NameApi> signUpName(@Body JsonObject jsonObject);
    @Multipart
    @POST("fb-users")
    Call<message> loginFacebook(@Part("api_key") RequestBody api_key,
                                     @Part MultipartBody.Part user_image,
                                     @Part("age") RequestBody age,
                                     @Part("dob") RequestBody dob,
                                     @Part("education") RequestBody education,
                                     @Part("job_info") RequestBody job_info,
                                     @Part("name") RequestBody name,
                                    @Part("token_key") RequestBody token_key);
    @POST("heading-add")
    Call<NameApi> addHeading(@Field("api_key") String api_key, @Field("user_id") String user_id, @Field("heading") String heading);
    @POST("user-occupation")
    Call<ResponseBody> addJob(@Body JsonObject jsonObject);
    @POST("user-occupation-fetch")
    Call<List<Jobobject>> getJob(@Body JsonObject jsonObject);
    @POST("user-education")
    Call<ResponseBody> addEducation(@Body JsonObject jsonObject);
    @POST("user-education-fetch")
    Call<List<Education>> getEducation(@Body JsonObject jsonObject);
    @POST("heading-add")
    Call<ResponseBody> addHeading(@Body JsonObject jsonObject);
    @GET("skills_show")
    Call<List<Item>> getSkills ();
    @POST("user-fetch-details")
    Call<List <user>> getUsers(@Body JsonObject jsonObject);
    @POST("user-match")
    Call <ResponseBody>match(@Body JsonObject jsonObject);
    @Multipart
    @POST ("user-details-update")
    Call <ResponseBody> updatedetails(@Part MultipartBody.Part user_image,
            @Part("api_key") RequestBody api_key,
                                      @Part("skill_id") RequestBody skill_id,
                                      @Part("user_id") RequestBody user_id,
                                      @Part("email") RequestBody email,
                                      @Part("age") RequestBody age,
                                      @Part("live_city") RequestBody live_city,
                                      @Part("from_city") RequestBody from_city,
                                      @Part("looking_for") RequestBody looking_for,
                                      @Part("industery") RequestBody industery,
                                      @Part("years_of_experience") RequestBody years_of_experience,
                                      @Part("education_level") RequestBody education_level,
                                      @Part("heading") RequestBody heading,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part("dob") RequestBody dob,
                                      @Part("gender") RequestBody gender,
                                      @Part("phone") RequestBody phone);
    @POST("user-id-details")
    Call <List<user>> getdetails(@Body JsonObject jsonObject);
    @POST ("user-details-location")
    Call <List<user>> getUsersUnderLocation(@Body JsonObject jsonObject);
    @POST ("user-details-match")
    Call <List<user>> getMatchUsers (@Body JsonObject jsonObject);
    @POST ("match-user12")
    Call <message>  checkUserMatch (@Body JsonObject jsonObject);
    @POST ("user-image-fatch")
    Call <List<Images>> getImages(@Body JsonObject jsonObject);
    @Multipart
    @POST("user-image")
    Call<RequestBody> uploadImages(@Part("api_key") RequestBody api_key,
                                   @Part("user_id") RequestBody user_id,
                                   @Part MultipartBody.Part image1,
                                   @Part MultipartBody.Part image2,
                                   @Part MultipartBody.Part image3,
                                   @Part MultipartBody.Part image4,
                                   @Part MultipartBody.Part image5,
                                   @Part MultipartBody.Part image6,
                                   @Part MultipartBody.Part image7,
                                   @Part MultipartBody.Part image8);
    @POST("auth-check")
    Call<PhoneApi> checkUser(@Body JsonObject jsonObject);
}
