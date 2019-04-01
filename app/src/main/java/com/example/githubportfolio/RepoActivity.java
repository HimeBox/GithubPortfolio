package com.example.githubportfolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class RepoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visrepo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title_message = intent.getStringExtra(DataActivity.EXTRA_MESSAGE);
        String info_message = intent.getStringExtra(DataActivity.EXTRA_MESSAGE2);
        String other_message = intent.getStringExtra(DataActivity.EXTRA_MESSAGE3);


        TextView title = findViewById(R.id.new_title);
        TextView info = findViewById(R.id.new_info);
        TextView other = findViewById(R.id.new_other);
        title.setText(title_message);
        title.setText(info_message);
        title.setText(other_message);

    }

}
