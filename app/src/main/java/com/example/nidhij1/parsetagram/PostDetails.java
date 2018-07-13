package com.example.nidhij1.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nidhij1.parsetagram.model.Post;

import org.parceler.Parcels;

public class PostDetails extends AppCompatActivity {

    Post post;
    public ImageView img;
    public TextView details_descripp;
    public TextView details_date;

    Post.Query pq = new Post.Query();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //toolbar
        //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//        getSupportActionBar().setElevation(
//                getResources().getDimensionPixelSize(R.dimen.action_bar_elevation)
//        );
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                final Intent intent_but = new Intent (PostDetails.this, FeedActivity.class);
                                startActivity(intent_but);
                                finish();

                            case R.id.action_schedules:
                                final Intent intent_but2 = new Intent (PostDetails.this, HomeActivity.class);
                                startActivity(intent_but2);
                                finish();

                            case R.id.action_music:

                        }
                        return true;
                    }
                });

        img = findViewById(R.id.iv_imgDetails);
        details_descripp = findViewById(R.id.tv_descripDetails);
        details_date = findViewById(R.id.iv_dateDetails);

        context = getApplicationContext();

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        //load image using glide
        Glide.with(context).load(post.getImage().getUrl())
                .into(img);

        details_descripp.setText(post.getDescription());

        details_date.setText(post.getCreatedAt().toString());
//        details_date.setText("today");

    }
}
