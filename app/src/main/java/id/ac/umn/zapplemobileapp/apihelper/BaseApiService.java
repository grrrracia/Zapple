package id.ac.umn.zapplemobileapp.apihelper;

import java.util.ArrayList;

import id.ac.umn.zapplemobileapp.FavouriteModel;
import id.ac.umn.zapplemobileapp.RestaurantModel;
import id.ac.umn.zapplemobileapp.ReviewModel;
import id.ac.umn.zapplemobileapp.UserModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface    BaseApiService {
    @FormUrlEncoded
    @POST("auth/signin")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/signup")
    Call<ResponseBody> registerRequest(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("password") String password);

    @Multipart
    @POST("review/data")
    Call<ResponseBody> reviewRequest(
            @Header("x-access-token") String accessToken,
            @Part("restaurantID") Integer restaurantID,
            @Part("review") String review,
            @Part("score") Integer score,
            @Part MultipartBody.Part photo);

    @Multipart
    @POST("user/profile_picture")
    Call<ResponseBody> changePP(@Header("x-access-token") String accessToken, @Part MultipartBody.Part photo);

    @GET("user/info")
    Call<ArrayList<UserModel>> getUser(@Header("x-access-token") String accessToken);

    @FormUrlEncoded
    @POST("user/info")
    Call<ResponseBody> editUser(@Header("x-access-token") String accessToken, @Field("name") String name, @Field("email") String email);

    @Multipart
    @POST("user/emailandpicture")
    Call<ResponseBody> editUser(
            @Header("x-access-token") String accessToken,
            @Part MultipartBody.Part photo,
            @Part("email") String email,
            @Part("name") String name);

    @FormUrlEncoded
    @POST("user/favourites/add")
    Call<ResponseBody> setFavourite(@Header("x-access-token") String accessToken, @Field("restaurantID") Integer restaurantID);

    @FormUrlEncoded
    @POST("user/favourites/delete")
    Call<ResponseBody> unsetFavourite(@Header("x-access-token") String accessToken, @Field("restaurantID") Integer restaurantID);

    @GET("restaurant/")
    Call<ArrayList<RestaurantModel>> getList(@Header("x-access-token") String accessToken);

    @GET("restaurant/name/{search}")
    Call<ArrayList<RestaurantModel>> getList(@Header("x-access-token") String accessToken, @Path("search") String search);

    @GET("restaurant/category/{genre}")
    Call<ArrayList<RestaurantModel>> getList(@Header("x-access-token") String accessToken, @Path("genre") Integer genre);

    @GET("restaurant/{genre}/{search}")
    Call<ArrayList<RestaurantModel>> getList(@Header("x-access-token") String accessToken, @Path("genre") Integer genre, @Path("search") String search);

    @GET("restaurant/id/{restaurantID}")
    Call<ArrayList<RestaurantModel>> getDetail(@Header("x-access-token") String accessToken, @Path("restaurantID") Integer restaurantID);

    @GET("user/favourites/")
    Call<ArrayList<RestaurantModel>> getUserFavourite(@Header("x-access-token") String accessToken);

    @GET("review/restaurant/{restaurantID}")
    Call<ArrayList<ReviewModel>> getReview(@Header("x-access-token") String accessToken, @Path("restaurantID") Integer restaurantID);

    @GET("review/user")
    Call<ArrayList<ReviewModel>> getReview(@Header("x-access-token") String accessToken);
}
