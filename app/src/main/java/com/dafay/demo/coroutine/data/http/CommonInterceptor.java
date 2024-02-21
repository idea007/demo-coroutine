package com.dafay.demo.coroutine.data.http;


import com.dafay.demo.coroutine.data.ConfigC;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Des
 * @Author lipengfei
 * @Date 2023/12/29
 */
public class CommonInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        Request newRequest = oldRequest.newBuilder()
                .header("Authorization", ConfigC.PEXELS_KEY)
                .method(oldRequest.method(), oldRequest.body())
                .build();
        return chain.proceed(newRequest);
    }
}
