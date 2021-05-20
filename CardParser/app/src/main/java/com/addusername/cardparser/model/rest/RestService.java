package com.addusername.cardparser.model.rest;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestService {
    @Multipart
    @POST("v2")
    Call<HashMap<String, Rect>> getRect(@Part MultipartBody.Part file);
}
