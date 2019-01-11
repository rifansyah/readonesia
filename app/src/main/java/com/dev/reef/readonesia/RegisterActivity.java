package com.dev.reef.readonesia;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dev.reef.readonesia.profile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button signInButton;
    private Button registerButton;
    private FirebaseAuth auth;
    private FrameLayout progressBarLayout;
    private EditText email;
    private EditText password;
    private EditText passwordConfirmation;

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        passwordConfirmation = (EditText) findViewById(R.id.password_confirmation);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        progressBarLayout = (FrameLayout) findViewById(R.id.progress_bar_layout);
        registerButton = (Button) findViewById(R.id.button);

        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button:
                final String emailTemp = email.getText().toString().trim();
                final String passwordTemp = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailTemp)) {
                    Toast.makeText(getApplicationContext(), "Masukkan alamat email yang valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordTemp) || !password.getText().toString().equalsIgnoreCase(passwordConfirmation.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Masukkan password atau password tidak cocok", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    password.setError("password minimal 6 karakter");
                    return;
                }

                progressBarLayout.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(emailTemp, passwordTemp)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBarLayout.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Autentikasi gagal",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    writeNewUser(auth.getUid().toString(), emailTemp);

                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });

                break;
        }
    }

    private void writeNewUser(String userId, String email) {
        User user = new User("Nama Kamu", getRandomName(), email, 0, "https://firebasestorage.googleapis.com/v0/b/readonesia.appspot.com/o/users%2Ffirst-photo.jpg?alt=media&token=10543935-9262-40ca-90be-8dec6cd901ab");

        ref.child("users").child(userId).setValue(user);
    }

    public String getRandomName() {
        String[] firstName = {"warty","hoary", "breezy", "lucid", "quantal", "trusty", "vivid", "xenial"};
        String[] lastName = {"jackalobe", "salamander", "unicorn", "tahr", "vervet", "xerus", "werewolf", "beaver"};

        int first = (int) ((Math.random() * ((7 - 0) + 1)) + 0);
        int last = (int) ((Math.random() * ((7 - 0) + 1)) + 0);

        return firstName[first]+lastName[last];
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBarLayout.setVisibility(View.GONE);
    }
}
