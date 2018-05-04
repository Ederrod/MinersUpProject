package com.eder.rodriguez.minersup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eder.rodriguez.minersup.Model.NewsfeedPost;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView newsFeed;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsFeed = findViewById(R.id.newsfeedList);
        newsFeed.setHasFixedSize(true);
        newsFeed.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("minersup");
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(MainActivity.this, SignUpActivity.class);
                    // If user is directed to sign up activity,
                    // he shouldn't be able to go back to previous activity.
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, ImagePostActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //auth.addAuthStateListener(authStateListener);
        FirebaseRecyclerAdapter <NewsfeedPost,NewsFeedHolder> fbra = new FirebaseRecyclerAdapter<NewsfeedPost, NewsFeedHolder>(
                NewsfeedPost.class,
                R.layout.newsfeed_post,
                NewsFeedHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(NewsFeedHolder viewHolder, NewsfeedPost model, int position) {
                viewHolder.bindNewsFeed(model);
            }
        };
        newsFeed.setHasFixedSize(true);
        newsFeed.setLayoutManager(new LinearLayoutManager(this));
        newsFeed.setAdapter(fbra);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if(id == R.id.action_logout) {
            auth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class NewsFeedHolder extends RecyclerView.ViewHolder {
        View newsFeedView;
        Context newsFeedContext;
        public NewsFeedHolder(View view) {
            super(view);
            newsFeedView = view;
            newsFeedContext = view.getContext();
        }

        public void bindNewsFeed(NewsfeedPost newsfeedPost) {
            ImageView postImage = (ImageView) newsFeedView.findViewById(R.id.postImage);
            TextView postTitle = (TextView) newsFeedView.findViewById(R.id.postTitle);
            TextView postDescription = (TextView) newsFeedView.findViewById(R.id.postDescription);
            Picasso.with(newsFeedContext).load(newsfeedPost.getImage()).into(postImage);
            postTitle.setText(newsfeedPost.getTitle());
            postDescription.setText(newsfeedPost.getDescription());
        }

        public void setTitle(String title) {
            TextView postTitle = (TextView) itemView.findViewById(R.id.postTitle);
            postTitle.setText(title);
        }

        public void setDescription(String description) {
            TextView postDescription = (TextView) itemView.findViewById(R.id.postDescription);
            postDescription.setText(description);
        }

        public void setImage(String image, Context context) {
            ImageView postImage = (ImageView) itemView.findViewById(R.id.postImage);
            Picasso.with(context).load(image).into(postImage);
        }
    }
}