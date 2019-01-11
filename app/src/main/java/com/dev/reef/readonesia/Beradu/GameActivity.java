package com.dev.reef.readonesia.Beradu;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    public String idPost;
    public String idGameRoom;
    private String idMusuh;

    private TextView timer;
    private TextView musuhStatus;
    private TextView pemainStatus;

    private LinearLayout loading;

    public static int poin;
    public static String answer;
    public static String questionAnswer;

    public Button button;

    Dialog myDialog;

    private static int counter;

    CountDownTimer yourCountDownTimer;

    DatabaseReference db;
    FirebaseAuth auth;

    final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    final Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        timer = findViewById(R.id.timer);
        musuhStatus = findViewById(R.id.musuh_status);
        pemainStatus = findViewById(R.id.pemain_status);
        button = findViewById(R.id.button);
        loading = findViewById(R.id.loading_matchmaking);

        counter = 0;

        idPost = "";
        idGameRoom = "";
        idMusuh = "";
        questionAnswer = "-";
        answer = "";
        poin = 0;
        try {
            idPost = getIntent().getStringExtra("id_post");
            idGameRoom = getIntent().getStringExtra("id_room");
        } catch (Exception e) {
            idPost = "";
            idGameRoom = "";
        }

        firstFragment();

        button.setOnClickListener(this);

        myDialog = new Dialog(this);

        getData();
    }

    public void firstFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new GameBacaFragment(idPost));
        ft.commit();
    }

    public void changeFragment(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, new GameKuisFragment(i, idPost));
        ft.commit();
    }

    public void setCounter(int millis) {
        yourCountDownTimer = new CountDownTimer(millis, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timer.setText(""+String.format("Sisa waktu : %d:%d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

                if (counter < 2){
                    counter+=1;
                    DatabaseReference userDb = db.child("play").child(idPost).child(idGameRoom);
                    userDb.child(idMusuh).setValue("belum");
                    userDb.child(auth.getUid()).setValue("belum");
                    changeFragment(counter);
                    setCounter(15000);

                } else {
                    if (questionAnswer.equalsIgnoreCase(answer)) {
                        poin += 10;
                    }

                    showPopup();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (counter < 2){
                    yourCountDownTimer.cancel();
                    counter+=1;

                    if (questionAnswer.equalsIgnoreCase(answer)) {
                        poin += 10;
                    }

                    changeFragment(counter);
                    setCounter(15000);

                } else {
                    yourCountDownTimer.cancel();
                    if (questionAnswer.equalsIgnoreCase(answer)) {
                        poin += 10;
                    }
                    showPopup();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
                Toast.makeText(this, "Kamu harus menyelesaikan game dulu", Toast.LENGTH_SHORT).show();

    }

    public void showPopup() {
        loading.setVisibility(View.VISIBLE);

        final DatabaseReference dbUserGame = db.child("game").child("play").child(idPost);
        dbUserGame.child(idGameRoom).child("hasil").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                        size++;
                    }
                }

                final DatabaseReference dbPlay = db.child("game").child("history");
                dbPlay.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                            if (roomSnapshot.getKey().equalsIgnoreCase(idGameRoom)) {
                                int poinPemain = 0;
                                int poinMusuh = 0;
                                String winOrLose = "";
                                int poin = 0;

                                int size = 0;
                                for(DataSnapshot userSnapshot : roomSnapshot.getChildren()) {
                                    size++;
                                }

                                if (size < 2) {
                                    return;
                                }
                                
                                
                                for(DataSnapshot userSnapshot : roomSnapshot.getChildren()) {
                                    db.child("users").child(auth.getUid()).child("game_history").child(idGameRoom + formatter.format(date)).child(userSnapshot.getKey()).setValue(userSnapshot.getValue());
                                    if(userSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                                        poinPemain = Integer.parseInt(userSnapshot.getValue().toString());
                                    } else {
                                        poinMusuh = Integer.parseInt(userSnapshot.getValue().toString());
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

                                loading.setVisibility(View.GONE);

                                Button kembali;
                                TextView statusView;
                                TextView poinView;
                                myDialog.setContentView(R.layout.dialog_hasil_game);
                                kembali = myDialog.findViewById(R.id.button_kembali);
                                statusView = myDialog.findViewById(R.id.win_or_lose);
                                poinView = myDialog.findViewById(R.id.poin);
                                kembali.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        db.child("game").child("history").child(idGameRoom).child(auth.getUid()).removeValue();

                                        myDialog.dismiss();
                                        yourCountDownTimer.cancel();
                                        finish();
                                    }
                                });
                                statusView.setText(winOrLose);
                                poinView.setText(Integer.toString(poin));
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                myDialog.show();
                                myDialog.setCancelable(false);
                                myDialog.setCanceledOnTouchOutside(false);

                                dbPlay.removeEventListener(this);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (size < 1) {
                    dbUserGame.child(idGameRoom).child("hasil").child(auth.getUid()).setValue(poin);
                    dbUserGame.child(idGameRoom).child(auth.getUid()).setValue("sudah");
                    return;
                }

                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (!postSnapshot.getKey().equalsIgnoreCase(auth.getUid())) {
                        db.child("game").child("history").child(idGameRoom).child(postSnapshot.getKey()).setValue(postSnapshot.getValue());
                        db.child("game").child("history").child(idGameRoom).child(auth.getUid()).setValue(poin);


                        dbUserGame.child(idGameRoom).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getData() {
        DatabaseReference postRef = db.child("game_post").child(idPost);
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("description").getValue().toString();
                int wordCount = description.split(" ").length;

                double time = wordCount / 200;
                time = Math.ceil(time);
                time = time * 60000;

                setCounter((int)time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference gameRef = db.child("game").child("play").child(idPost).child(idGameRoom);
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (!userSnapshot.getKey().equalsIgnoreCase(auth.getUid()) && !userSnapshot.getKey().equalsIgnoreCase("hasil") && !userSnapshot.getKey().equalsIgnoreCase("status")){
                        if(userSnapshot.getValue().toString().equalsIgnoreCase("sudah")) {
                            musuhStatus.setText("Musuh : " + userSnapshot.getValue().toString());
                            musuhStatus.setTextColor(Color.parseColor("#ff0000"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
