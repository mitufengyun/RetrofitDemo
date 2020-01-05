package com.example.retrofit_library;

import com.example.retrofit_library.http.Field;
import com.example.retrofit_library.http.GET;
import com.example.retrofit_library.http.POST;
import com.example.retrofit_library.http.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.HttpUrl;

/**
 * author: xpf
 * date: 2020/1/5 14:59
 * description: 方法的属性封装类
 */
public class ServiceMethod {

    //OkHttpClient唯一实现接口
    private final Call.Factory callFactory;
    //接口请求地址
    private final HttpUrl baseUrl;
    //方法的所请求方式("GET" 、"POST")
    private final String httpMethod;
    //方法的注解的值("/ip/ipNew")
    private String relativeUrl;
    //方法参数的数组(每个对象包含：参数注解值、参数值)
    private ParameterHandler[] parameterHandlers;
    //是否有请求体(GET方式没有)
    private boolean hasBody;

    private ServiceMethod(Builder builder) {
        this.callFactory = builder.retrofit.callFactory;
        this.baseUrl = builder.retrofit.baseUrl;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHanlers;
        this.hasBody = builder.hasBody;
    }

    //host.get("11.22.33.44", "appKey")
    public Call toCall(Object[] args) {
        //最终的 请求拼装类
        RequestBuilder requestBuilder = new RequestBuilder(httpMethod, baseUrl, relativeUrl, hasBody);
        //参考源码
        ParameterHandler[] handlers = parameterHandlers;

        int argumentCount = args != null ? args.length : 0;
        //Proxy方法的参数个数是否等于参数的数组(手动添加)的长度，此处理解为校验
        if (argumentCount != handlers.length) {
            throw new IllegalArgumentException(
                    "Argument count (" + argumentCount + ") doesn't match expected count (" + handlers.length + ")");
        }

        //循环拼接每个参数名和参数值
        for (int i = 0; i < argumentCount; i++) {
            //方法参数的数组中每个对象已经调用了对应实现方法
            handlers[i].apply(requestBuilder, args[i].toString());
        }

        //参考源码，创建请求
        return callFactory.newCall(requestBuilder.build());
    }

    public static class Builder {
        //OkHttpClient封装构建
        final Retrofit retrofit;
        //带注解的方法
        final Method method;
        //方法的所有注解(方法可能有多个注解)
        final Annotation[] methodAnnotations;
        //方法参数的所有注解(一个方法有多个参数，一个参数有多个注解)
        final Annotation[][] parameterAnnotationsArray;
        //方法的请求方式("GET" "POST")
        private String httpMethod;
        //方法的注解的值("/ip/ipNew")
        private String relativeUrl;
        //方法参数的数组(每个对象包含：参数注解值、参数值)
        private ParameterHandler[] parameterHanlers;
        //是否有请求体(GET方式没有)
        private boolean hasBody;


        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            //获取方法的所有注解(@GET @POST)
            methodAnnotations = method.getAnnotations();
            //获取方法参数的所有注解(@Query @Field)
            parameterAnnotationsArray = method.getParameterAnnotations();


        }

        public ServiceMethod build() {
            //遍历方法的每个注解
            for (Annotation annotation : methodAnnotations) {
                //把方法、方法上的注解、方法上注解的值都解析了
                parseMethodAnnotation(annotation);
            }

            //解析方法的参数的注解，方法的参数多：双层循环
            //因为一个方法可能有多个参数，一个参数可能有多个注解

            //定义方法参数的数组长度
            int parameterCount = parameterAnnotationsArray.length;
            //初始化方法参数的数组
            parameterHanlers = new ParameterHandler[parameterCount];
            //遍历方法的参数(我们只需要Query或者Field注解)
            for (int i = 0; i < parameterCount; i++) {
                //获取每个参数的所有注解
                Annotation[] parameterAnnotations = parameterAnnotationsArray[i];
                //如果该参数没有任何注解抛出异常
                if (parameterAnnotations == null) {
                    throw new IllegalArgumentException("No Retrofit annotation found." + " (parameter #" + (i + 1) + ")");
                }
                //参考源码，获取参数的注解值、参数值
                parameterHanlers[i] = parseParameter(i, parameterAnnotations);
            }

            return new ServiceMethod(this);
        }

        //解析参数的所有注解(嵌套循环)
        private ParameterHandler parseParameter(int i, Annotation[] annotations) {
            //参考源码
            ParameterHandler result = null;
            //遍历参数的注解，如：(@Query("ip") String ip)
            for (Annotation annotation : annotations) {
                //注解可能是Query或者Field
                ParameterHandler annotationAction = parseParameterAnnotation(annotation);
                //找不到继续找，可不写(增强代码健壮性)
                if (annotationAction == null) {
                    continue;
                }
                //赋值
                result = annotationAction;
            }
            //如果该参数没有任何注解抛出异常
            if (result == null) {
                throw new IllegalArgumentException("No Retrofit annotation found." + " (parameter #" + (i + 1) + ")");
            }

            return result;
        }

        //解析参数的注解，可能是Query或者Field
        private ParameterHandler parseParameterAnnotation(Annotation annotation) {
            //参考源码
            if (annotation instanceof Query) {
                Query query = (Query) annotation;
                String name = query.value();
                //注意：传过去的参数是注解的值，并非参数值，参数值由Proxy方法传入
                return new ParameterHandler.Query(name);
            } else if (annotation instanceof Field) {
                Field query = (Field) annotation;
                String name = query.value();
                //注意：传过去的参数是注解的值，并非参数值，参数值由Proxy方法传入
                return new ParameterHandler.Field(name);
            }

            return null;
        }

        //解析方法的注解，可能是GET，可能是POST
        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                //@GET("/ip/ipNew")
                parseHttpMethodAndPath("GET", ((GET)annotation).value(), false);
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST)annotation).value(), true);


            }
        }

        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            //方法的请求方式
            this.httpMethod = httpMethod;
            //方法的注解的值: "/ip/ipNew"
            this.relativeUrl = value;
            //方法是否有请求体
            this.hasBody = hasBody;

        }


    }


}
