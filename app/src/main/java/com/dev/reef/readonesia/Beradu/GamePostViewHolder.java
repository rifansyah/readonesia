package com.dev.reef.readonesia.Beradu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dev.reef.readonesia.Bacaan;
import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GamePostViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView author;
    TextView category;
    TextView date;
    ImageView photo;
    ConstraintLayout layout;
    DatabaseReference db;
    FirebaseAuth auth;
    boolean inRoom;

    public GamePostViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        author = (TextView) itemView.findViewById(R.id.author);
        category = (TextView) itemView.findViewById(R.id.category);
        date = (TextView) itemView.findViewById(R.id.date);
        photo = (ImageView) itemView.findViewById(R.id.image);
        layout = (ConstraintLayout) itemView.findViewById(R.id.layout_beranda_list_item);

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        inRoom = false;
    }

    public void bind(final Bacaan bacaan, final Context context, final LinearLayout loading) {
        title.setText(bacaan.getTitle());
        description.setText(bacaan.getDescription());
        author.setText(bacaan.getAuthor());
        category.setText(bacaan.getCategory());
        date.setText(bacaan.getPublishedAt());

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.placeholder);

        Glide.with(context)
                .load(bacaan.getUrlToImage())
                .apply(options)
                .into(photo);


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);

                final DatabaseReference dbGame = db.child("game").child("room").child(bacaan.getId());
                dbGame.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int size = 0;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (!postSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                                size++;
                            }
                        }

                        if (size < 1) {
                            dbGame.child(auth.getUid()).setValue(auth.getUid());
                            return;
                        }

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if (!postSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                                db.child("game").child("play").child(bacaan.getId()).child(postSnapshot.getValue().toString() + "-" + auth.getUid()).child("status").setValue("ok");
                                db.child("game").child("play").child(bacaan.getId()).child(postSnapshot.getValue().toString() + "-" + auth.getUid()).child(postSnapshot.getValue().toString()).setValue("belum");
                                db.child("game").child("play").child(bacaan.getId()).child(postSnapshot.getValue().toString() + "-" + auth.getUid()).child(auth.getUid()).setValue("belum");


                                dbGame.child(postSnapshot.getValue().toString()).removeValue();
                                dbGame.child(auth.getUid()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                final DatabaseReference dbPlay = db.child("game").child("play").child(bacaan.getId());
                dbPlay.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot userSnapshot : roomSnapshot.getChildren()) {
                                if (userSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                                    loading.setVisibility(View.GONE);
                                    Intent intent = new Intent(context, GameActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("id_post", bacaan.getId());
                                    intent.putExtra("id_room", roomSnapshot.getKey());

                                    context.startActivity(intent);
                                    dbPlay.removeEventListener(this);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
