package com.dev.reef.readonesia.Beradu;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.reef.readonesia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameKuisFragment extends Fragment implements View.OnClickListener {
    private int noQuestion;
    private String idPost;

    private TextView questionView;
    private TextView noQuestionsView;
    private TextView answerA;
    private TextView answerB;
    private TextView answerC;
    private TextView answerD;

    public String answer;


    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    public GameKuisFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public GameKuisFragment(int noQuestion, String idPost) {
        // Required empty public constructor
        this.noQuestion = noQuestion;
        this.idPost = idPost;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game_kuis, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        questionView = (TextView) v.findViewById(R.id.question);
        noQuestionsView = (TextView) v.findViewById(R.id.no_questions);
        answerA = (TextView) v.findViewById(R.id.jawaban_a);
        answerB = (TextView) v.findViewById(R.id.jawaban_b);
        answerC = (TextView) v.findViewById(R.id.jawaban_c);
        answerD = (TextView) v.findViewById(R.id.jawaban_d);

        getData();

        answerA.setOnClickListener(this);
        answerB.setOnClickListener(this);
        answerC.setOnClickListener(this);
        answerD.setOnClickListener(this);


        return v;
    }

    public void getData() {
        DatabaseReference questionsRef = mDatabase.child("game_post").child(idPost).child("questions").child(Integer.toString(noQuestion));
        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(getContext(), idPost  + " " + noQuestion, Toast.LENGTH_SHORT).show();
                questionView.setText(dataSnapshot.child("question").getValue().toString());
                noQuestionsView.setText("Pertanyaan ke-" + noQuestion);

                answerA.setText(dataSnapshot.child("option1").getValue().toString());
                answerB.setText(dataSnapshot.child("option2").getValue().toString());
                answerC.setText(dataSnapshot.child("option3").getValue().toString());
                answerD.setText(dataSnapshot.child("option4").getValue().toString());

                ((GameActivity)getActivity()).questionAnswer = dataSnapshot.child("answer").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jawaban_a:
                ((GameActivity)getActivity()).answer = "option1";
                answerA.getBackground().setColorFilter(Color.parseColor("#F8B23A"), PorterDuff.Mode.SRC_ATOP);
                answerB.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerC.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerD.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.jawaban_b:
                ((GameActivity)getActivity()).answer = "option2";
                answerA.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerB.getBackground().setColorFilter(Color.parseColor("#F8B23A"), PorterDuff.Mode.SRC_ATOP);
                answerC.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerD.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.jawaban_c:
                ((GameActivity)getActivity()).answer = "option3";
                answerA.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerB.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerC.getBackground().setColorFilter(Color.parseColor("#F8B23A"), PorterDuff.Mode.SRC_ATOP);
                answerD.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.jawaban_d:
                ((GameActivity)getActivity()).answer = "option4";
                answerA.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerB.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerC.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                answerD.getBackground().setColorFilter(Color.parseColor("#F8B23A"), PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

    public String getAnswer() {
        return this.answer;
    }
}
