package edu.escuelaing.ieti.jeebka.Interface;

import java.util.List;

import edu.escuelaing.ieti.jeebka.DTOs.UserDto;
import edu.escuelaing.ieti.jeebka.Models.LoginResponse;
import edu.escuelaing.ieti.jeebka.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JeebkaApi {

    @GET("users/{email}")
    Call<User> getUserByEmail(@Path("email") String email);

    @POST("login")
    Call<LoginResponse> login(@Body UserDto userDto);

    @POST("users")
    Call<UserDto> createUser(@Body UserDto userDto);


}
