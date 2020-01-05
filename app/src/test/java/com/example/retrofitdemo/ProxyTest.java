package com.example.retrofitdemo;

import com.example.retrofit_library.http.Field;
import com.example.retrofit_library.http.GET;
import com.example.retrofit_library.http.POST;
import com.example.retrofit_library.http.Query;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;



/**
 * author: xpf
 * date: 2020/1/5 12:43
 * description: 动态代理测试
 */
public class ProxyTest {

    /*//集合ip查询接口
    interface HOST {
        @GET("/ip/ipNew")
        Call<ResponseBody> get(@Query("ip") String ip, @Query("key") String key);

        @POST("ip/ipNew")
        @FormUrlEncoded
        Call<ResponseBody> post(@Field("ip") String ip, @Field("key") String key);
    }

    @Test
    public void proxy() {
        HOST host = (HOST) Proxy.newProxyInstance(HOST.class.getClassLoader(), new Class[]{HOST.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //获取方法名称
                System.out.println("获取方法名称 >>> " + method.getName());
                //获取方法的注解
                GET get = method.getAnnotation(GET.class);
                //获取方法的注解值
                System.out.println("获取方法的注解值 >>> " + get.value());
                //获取方法的参数的注解
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (Annotation[] annotation : parameterAnnotations) {
                    System.out.println("获取方法的参数的注解 >>> " + Arrays.toString(annotation));
                }
                // 获取方法的参数值
                System.out.println("获取方法的参数值 >>> " + Arrays.toString(args));
                return null;
            }
        });

        //$Proxy4. 运行时动态创建的类，存在于内存中
        host.get("11.22.33.44", "appKey");

        *//*获取方法名称 >>> get
        获取方法的注解值 >>> /ip/ipNew
        获取方法的参数的注解 >>> [@retrofit2.http.Query(encoded=false, value=ip)]
        获取方法的参数的注解 >>> [@retrofit2.http.Query(encoded=false, value=key)]
        获取方法的参数值 >>> [11.22.33.44, appKey]*//*
    }*/
}
