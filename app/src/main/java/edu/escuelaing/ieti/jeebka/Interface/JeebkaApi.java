package edu.escuelaing.ieti.jeebka.Interface;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.escuelaing.ieti.jeebka.DTOs.GroupDto;
import edu.escuelaing.ieti.jeebka.DTOs.LinkDto;
import edu.escuelaing.ieti.jeebka.DTOs.TaggedLinkDto;
import edu.escuelaing.ieti.jeebka.DTOs.UserDto;
import edu.escuelaing.ieti.jeebka.Models.Group;
import edu.escuelaing.ieti.jeebka.Models.Link;
import edu.escuelaing.ieti.jeebka.Models.LinkUpdateRequest;
import edu.escuelaing.ieti.jeebka.Models.LoginResponse;
import edu.escuelaing.ieti.jeebka.Models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("users/{email}/groups/{name}")
    Call<Group> getGroup(@Path("email") String email, @Path("name") String name);

    @GET("users/{email}/query/matchingPublicGroups")
    Call<List<Group>> showPublicGroups(@Path("email") String email);

    @POST("users/{email}/groups")
    Call<Group> createGroup(@Body GroupDto group, @Path("email") String email);

    @POST("users/{email}/groups/{name}/links")
    Call<LinkDto> createLinkNoTags(@Body LinkDto link, @Path("email") String email, @Path("name") String name);

    @POST("users/{email}/groups/{name}/tagLinks")
    Call<TaggedLinkDto> createLinkTags(@Body TaggedLinkDto link, @Path("email") String email, @Path("name") String name);

    @GET("users/{email}/query/tags")
    Call<HashSet<String>> getUserTags(@Path("email") String email);

    @POST("users/{email}/query/group/{group}/tag")
    Call<List<Link>> getLinksByTags(@Path("email") String email, @Path("group") String group, @Body List<String> tags);

    @PUT("users/{email}/groups/{groupName}/links/{linkName}/update")
    Call<List<Link>> updateLink(@Path("email") String email, @Path("groupName") String groupName, @Path("linkName") String linkName, @Body LinkUpdateRequest linkUpdateRequest);

}
