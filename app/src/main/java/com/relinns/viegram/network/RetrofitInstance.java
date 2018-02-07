package com.relinns.viegram.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitInstance {

    private static Retrofit retrofit;
 //   private static final String BASE_URL = "http://www.relinns.me/Viegram/apis/";
   // http://ec2-18-221-227-110.us-east-2.compute.amazonaws.com/Viegram/apis/
 // private static final String BASE_URL = "http://18.221.232.102/Viegram/apis/";

    private RetrofitInstance(){

    }

   public static final String BASE_URL = "http://107.6.9.5/~viegram/apis/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();



            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}