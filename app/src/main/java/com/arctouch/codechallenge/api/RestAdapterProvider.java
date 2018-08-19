package com.arctouch.codechallenge.api;

import com.arctouch.codechallenge.BuildConfig;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by fernanda on 19/08/2018.
 */

public class RestAdapterProvider {
    private static final RestAdapterProvider INSTANCE = new RestAdapterProvider();


    Retrofit retrofit;


    private RestAdapterProvider() {
        retrofit = new Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(getClient())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    public static RestAdapterProvider instance() {
        return INSTANCE;
    }


    public Retrofit getAdapter() {
        return retrofit;
    }

    private HttpLoggingInterceptor.Level defineLogLevel() {
        return BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE;
    }

    public OkHttpClient getClient() {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(getInterceptorLogging())
                .build();
        return client;
    }



    public Interceptor getInterceptorLogging() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(defineLogLevel());
        return interceptor;
    }
}
