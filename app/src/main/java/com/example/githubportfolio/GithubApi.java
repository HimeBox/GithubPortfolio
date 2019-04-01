package com.example.githubportfolio;

import android.app.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubApi {
    // Handle all the API request.
    // GET request for login user
    @GET("/user")
    Call<UserInfo> getUserInfo(@Query("access_token") String accessToken);

    // GET request for repos
    @GET("/user/repos")
    Call<List<RepoData>> getRepoData(@Query("access_token") String accessToken);

    // GET request for followers
    @GET("/user/followers")
    Call<List<Follow>> getFollowerData(@Query("access_token") String accessToken);

    // GET request for following
    @GET("/user/following")
    Call<List<Follow>> getFollowingData(@Query("access_token") String accessToken);

    // POST request for OAuth
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    // PUT request for follow
    @Headers("Content-Length: 0")
    @PUT("/user/following/{user}")
    Call<Void> putFollow(
            @Path("user") String user,
            @Query("access_token") String accessToken
    );

    // DELETE request for follow
    @DELETE("/user/following/{user}")
    Call<Void> deleteUnfollow(
            @Path("user") String user,
            @Query("access_token") String accessToken
    );

    // PUT request for star
    @Headers("Content-Length: 0")
    @PUT("/user/starred/{owner}/{repo}")
    Call<Void> putStar(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("access_token") String accessToken
    );

    // DELETE request for unstar
    @DELETE("/user/starred/{owner}/{repo}")
    Call<Void> deleteUnstar(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("access_token") String accessToken
    );

    // GET request for notifications
    @GET("/notifications")
    Call<List<Notifications>> getNotifications(
            @Query("since") String startDate,
            @Query("before") String date,
            @Query("access_token") String accessToken
    );

    // GET request for user search
    @GET("/search/users")
    Call<UserSearch> searchUser(
            @Query("q") String input
    );

    // GET request for repo search
    @GET("/search/repositories")
    Call<RepoSearch> searchRepo(
            @Query("q") String input
    );

    /* Request for other users */
    // GET request for login user
    @GET("/users/{user}")
    Call<UserInfo> getOtherUserInfo(@Path("user") String user);

    // GET request for repos
    @GET("/users/{user}/repos")
    Call<List<RepoData>> getOtherRepoData(@Path("user") String user);

    // GET request for followers
    @GET("/users/{user}/followers")
    Call<List<Follow>> getOtherFollowerData(@Path("user") String user);

    // GET request for following
    @GET("/users/{user}/following")
    Call<List<Follow>> getOtherFollowingData(@Path("user") String user);

    // More need to be added...
}
