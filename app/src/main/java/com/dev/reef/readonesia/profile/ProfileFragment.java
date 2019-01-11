package com.dev.reef.readonesia.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.BacaanAdapter;
import com.dev.reef.readonesia.EditProfileActivity;
import com.dev.reef.readonesia.HomeActivity;
import com.dev.reef.readonesia.LoginActivity;
import com.dev.reef.readonesia.R;
import com.dev.reef.readonesia.beranda.MyPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView signOut;
    private ImageView rankImage;
    private CircleImageView photoImage;
    private TextView editProfileView;
    private TextView nameView;
    private TextView emailView;
    private TextView usernameView;
    private TextView pointView;
    private TextView rankName;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    private String[] rankNameList = {"Warrior Reader", "Elite Reader", "Master Reader", "Epic Reader", "Legend Reader", "Mythic Reader"};

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager_fragment);
        signOut = (ImageView) v.findViewById(R.id.sign_out_button);
        photoImage = (CircleImageView) v.findViewById(R.id.profile_image);
        editProfileView = (TextView) v.findViewById(R.id.edit_profil);
        nameView = (TextView) v.findViewById(R.id.name);
        emailView = (TextView) v.findViewById(R.id.email);
        usernameView = (TextView) v.findViewById(R.id.username);
        pointView = (TextView) v.findViewById(R.id.poin);
        rankImage = (ImageView) v.findViewById(R.id.rank_image);
        rankName = (TextView) v.findViewById(R.id.rank_name);

        ProfilePagerAdapter myPagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        signOut.setOnClickListener(this);

        getData();

        return v;
    }

    public void getData() {
        DatabaseReference profileRef = mDatabase.child("users").child(FirebaseAuth.getInstance().getUid());
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                int poin = 0;

                int poinPemain = 0;
                int poinMusuh = 0;

                for(DataSnapshot historyGameSnapshot : dataSnapshot.child("game_history").getChildren()) {
                    for (DataSnapshot poinSnapshot : historyGameSnapshot.getChildren()) {
                        if(poinSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                            poinPemain = Integer.parseInt(poinSnapshot.getValue().toString());
                        } else {
                            poinMusuh = Integer.parseInt(poinSnapshot.getValue().toString());
                        }
                    }

                    if (poinPemain == poinMusuh) {
                        poin += 20;
                    } else if (poinPemain > poinMusuh) {
                        poin += 50;
                    } else {
                        poin += 0;
                    }
                }

                nameView.setText(name);
                emailView.setText(email);
                usernameView.setText(username);

                if (poin >= 0 && poin <= 500) {
                    rankImage.setImageResource(R.drawable.rank6);
                    rankName.setText(rankNameList[0]);
                } else if (poin >= 0 && poin <= 1000) {
                    rankName.setText(rankNameList[1]);
                    rankImage.setImageResource(R.drawable.rank5);
                } else if (poin >= 0 && poin <= 4000) {
                    rankName.setText(rankNameList[2]);
                    rankImage.setImageResource(R.drawable.rank4);
                } else if (poin >= 0 && poin <= 8000) {
                    rankName.setText(rankNameList[3]);
                    rankImage.setImageResource(R.drawable.rank3);
                } else if (poin >= 0 && poin <= 10000) {
                    rankName.setText(rankNameList[4]);
                    rankImage.setImageResource(R.drawable.rank2);
                } else if (poin >= 0) {
                    rankName.setText(rankNameList[5]);
                    rankImage.setImageResource(R.drawable.rank1);
                }

                pointView.setText(poin + " Poin");

                RequestOptions options = new RequestOptions();
                options.centerCrop();
                options.placeholder(R.drawable.placeholder);

                try {
                    Glide.with(getContext())
                            .load(dataSnapshot.child("imageUrl").getValue().toString())
                            .apply(options)
                            .into(photoImage);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editProfileView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_out_button:

                new AlertDialog.Builder(getContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin ingin keluar?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


                break;
            case R.id.edit_profil:
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
        }
    }
}
