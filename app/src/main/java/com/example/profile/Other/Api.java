package com.example.profile.Other;


import com.example.profile.Model.FileModel;
import com.example.profile.Model.ModModel;
import com.example.profile.Model.Profile;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {



    @GET("get_user_detail.php")
    Call<List<Profile>> getdata(@Query("D_ID") String searchQuery, @Query("v")String b);

    @GET("get_all_mod.php")
    Call<List<ModModel>> getAllMods(@Query("v") String str4);

    @Multipart
    @POST("image_upload.php")
    Call<FileModel> callUploadApi(@Part MultipartBody.Part images, @Part("D_ID") RequestBody serial_wise_img);



}
