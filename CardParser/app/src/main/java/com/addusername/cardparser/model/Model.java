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
import java.util.Random;
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
    private final String baseUrl ="http://[your local ip here]:2000/";
    //private final String baseUrl = "https://[your name app].herokuapp.com";
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
    @Override
    public void parseImgAndGetPrediction(byte[] bytes) {
        executor.execute(() -> {
            //String filename = ""+new Random().nextInt(999999)+".jpg";
            //Log.d("retro",filename);
            String filename = "img.jpg";
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), bytes);
            MultipartBody.Part mb = MultipartBody.Part.createFormData("img", filename, requestFile);
            Call<HashMap<String, Rect>> request = rt.getRectV4(mb);
            try {
                Response<HashMap<String, Rect>> response = request.execute();
                if(response.body().values() != null && !response.body().isEmpty()){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    vo.loadImg(bitmap, response.body().values());
                    // TODO hacer aqui una llamada a api/v5 para obtener posibles resultados para los campos
                    getPrediction(filename);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getPrediction(String filename) {

        Log.d("retro","GET PREDICTION");
        Call<Contact> request = rt.getRectV5(filename);
        try{
            Response<Contact> response = request.execute();
            if(response.body() != null){
                Log.d("retro","CONTAC NAME: "+response.body().getName());
                Contact c = response.body();
                c.setName(c.getName() != null ? c.getName():"");
                c.setEmail(c.getEmail() != null ? c.getEmail():"");
                c.setPhone(c.getPhone() != null ? c.getPhone():"");
                vo.setPredictedContact(c);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
