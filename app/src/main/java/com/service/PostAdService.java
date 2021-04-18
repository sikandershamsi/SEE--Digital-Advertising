package com.service;

import com.model.AddClass;
import com.see.Api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostAdService
{
    @Headers({"Accept: application/json"})
    @Multipart
    @POST(Api.upload_ad)
    Call<AddClass> postadd(
            @Part("Authorization") RequestBody token,
            @Part("title") RequestBody title,
            @Part("type") RequestBody type,
            @Part("from_date") RequestBody from_date,
            @Part("to_date") RequestBody to_date,
            @Part("category_id") RequestBody category_id,
            @Part("age_group") RequestBody age_group,
            @Part("max_views") RequestBody max_views,
            @Part("country") RequestBody country,
            @Part("city") RequestBody city,
            @Part("ad_url") RequestBody ad_url,
            @Part MultipartBody.Part filename,
            @Part MultipartBody.Part thumb

    );
}
