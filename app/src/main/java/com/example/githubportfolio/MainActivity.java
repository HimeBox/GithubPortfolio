package com.example.githubportfolio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.FirebaseApp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    // The OAuth stuff:
    private String clientId = "cef9cc2fcdb5dc06cd89";
    private String clientSecret = "1125fd8f784d55fd7f9802d5c5f9cb8437c9be2d";
    private String redirectUrl = "githubportfolio://callback";
    public String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // The OAuth login webpage:
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&scope=repo,user&redirect_uri=" + redirectUrl));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if(uri != null && uri.toString().startsWith(redirectUrl)){
            String code = uri.getQueryParameter("code");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final GithubApi githubApi = retrofit.create(GithubApi.class);

            final Call<AccessToken> accessTokenCall = githubApi.getAccessToken(
                    clientId,
                    clientSecret,
                    code
            );

            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    AccessToken token =  response.body();
                    accessToken = token.getAccessToken();
                    sendMessage(accessToken);
                    finish();
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    finish();
                }
            });
        }
    }

    public void sendMessage(String message) {
        Intent intent = new Intent(this, DataActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
