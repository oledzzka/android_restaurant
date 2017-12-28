package com.example.oleg.restaurants.network;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by oleg on 25.10.17.
 */

public class HttpRequest {
    private static final String USER_KEY = "33117921ea79430c4433b2088ab8fe22";
    private final String mURL;
    private Map<String, String> queryParameters;


    public HttpRequest(String url, Map<String, String > queryParameters) {
        mURL = url;
        this.queryParameters = queryParameters;
    }

    public String makeRequest() throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl.Builder builder = originalHttpUrl.newBuilder();
                if (queryParameters != null) {
                    for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
                        builder.addQueryParameter(entry.getKey(), entry.getValue());
                    }
                }
                Request request = original.newBuilder().url(builder.build()).build();
                return chain.proceed(request);
            }
        });
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.addHeader("user-key", USER_KEY).url(mURL).build();
        Response response = builder.build().newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }
    }
}
