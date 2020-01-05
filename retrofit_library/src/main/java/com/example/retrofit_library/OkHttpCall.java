package com.example.retrofit_library;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

/**
 * author: xpf
 * date: 2020/1/5 15:12
 * description:
 */
public class OkHttpCall implements Call {

    private ServiceMethod serviceMethod;
    private Object[] args;
    private Call rawCall;


    public OkHttpCall(ServiceMethod serviceMethod, Object[] args) {
        this.serviceMethod = serviceMethod;
        this.args = args;
        this.rawCall = serviceMethod.toCall(args);
    }

    @Override
    public void cancel() {
        rawCall.cancel();
    }

    @NotNull
    @Override
    public Call clone() {
        return rawCall.clone();
    }

    @Override
    public void enqueue(@NotNull Callback callback) {
        rawCall.enqueue(callback);
    }

    @NotNull
    @Override
    public Response execute() throws IOException {
        return rawCall.execute();
    }

    @Override
    public boolean isCanceled() {
        return rawCall.isCanceled();
    }

    @Override
    public boolean isExecuted() {
        return rawCall.isExecuted();
    }

    @NotNull
    @Override
    public Request request() {
        return rawCall.request();
    }

    @NotNull
    @Override
    public Timeout timeout() {
        return rawCall.timeout();
    }
}
