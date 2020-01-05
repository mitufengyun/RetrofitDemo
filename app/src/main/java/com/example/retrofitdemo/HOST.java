package com.example.retrofitdemo;

import com.example.retrofit_library.http.Field;
import com.example.retrofit_library.http.GET;
import com.example.retrofit_library.http.POST;
import com.example.retrofit_library.http.Query;

import okhttp3.Call;

/**
 * author: xpf
 * date: 2020/1/5 18:15
 * description:集合ip查询接口
 */
public interface HOST {

    @GET("/ip/ipNew")
    Call get(@Query("ip") String ip, @Query("key") String key);

    @POST("ip/ipNew")
    Call post(@Field("ip") String ip, @Field("key") String key);
}
