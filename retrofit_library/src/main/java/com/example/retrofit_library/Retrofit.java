package com.example.retrofit_library;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * author: xpf
 * date: 2020/1/5 14:04
 * description: 自定义的Retrofit
 */
public class Retrofit {

    //接口请求地址
    public HttpUrl baseUrl;
    //OkHttpClient唯一实现接口
    public Call.Factory callFactory;
    //key: 请求方法 如host.get()  value: 该方法的属性封装类
    public Map<Method, ServiceMethod> serviceMethodCache = new HashMap<>();

    //私有构造，不让通过new的方式实例化，通过构建者模式实例化
    private Retrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.callFactory = builder.callFactory;
    }


    public HttpUrl getBaseUrl() {
        return baseUrl;
    }

    public Call.Factory getCallFactory() {
        return callFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //获取方法的所有内容：方法名，方法的注解，方法参数的注解，方法的参数
                //将方法所有信息拦截之后，存储到ServiceMethod(JavaBean实体类)
                ServiceMethod serviceMethod = loadServiceMethod(method);
                return new OkHttpCall(serviceMethod, args);
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod result = serviceMethodCache.get(method);
        if (result != null) return result;

        //线程安全同步锁
        synchronized (serviceMethodCache) {
            //为什么要拿2次
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder(this, method).build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    public static class Builder{
        //接口请求地址
        private HttpUrl baseUrl;
        //OkHttpClient唯一实现接口
        private Call.Factory callFactory;

        public Builder baseUrl(String baseUrl) {
            if (baseUrl.isEmpty()) {
                throw new NullPointerException("baseUrl == null");
            }

            this.baseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(HttpUrl baseUrl) {
            if (baseUrl == null) {
                throw  new NullPointerException("baseUrl == null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        //属性的校验，或者初始化
        public Retrofit build() {
            if (baseUrl == null) {
                throw new NullPointerException("Base URL is required");
            }



            if (callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }
    }
}
