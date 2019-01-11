package com.dev.reef.readonesia;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.reef.readonesia.Beradu.BeraduFragment;
import com.dev.reef.readonesia.beranda.HomeFragment;
import com.dev.reef.readonesia.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private TextView titleToolbar;
    private BottomNavigationView navigationView;
    private FirebaseUser firebaseUser;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.beranda:
                        Fragment newFragment = new HomeFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        return true;
                    case R.id.beradu:
                        newFragment = new BeraduFragment();
                        transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        return true;
                    case R.id.profil:
                        newFragment = new ProfileFragment();
                        transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        return true;
                }
                return false;
            }
        });

        setFirstFragment();
    }

    public void setFirstFragment() {
        Fragment newFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
