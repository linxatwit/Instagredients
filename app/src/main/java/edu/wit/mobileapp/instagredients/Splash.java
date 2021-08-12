package edu.wit.mobileapp.instagredients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        /****** Create Thread that will sleep for 3 seconds****/
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 3 seconds
                    sleep(1500);

                    // After 1.5 seconds redirect to another intent
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        Intent it=new Intent(Splash.this, MainActivity.class);
                        startActivity(it);
                    }else{
                        Intent it=new Intent(Splash.this, LoginActivity.class);
                        startActivity(it);
                    }


                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();

    }
}