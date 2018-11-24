package com.example.theprogrammer.bce.rest;

import com.example.theprogrammer.bce.model.RequestData;
import com.example.theprogrammer.bce.model.RequestData2;
import com.example.theprogrammer.bce.model.Result;
import com.example.theprogrammer.bce.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {


    /*//@Headers("Content-Type:application/json")
    @POST("RestTest/service2.svc/TestMe")
    @FormUrlEncoded
    Call<Result> getUserID(@Part("userID") RequestBody userID);
//*/

    //@FormUrlEncoded
    //@POST("api/users/login")
    //Call<Result> login(@Body RequestData requestData);
//*
    @POST("api/users/login")
    Call<User> login(@Body RequestData rd);
//*/
/*
    @FormUrlEncoded
    @POST("api/users/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);
*/

    @POST("api/users/signup")
    Call<Result> CreateUser(@Body RequestData req);

    //@POST("api/users/faceFeatures")
    //Call<Result> sendFaceFeatures(@Body RequestData2 req);

    @Multipart
    @POST("sendPhoto")
    Call<ResponseBody> updateProfile(@Part MultipartBody.Part image, @Part("token") RequestBody fullName);
    //Call<ResponseBody>  updateProfile(@Part MultipartBody.Part image, @Part("description") RequestBody description);

    @POST("api/users/face_search")
    Call<Result> sendFaceFeatures(@Body RequestData req);
}

//Call<Result> getUserID(@Body String userID);
