package com.addusername.cardparser.model.rest;

import com.addusername.cardparser.model.Contact;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {
    @Multipart
    @POST("v2")
    Call<HashMap<String, Rect>> getRect(@Part MultipartBody.Part file);

    @Multipart
    @POST("v4")
    Call<HashMap<String, Rect>> getRectV4(@Part MultipartBody.Part file);

    @GET("v5/{img_name}")
    Call<Contact> getRectV5(@Path("img_name") String name);
}
