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
import android.view.View;
import android.widget.Button;
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
    private TextView date;
    private Button repoBtn;
    private Button followerBtn;
    private Button followingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the initial view (loadout) to activity_main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // The drawer layout for navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Elements in layouts, these can be found at res/layout
        avatarImage = findViewById(R.id.avatar_image);
        name = findViewById(R.id.name);
        userName = findViewById(R.id.user_name);
        bio = findViewById(R.id.bio);
        website = findViewById(R.id.website);
        email = findViewById(R.id.email);
        errorLog = findViewById(R.id.error_log);
        date = findViewById(R.id.date);
        repoBtn = findViewById(R.id.repo_btn);
        followerBtn = findViewById(R.id.follower_btn);
        followingBtn = findViewById(R.id.following_btn);

        // Build Gson, can be ignored as GsonConverterFactorey will automatically do that for you
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        // Build retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);

        Call<UserInfo> call = githubApi.getUserInfo();

        call.enqueue(new Callback<UserInfo>() {
            // We use implemented enqueue function to avoid multiple calls caused by serval activities.
            // However at that moment the app itself only have 1 activity.
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                // Detect if a code error appears instead of actual response. For example, code 404.
                if(!response.isSuccessful()){
                    errorLog.setText("Code: " + response.code());
                    return;
                }
                // Grab data from object created by Retrofit.
                UserInfo userInfos = response.body();
                name.setText(userInfos.getName());
                userName.setText(userInfos.getLogin());
                bio.setText(userInfos.getBio());
                website.setText("Github Url: " + userInfos.getHtml_url());
                email.setText("Email Address: " + userInfos.getEmail());
                date.setText("Date updated: " + userInfos.getUpdated_at());
                repoBtn.setText("Number of public repo: " + userInfos.getPublic_repos());
                followerBtn.setText("Number of followers: " + userInfos.getFollowers());
                followingBtn.setText("Number of accounts following: " + userInfos.getFollowing());
                Ion.with(avatarImage).load(userInfos.getAvatar_url());

            }
            // If something went wrong, display it on the screen.
            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                errorLog.setText(t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handle the situation press on back when the drawer pop out, implemented initially by Android Studio.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void repoPressed(View view){
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

        // The ListView item and a TextView for error log.
        repoList = findViewById(R.id.repo_list);
        repoErrorLog = findViewById(R.id.repo_error_log);

        // Almost same thing to create object.
        // This time use a List of created objects to keep track of data.
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
                // Handle the stuff like 404.
                if(!response.isSuccessful()){
                    repoErrorLog.setText("Code: " + response.code());
                    return;
                }

                List<RepoData> repoDatas = response.body();
                List<String> data = new ArrayList<String>();
                List<String> links = new ArrayList<String>();

                // Loop through List to grab data.
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
                // Handle the exception.
                repoErrorLog.setText(t.getMessage());
            }
        });
    }

    public void followerPressed(View view){
        // Just the view stuff, nothing being implemented yet.
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
    }

    public void followingPressed(View view){
        // Just the view stuff, nothing being implemented yet.
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. Implemented initially by Android Studio.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Implemented initially by Android Studio.
        // More functions need to be added.
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
        // Used 4 different layouts in 1 activity to handle the navigation.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Just a code copy from onCreate to handle the main layout, or, the profile page.
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
            date = findViewById(R.id.date);
            repoBtn = findViewById(R.id.repo_btn);
            followerBtn = findViewById(R.id.follower_btn);
            followingBtn = findViewById(R.id.following_btn);


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
                    date.setText("Date updated: " + userInfos.getUpdated_at());
                    repoBtn.setText("Number of public repo: " + userInfos.getPublic_repos());
                    followerBtn.setText("Number of followers: " + userInfos.getFollowers());
                    followingBtn.setText("Number of accounts following: " + userInfos.getFollowing());
                    Ion.with(avatarImage).load(userInfos.getAvatar_url());

                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    errorLog.setText(t.getMessage());
                }
            });
        } else if (id == R.id.nav_repo) {
            // Handle the repo page.
            // All the same stuff to handle the view, drawer, etc.
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

            // The ListView item and a TextView for error log.
            repoList = findViewById(R.id.repo_list);
            repoErrorLog = findViewById(R.id.repo_error_log);

            // Almost same thing to create object.
            // This time use a List of created objects to keep track of data.
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
                    // Handle the stuff like 404.
                    if(!response.isSuccessful()){
                        repoErrorLog.setText("Code: " + response.code());
                        return;
                    }

                    List<RepoData> repoDatas = response.body();
                    List<String> data = new ArrayList<String>();
                    List<String> links = new ArrayList<String>();

                    // Loop through List to grab data.
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
                    // Handle the exception.
                    repoErrorLog.setText(t.getMessage());
                }
            });



        } else if (id == R.id.nav_follower) {
            // Just the view stuff, nothing being implemented yet.
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
            // Just the view stuff, nothing being implemented yet.
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

        // The lines below are actually useless at that moment. As the selection navigate to new layout and refresh the page itself, the drawer will never stay in pop out status at that point.
        // Will be useful for next part or sth.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
