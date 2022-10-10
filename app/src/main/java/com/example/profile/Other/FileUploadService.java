package com.example.profile.Other;



import com.example.profile.Model.FileModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {

    @Multipart
    @POST("image_uploader.php")
    Call<FileModel> uploadImages(@Part List<MultipartBody.Part> images, @Part("dfgdfgdf") RequestBody dfgdfgdf, @Part("serial_wise_img") RequestBody serial_wise_img);
}
