package com.example.githubportfolio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String EXTRA_MESSAGE2 = "com.example.myfirstapp.MESSAGE2";
    public static final String EXTRA_MESSAGE3 = "com.example.myfirstapp.MESSAGE3";

    private TextView errorLog;
    private TextView repoErrorLog;
    private ListView repoList;
    private ListView followerList;
    private TextView followerErrorLog;
    private ListView followingList;
    private ListView searchList;
    private TextView followingErrorLog;
    private TextView searchErrorLog;
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
    private Button userButton;
    private Button repoButton;
    public String accessToken;
    private TextView followUnfollowErrorLog;
    private TextView starErrorLog;
    public String username;
    public String owner;
    public String repo;
    public String startDate;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the initial view (loadout) to activity_main
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE0);
        accessToken = message;
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String startDate = df.format(Calendar.getInstance().getTime());

        runProfile();
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

    public void runProfile(){
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

        Call<UserInfo> call = githubApi.getUserInfo(accessToken); // Add user here!

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
                mDatabase.child("users").setValue(userInfos);
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


    public void runRepo(){
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

        Call<List<RepoData>> call = githubApi.getRepoData(accessToken); // Add user here!

        call.enqueue(new Callback<List<RepoData>>() {
            @Override
            public void onResponse(Call<List<RepoData>> call, Response<List<RepoData>> response) {
                // Handle the stuff like 404.
                if(!response.isSuccessful()){
                    repoErrorLog.setText("Code: " + response.code());
                    return;
                }

                List<RepoData> repoDatas = response.body();
                mDatabase.child("repos").setValue(repoDatas);
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
                repoList.setAdapter(new ButtonArrayAdapter(data,links,DataActivity.this));
            }

            @Override
            public void onFailure(Call<List<RepoData>> call, Throwable t) {
                // Handle the exception.
                repoErrorLog.setText(t.getMessage());
            }
        });
    }

    public void runFollower(){
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

        // The ListView item and a TextView for error log.
        followerList = findViewById(R.id.follower_list);
        followerErrorLog = findViewById(R.id.follower_error_log);

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

        Call<List<Follow>> call = githubApi.getFollowerData(accessToken); // Add user here!

        call.enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                // Handle the stuff like 404.
                if(!response.isSuccessful()){
                    followerErrorLog.setText("Code: " + response.code());
                    return;
                }

                List<Follow> followData = response.body();
                mDatabase.child("followers").setValue(followData);
                List<String> data = new ArrayList<String>();
                List<String> links = new ArrayList<String>();

                // Loop through List to grab data.
                for (Follow follow : followData){
                    String content = "";
                    String link = "";
                    content += follow.getLogin();
                    link += follow.getAvatarUrl();
                    data.add(content);
                    links.add(link);
                }
                if(data == null) {
                    followerErrorLog.setText("No Repository Found!");
                    return;
                }
                followerList.setAdapter(new FollowAdapter(data,links,DataActivity.this));
            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {
                // Handle the exception.
                repoErrorLog.setText(t.getMessage());
            }
        });
    }

    public void runFollowing(){
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

        // The ListView item and a TextView for error log.
        followingList = findViewById(R.id.following_list);
        followingErrorLog = findViewById(R.id.following_error_log);

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

        Call<List<Follow>> call = githubApi.getFollowingData(accessToken); // Add user here!

        call.enqueue(new Callback<List<Follow>>() {
            @Override
            public void onResponse(Call<List<Follow>> call, Response<List<Follow>> response) {
                // Handle the stuff like 404.
                if(!response.isSuccessful()){
                    followingErrorLog.setText("Code: " + response.code());
                    return;
                }

                List<Follow> followData = response.body();
                mDatabase.child("following").setValue(followData);
                List<String> data = new ArrayList<String>();
                List<String> links = new ArrayList<String>();

                // Loop through List to grab data.
                for (Follow follower : followData){
                    String content = "";
                    String link = "";
                    content += follower.getLogin();
                    link += follower.getAvatarUrl();
                    data.add(content);
                    links.add(link);
                }
                if(data == null) {
                    followingErrorLog.setText("No Repository Found!");
                    return;
                }
                followingList.setAdapter(new FollowAdapter(data,links,DataActivity.this));
            }

            @Override
            public void onFailure(Call<List<Follow>> call, Throwable t) {
                // Handle the exception.
                followingErrorLog.setText(t.getMessage());
            }
        });
    }

    public void runFollowUnfollow(){
        setContentView(R.layout.activity_follow_unfollow);
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

        Button followBtn = (Button)findViewById(R.id.follow_btn);

        followUnfollowErrorLog = (TextView) findViewById(R.id.follow_unfollow_log);

        followBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.follow_unfollow_input);
                username = editText.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<Void> call = githubApi.putFollow(username,accessToken);

                call.enqueue(new Callback<Void>() {
                    // We use implemented enqueue function to avoid multiple calls caused by serval activities.
                    // However at that moment the app itself only have 1 activity.
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Detect if a code error appears instead of actual response. For example, code 404.
                        if(!response.isSuccessful()){
                            followUnfollowErrorLog.setText("Failed! Code: " + response.code());
                            return;
                        }
                        followUnfollowErrorLog.setText("Successfully follow " + username + "!");
                    }
                    // If something went wrong, display it on the screen.
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        followUnfollowErrorLog.setText("Follow failed!");
                    }
                });
            }
        });

        Button unfollowBtn = (Button)findViewById(R.id.unfollow_btn);

        unfollowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.follow_unfollow_input);
                username = editText.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<Void> call = githubApi.deleteUnfollow(username,accessToken);

                call.enqueue(new Callback<Void>() {
                    // We use implemented enqueue function to avoid multiple calls caused by serval activities.
                    // However at that moment the app itself only have 1 activity.
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Detect if a code error appears instead of actual response. For example, code 404.
                        if(!response.isSuccessful()){
                            followUnfollowErrorLog.setText("Failed! Code: " + response.code());
                            return;
                        }
                        followUnfollowErrorLog.setText("Successfully unfollow " + username + "!");
                    }
                    // If something went wrong, display it on the screen.
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        followUnfollowErrorLog.setText("Unfollow failed!");
                    }
                });
            }
        });
    }

    public void runStar(){
        setContentView(R.layout.activity_star);
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

        Button starBtn = (Button)findViewById(R.id.star_btn);

        starErrorLog = (TextView) findViewById(R.id.star_log);

        starBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText ownerText = (EditText) findViewById(R.id.owner_input);
                owner = ownerText.getText().toString();

                EditText repoText = (EditText) findViewById(R.id.name_input);
                repo = repoText.getText().toString();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<Void> call = githubApi.putStar(owner,repo,accessToken);

                call.enqueue(new Callback<Void>() {
                    // We use implemented enqueue function to avoid multiple calls caused by serval activities.
                    // However at that moment the app itself only have 1 activity.
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Detect if a code error appears instead of actual response. For example, code 404.
                        if(!response.isSuccessful()){
                            starErrorLog.setText("Failed! Code: " + response.code());
                            return;
                        }
                        starErrorLog.setText("Successfully star " + owner + "'s " + repo + "!");
                    }
                    // If something went wrong, display it on the screen.
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        starErrorLog.setText("Star failed!");
                    }
                });
            }
        });

        Button unstarBtn = (Button)findViewById(R.id.unstar_btn);

        unstarBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                EditText ownerText = (EditText) findViewById(R.id.owner_input);
                owner = ownerText.getText().toString();

                EditText repoText = (EditText) findViewById(R.id.name_input);
                repo = repoText.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<Void> call = githubApi.deleteUnstar(owner,repo,accessToken);

                call.enqueue(new Callback<Void>() {
                    // We use implemented enqueue function to avoid multiple calls caused by serval activities.
                    // However at that moment the app itself only have 1 activity.
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Detect if a code error appears instead of actual response. For example, code 404.
                        if(!response.isSuccessful()){
                            starErrorLog.setText("Failed! Code: " + response.code());
                            return;
                        }
                        starErrorLog.setText("Successfully unstar " + owner + "'s " + repo + "!");
                    }
                    // If something went wrong, display it on the screen.
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        starErrorLog.setText("Unstar failed!");
                    }
                });
            }
        });
    }

    public void runNotification() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String date = df.format(Calendar.getInstance().getTime());

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GithubApi githubApi = retrofit.create(GithubApi.class);

        Call<List<Notifications>> call = githubApi.getNotifications(startDate,date,accessToken); // Add user here!

        call.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                // Handle the stuff like 404.
                if(!response.isSuccessful()){
                    return;
                }

                List<Notifications> notificationList = response.body();

                // Loop through List to grab data.
                for (Notifications noti : notificationList){
                    String reason = "";
                    String title = "";
                    reason += noti.getReason();
                    title += noti.getSubject().getTitle();

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(DataActivity.this)
                            .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                            .setContentTitle(reason)
                            .setContentText(title);

                    Intent notificationIntent = new Intent(DataActivity.this, DataActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(DataActivity.this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(contentIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0,mBuilder.build());
                }
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {

            }
        });

    }

    public void runSearch() {
        setContentView(R.layout.activity_search);
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
        searchList = findViewById(R.id.search_list);
        searchErrorLog = findViewById(R.id.search_error_log);
        userButton = findViewById(R.id.search_user_btn);
        repoButton = findViewById(R.id.search_repo_btn);

        userButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Almost same thing to create object.
                // This time use a List of created objects to keep track of data.

                EditText searchInput = (EditText) findViewById(R.id.search_input);
                String input = searchInput.getText().toString();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<UserSearch> call = githubApi.searchUser(input); // Add user here!

                call.enqueue(new Callback<UserSearch>() {
                    @Override
                    public void onResponse(Call<UserSearch> call, Response<UserSearch> response) {
                        // Handle the stuff like 404.
                        if(!response.isSuccessful()){
                            searchErrorLog.setText("Code: " + response.code());
                            return;
                        }

                        UserSearch result = response.body();
                        List<String> data = new ArrayList<String>();
                        List<String> links = new ArrayList<String>();
                        List<UserItem> items = result.getItems();

                        // Loop through List to grab data.
                        for (UserItem item : items){
                            String content = "";
                            String link = "";
                            content += item.getLogin();
                            link += item.getHtmlUrl();
                            data.add(content);
                            links.add(link);
                        }
                        if(result == null) {
                            searchErrorLog.setText("No Result Found!");
                            return;
                        }
                        searchList.setAdapter(new ButtonArrayAdapter(data,links,DataActivity.this));
                    }

                    @Override
                    public void onFailure(Call<UserSearch> call, Throwable t) {
                        // Handle the exception.
                        searchErrorLog.setText(t.getMessage());
                    }
                });
            }
        });

        repoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Almost same thing to create object.
                // This time use a List of created objects to keep track of data.

                EditText searchInput = (EditText) findViewById(R.id.search_input);
                String input = searchInput.getText().toString();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                GithubApi githubApi = retrofit.create(GithubApi.class);

                Call<RepoSearch> call = githubApi.searchRepo(input); // Add user here!

                call.enqueue(new Callback<RepoSearch>() {
                    @Override
                    public void onResponse(Call<RepoSearch> call, Response<RepoSearch> response) {
                        // Handle the stuff like 404.
                        if(!response.isSuccessful()){
                            searchErrorLog.setText("Code: " + response.code());
                            return;
                        }

                        RepoSearch result = response.body();
                        List<String> names = new ArrayList<String>();
                        List<String> infos = new ArrayList<String>();
                        List<String> others = new ArrayList<String>();
                        List<RepoItem> items = result.getItems();

                        // Loop through List to grab data.
                        for (RepoItem item : items){
                            String name = "";
                            String info = "";
                            String other = "";
                            name += item.getName();
                            info += item.getDescription();
                            other += "Number of stargazers: " + item.getStargazersCount() + "\n";
                            other += "Number of watchers: " + item.getWatchersCount() + "\n";
                            other += "Number of forks: "+ item.getForksCount();
                            names.add(name);
                            infos.add(info);
                            others.add(other);
                        }
                        if(result == null) {
                            searchErrorLog.setText("No Result Found!");
                            return;
                        }
                        searchList.setAdapter(new RepoVisualAdapter(names,infos,others,DataActivity.this));
                    }

                    @Override
                    public void onFailure(Call<RepoSearch> call, Throwable t) {
                        // Handle the exception.
                        searchErrorLog.setText(t.getMessage());
                    }
                });
            }
        });

    }

    public void repoPressed(View view){
        runRepo();
    }

    public void followerPressed(View view){
        runFollower();
    }

    public void followingPressed(View view){
        runFollowing();
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
        // Used 6 different layouts in 1 activity to handle the navigation.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            runProfile();
        } else if (id == R.id.nav_repo) {
            // Handle the repo page.
            runRepo();
        } else if (id == R.id.nav_follower) {
            runFollower();
        } else if (id == R.id.nav_following) {
            runFollowing();
        } else if (id == R.id.nav_follow) {
            runFollowUnfollow();
        } else if (id == R.id.nav_star) {
            runStar();
        } else if (id == R.id.nav_search) {
            runSearch();
        } else if (id == R.id.nav_notification) {
            runNotification();
        }

        // The lines below are actually useless at that moment. As the selection navigate to new layout and refresh the page itself, the drawer will never stay in pop out status at that point.
        // Will be useful for next part or sth.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
