package edu.escuelaing.ieti.jeebka.Interface;

import java.util.List;

import edu.escuelaing.ieti.jeebka.Models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JeebkaApi {

    @GET("users/{email}")
    Call<User> getUserByEmail(@Path("email") String email);
}
