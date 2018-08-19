package com.arctouch.codechallenge.base;

import android.support.v7.app.AppCompatActivity;

import com.arctouch.codechallenge.BuildConfig;
import com.arctouch.codechallenge.api.TmdbApi;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BaseActivity extends AppCompatActivity {

    protected TmdbApi api = new Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(getClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi.class);

    public OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(getInterceptorLogging())
                .build();
        return client;
    }
    private HttpLoggingInterceptor.Level defineLogLevel() {

        return BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE;
    }
    public Interceptor getInterceptorLogging() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(defineLogLevel());
        return interceptor;
    }

}
