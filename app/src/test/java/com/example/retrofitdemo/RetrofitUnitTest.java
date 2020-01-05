package com.example.retrofitdemo;



import com.example.retrofit_library.Retrofit;
import com.example.retrofit_library.http.Field;
import com.example.retrofit_library.http.GET;
import com.example.retrofit_library.http.POST;
import com.example.retrofit_library.http.Query;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


/**
 * author: xpf
 * date: 2020/1/5 11:33
 * description: Retrofit的简单使用测试
 * 该类进行使用测试的时候引入了相关依赖
 * //OkHttp
 * implementation 'com.squareup.okhttp3:okhttp:4.3.0'
 * //Retrofit
 * implementation 'com.squareup.retrofit2:retrofit:2.4.0'
 */
public class RetrofitUnitTest {

//    private static final String IP = "192.168.1.125";
    private static final String IP = "144.34.161.97";
    private static final String KEY = "aa205eeb45aa76c6afe3c52151b52160";
    private static final String BASE_URL = "http://apis.juhe.cn/";

    //集合ip查询接口
    /*interface HOST {
        @GET("/ip/ipNew")
        Call<ResponseBody> get(@Query("ip") String ip, @Query("key") String key);

        @POST("ip/ipNew")
        @FormUrlEncoded
        Call<ResponseBody> post(@Field("ip") String ip, @Field("key") String key);
    }*/



    /*@Test
    public void retrofit() throws IOException {
        //-----------------------------Retrofit2---------------------------
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        HOST host = retrofit.create(HOST.class);

        //Retrofit GET请求
        {
            Call<ResponseBody> call = host.get(IP, KEY);
            retrofit2.Response<ResponseBody> response = call.execute();
            if (response != null && response.body() != null) {
                System.out.println("Retrofit GET同步请求 >>> " + response.body().string());
                *//**Retrofit GET同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*//*
            }
        }

        //Retrofit POST同步请求
        {
            Call<ResponseBody> call = host.post(IP, KEY);
            retrofit2.Response<ResponseBody> response = call.execute();
            if (response != null && response.body() != null) {
                System.out.println("Retrofit POST同步请求 >>> " + response.body().string());
                *//**Retrofit POST同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*//*
            }
        }
    }*/


    //集合ip查询接口
    interface HOST {
        @GET("/ip/ipNew")
        Call get(@Query("ip") String ip, @Query("key") String key);

        @POST("ip/ipNew")
        Call post(@Field("ip") String ip, @Field("key") String key);
    }

    @Test
    public void testMyRetrofit() throws IOException {
        //-----------------------------my retrofit---------------------------
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        HOST host = retrofit.create(HOST.class);

        //Retrofit GET请求
        {
            Call call = host.get(IP, KEY);
            Response response = call.execute();
            if (response != null && response.body() != null) {
                System.out.println("Retrofit GET同步请求 >>> " + response.body().string());
                /**Retrofit GET同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*/
            }
        }

        //Retrofit POST同步请求
        {
            Call call = host.post(IP, KEY);
            Response response = call.execute();
            if (response != null && response.body() != null) {
                System.out.println("Retrofit POST同步请求 >>> " + response.body().string());
                /**Retrofit POST同步请求 >>> {"resultcode":"200","reason":"查询成功","result":{"Country":"美国","Province":"加利福尼亚","City":"","Isp":""},"error_code":0}*/
            }
        }
    }
}
