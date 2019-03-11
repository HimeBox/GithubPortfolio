package com.example.githubportfolio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GithubApi {

    @GET("/users/HimeBox")
    Call<UserInfo> getUserInfo();

    @GET("/users/HimeBox/repos")
    Call<List<RepoData>> getRepoData();
}
