package edu.escuelaing.ieti.jeebka.Interface;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import edu.escuelaing.ieti.jeebka.DTOs.GroupDto;
import edu.escuelaing.ieti.jeebka.DTOs.UserDto;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
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

    @GET("users/{email}/groups")
    Call<List<Group>> getUsersGroups(@Path("email") String email);

    @GET("users/{email}/publics")
    Call<Map<Group, Integer>> showPublicGroups(@Path("email") String email);

    @POST("users/{email}/groups")
    Call<Group> createGroup(@Body GroupDto group, @Path("email") String email);

    @POST("users/{email}/groups/{name}/links")
    Call<Link> createLink(@Path("email") String email, @Path("name") String name);
    

}
