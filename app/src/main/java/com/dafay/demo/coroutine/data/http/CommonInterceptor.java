package com.dafay.demo.coroutine.data.http;


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
        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .header("Authorization","8eVgQuHb9ZlBOc5myqCzzR9pHtEkA3Et23INb5drFjHfER0nD0OZIk18")
                .method(oldRequest.method(), oldRequest.body())
                .build();
        return chain.proceed(newRequest);
    }
}
