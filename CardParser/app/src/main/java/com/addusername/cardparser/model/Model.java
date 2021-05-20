package com.addusername.cardparser.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.addusername.cardparser.interfaces.ModelOps;
import com.addusername.cardparser.interfaces.ViewOps;
import com.addusername.cardparser.model.rest.Rect;
import com.addusername.cardparser.model.rest.RestService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Model implements ModelOps {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final String baseUrl ="http://x.x.x.x:5000/"; //Localhost ip
    //private final String baseUrl = "https://[app name].herokuapp.com";
    /**
     * Interfaz implementada por Retrofit para realizar las llamadas https
     */
    private final RestService rt;
    private final ViewOps vo;

    public Model(ViewOps vo){
        this.vo = vo;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        rt = retrofit.create(RestService.class);
    }
    @Override
    public void parseImg(byte[] bytes) {
        executor.execute(() -> {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
            MultipartBody.Part mb = MultipartBody.Part.createFormData("img", "img.jpg", requestFile);
            Call<HashMap<String, Rect>> request = rt.getRect(mb);
            try {
                Response<HashMap<String, Rect>> response = request.execute();
                if(response.body().values() != null && !response.body().isEmpty()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    vo.loadImg(bitmap, response.body().values());
                    // TODO hacer aqui una llamada a api/v1 para obtener posibles resultados para los campos
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
