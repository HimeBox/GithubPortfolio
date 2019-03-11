package com.example.githubportfolio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GithubApi {
    // Handle all the API request.

    // GET request for user
    @GET("/users/HimeBox")
    Call<UserInfo> getUserInfo();

    // GET request for repos
    @GET("/users/HimeBox/repos")
    Call<List<RepoData>> getRepoData();

    // More need to be added...
}
