package com.example.githubportfolio;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView errorLog;
    private TextView repoErrorLog;
    private ListView repoList;
    private ImageView avatarImage;
    private TextView name;
    private TextView userName;
    private TextView bio;
    private TextView website;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        avatarImage = findViewById(R.id.avatar_image);
        name = findViewById(R.id.name);
        userName = findViewById(R.id.user_name);
        bio = findViewById(R.id.bio);
        website = findViewById(R.id.website);
        email = findViewById(R.id.email);
        errorLog = findViewById(R.id.error_log);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);

        Call<UserInfo> call = githubApi.getUserInfo();

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(!response.isSuccessful()){
                    errorLog.setText("Code: " + response.code());
                    return;
                }

                UserInfo userInfos = response.body();
                name.setText(userInfos.getName());
                userName.setText(userInfos.getLogin());
                bio.setText(userInfos.getBio());
                website.setText("Github Url: " + userInfos.getHtml_url());
                email.setText("Email Address: " + userInfos.getEmail());
                Ion.with(avatarImage).load(userInfos.getAvatar_url());

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                errorLog.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            avatarImage = findViewById(R.id.avatar_image);
            name = findViewById(R.id.name);
            userName = findViewById(R.id.user_name);
            bio = findViewById(R.id.bio);
            website = findViewById(R.id.website);
            email = findViewById(R.id.email);
            errorLog = findViewById(R.id.error_log);

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            GithubApi githubApi = retrofit.create(GithubApi.class);

            Call<UserInfo> call = githubApi.getUserInfo();

            call.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    if(!response.isSuccessful()){
                        errorLog.setText("Code: " + response.code());
                        return;
                    }

                    UserInfo userInfos = response.body();
                    name.setText(userInfos.getName());
                    userName.setText(userInfos.getLogin());
                    bio.setText(userInfos.getBio());
                    website.setText("Github Url: " + userInfos.getHtml_url());
                    email.setText("Email Address: " + userInfos.getEmail());
                    Ion.with(avatarImage).load(userInfos.getAvatar_url());

                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    errorLog.setText(t.getMessage());
                }
            });
        } else if (id == R.id.nav_repo) {
            setContentView(R.layout.activity_repo);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            repoList = findViewById(R.id.repo_list);
            repoErrorLog = findViewById(R.id.repo_error_log);

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            GithubApi githubApi = retrofit.create(GithubApi.class);

            Call<List<RepoData>> call = githubApi.getRepoData();

            call.enqueue(new Callback<List<RepoData>>() {
                @Override
                public void onResponse(Call<List<RepoData>> call, Response<List<RepoData>> response) {
                    if(!response.isSuccessful()){
                        repoErrorLog.setText("Code: " + response.code());
                        return;
                    }

                    List<RepoData> repoDatas = response.body();
                    List<String> data = new ArrayList<String>();
                    List<String> links = new ArrayList<String>();

                    for (RepoData repoData : repoDatas){
                        String content = "";
                        String link = "";
                        content += "Name: " + repoData.getName() + "\n";
                        content += "Owner's Username: " + repoData.getOwner().getLogin() + "\n";
                        content += "Description: " + repoData.getDescription();
                        link += repoData.getHtmlUrl();
                        data.add(content);
                        links.add(link);
                    }
                    if(data == null) {
                        repoErrorLog.setText("No Repository Found!");
                        return;
                    }
                    repoList.setAdapter(new ButtonArrayAdapter(data,links,MainActivity.this));
                }

                @Override
                public void onFailure(Call<List<RepoData>> call, Throwable t) {
                    repoErrorLog.setText(t.getMessage());
                }
            });



        } else if (id == R.id.nav_follower) {
            setContentView(R.layout.activity_follower);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        } else if (id == R.id.nav_following) {
            setContentView(R.layout.activity_following);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
