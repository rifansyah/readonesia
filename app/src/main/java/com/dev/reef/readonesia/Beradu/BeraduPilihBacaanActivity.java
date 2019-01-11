package com.dev.reef.readonesia.Beradu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.BacaanAdapter;
import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BeraduPilihBacaanActivity extends AppCompatActivity {
    private ArrayList<Bacaan> listBacaan;
    private RecyclerView recyclerView;
    private GamePostAdapter bacaanAdapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    public LinearLayout loadingMatchMaking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beradu_pilih_bacaan);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        listBacaan = new ArrayList<Bacaan>();

        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        loadingMatchMaking = (LinearLayout) findViewById(R.id.loading_matchmaking);

        bacaanAdapter = new GamePostAdapter(listBacaan, getApplicationContext(), loadingMatchMaking);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bacaanAdapter);

//        prepareBacaanData();
        getData();
    }

    public void getData() {
        DatabaseReference gamePostRef = mDatabase.child("game_post");
        gamePostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String title = postSnapshot.child("title").getValue().toString();
                    String description = postSnapshot.child("description").getValue().toString();
                    String author = postSnapshot.child("author").getValue().toString();
                    String urlToImage = postSnapshot.child("urlToImage").getValue().toString();
                    String idPost = postSnapshot.getKey().toString();

                    Bacaan bacaan = new Bacaan(title, description, "", author, "", urlToImage, idPost, "");
                    listBacaan.add(bacaan);
                }
                bacaanAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        final DatabaseReference postDb = mDatabase.child("game").child("room");
        postDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    postDb.child(data.getKey()).child(auth.getUid()).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (loadingMatchMaking.getVisibility() == View.VISIBLE) {
            loadingMatchMaking.setVisibility(View.GONE);
        } else {
            finish();
        }
    }
}