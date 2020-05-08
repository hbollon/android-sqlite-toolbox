package com.bcstudio.androidsqlitetoolbox.Http;

import com.bcstudio.androidsqlitetoolbox.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Builder to create a new REST client with the given API base url using retrofit2
 */
public class ServiceGenerator {

    // base url for request
    private static final String BASE_URL = Constants.SYNC_URL;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(
            Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
