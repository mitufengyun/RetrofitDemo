package com.example.retrofitdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.retrofit_library.Retrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";


    private static final String IP = "144.34.161.97";
    private static final String KEY = "aa205eeb45aa76c6afe3c52151b52160";
    private static final String BASE_URL = "http://apis.juhe.cn/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void testMyRetrofit(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .build();

                HOST host = retrofit.create(HOST.class);

                //Retrofit GET请求
                {
                    Call call = host.get(IP, KEY);
                    Response response = null;
                    try {
                        response = call.execute();
                        if (response != null && response.body() != null) {
                            Log.d(TAG,"Retrofit GET同步请求 >>> " + response.body().string());
                            /**Retrofit GET同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*/
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Retrofit POST同步请求
                {
                    Call call = host.post(IP, KEY);
                    Response response = null;
                    try {
                        response = call.execute();
                        if (response != null && response.body() != null) {
                            Log.d(TAG,"Retrofit POST同步请求 >>> " + response.body().string());
                            /**Retrofit POST同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*/
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
