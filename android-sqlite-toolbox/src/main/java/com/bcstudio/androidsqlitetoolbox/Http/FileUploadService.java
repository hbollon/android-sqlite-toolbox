package com.bcstudio.androidsqlitetoolbox.Http;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Template for file upload request
 */
public interface FileUploadService {
    @Multipart
    @POST("sync")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part("content") RequestBody file
    );
}
