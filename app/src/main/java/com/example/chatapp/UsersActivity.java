package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar mToolbar;
    private RecyclerView mUsersList;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mToolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.user_toolbar);

        //mUsersList.getLayoutManager().setMeasurementCacheEnabled(false);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);
        mUsersList=(RecyclerView) findViewById(R.id.users_list);
        //mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(mUserDatabase,Users.class).build();
        FirebaseRecyclerAdapter <Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options){

            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder usersViewHolder, int i, @NonNull final Users users) {
              usersViewHolder.name.setText(users.getName());
                usersViewHolder.status.setText(users.getStatus());
                Picasso.with(UsersActivity.this).load(users.getImage()).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.defaultuser1).into(usersViewHolder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(UsersActivity.this).load(users.getImage()).placeholder(R.drawable.defaultuser1).into(usersViewHolder.image);
                    }
                });
                //Picasso.with(UsersActivity.this).load(users.getImage()).placeholder(R.drawable.defaultuser1).into(usersViewHolder.image);
                final String user_id=getRef(i).getKey();
                usersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent profile_intent = new Intent(UsersActivity.this,ProfileActivity.class);
                        profile_intent.putExtra("user_id",user_id);
                        startActivity(profile_intent);
                    }
                });
            }
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout,parent,false);
                UsersViewHolder viewHolder = new UsersViewHolder((view));
                return viewHolder;
            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        TextView name,status;
        CircleImageView image;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user_single_name);
            status=itemView.findViewById(R.id.user_single_status);
            image = itemView.findViewById(R.id.user_single_image);

        }

    }
}