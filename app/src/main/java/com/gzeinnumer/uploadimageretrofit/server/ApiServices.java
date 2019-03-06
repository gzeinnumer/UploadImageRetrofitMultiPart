package com.gzeinnumer.uploadimageretrofit.server;


import com.gzeinnumer.uploadimageretrofit.model.ResponseApiModel;
import com.gzeinnumer.uploadimageretrofit.model.ResponseDelete;
import com.gzeinnumer.uploadimageretrofit.model.ResponseGambarNoImage;
import com.gzeinnumer.uploadimageretrofit.model.ResponseGetData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServices {

    @Multipart
    @POST("uploadimage.php")
    Call<ResponseApiModel> uploadImage (@Part MultipartBody.Part image, @Part("name") RequestBody fname);

//    @Multipart
//    @POST("uploadimage.php")
//    Call<ResponseApiModel> uploadImage (@Part MultipartBody.Part image);

    @GET("getDataImage.php")
    Call<ResponseGetData> getAllImage();


    @FormUrlEncoded
    @POST("deleteimage.php")
    Call<ResponseDelete> deleteData(@Field("id") String id);

    @FormUrlEncoded
    @POST("updateimage.php")
    Call<ResponseGambarNoImage> updateDataNoImageChanged(@Field("id") String id,
                                                         @Field("name") String nama);

    @Multipart
    @POST("updateimage.php")
    Call<ResponseApiModel> uploadImageUpdate (@Part MultipartBody.Part image, @Part("name") RequestBody nama, @Part("isNewImage") RequestBody isNewImage);


}
