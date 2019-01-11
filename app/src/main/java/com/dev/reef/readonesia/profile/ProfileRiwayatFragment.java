package com.dev.reef.readonesia.profile;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.reef.readonesia.BacaanAdapter;
import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileRiwayatFragment extends Fragment {
    private RecyclerView rv;
    private ProfileRiwayatAdapter riwayatAdapter;
    private ArrayList<Riwayat> listRiwayat;

    private DatabaseReference db;
    private FirebaseAuth auth;

    public ProfileRiwayatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_riwayat, container, false);

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        listRiwayat = new ArrayList<>();

        rv = (RecyclerView) v.findViewById(R.id.list_item);

        riwayatAdapter = new ProfileRiwayatAdapter(listRiwayat, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(riwayatAdapter);

        prepareRiwayatData();

        return v;
    }

    public void prepareRiwayatData() {
        DatabaseReference historyDb = db.child("users").child(auth.getUid()).child("game_history");
        historyDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int poin = 0;
                int poinPemain = 0;
                int poinMusuh = 0;
                String winOrLose = "";
                String idMusuh = "";

                for(DataSnapshot historyGameSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot poinSnapshot : historyGameSnapshot.getChildren()) {
                        if(poinSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                            poinPemain = Integer.parseInt(poinSnapshot.getValue().toString());
                        } else {
                            poinMusuh = Integer.parseInt(poinSnapshot.getValue().toString());
                            idMusuh = poinSnapshot.getKey();
                        }
                    }

                    if (poinPemain == poinMusuh) {
                        winOrLose = "Seri";
                        poin = 20;
                    } else if (poinPemain > poinMusuh) {
                        winOrLose = "Menang";
                        poin = 50;
                    } else {
                        winOrLose = "Kalah";
                        poin = 0;
                    }

                    DatabaseReference oppositeDb = db.child("users").child(idMusuh);
                    final String finalWinOrLose = winOrLose;
                    final int finalPoin = poin;
                    oppositeDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listRiwayat.add(new Riwayat(dataSnapshot.child("name").getValue().toString(), finalWinOrLose, finalPoin));
                            riwayatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
