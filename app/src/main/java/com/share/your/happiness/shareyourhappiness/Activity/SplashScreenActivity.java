package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.share.your.happiness.shareyourhappiness.R;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String email="";
    String password="";
    private FirebaseAuth firebaseAuth;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        Handler handler =new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                autoLogIn();
               // finish();
            }
        },4000);
    }

    private boolean autoLogIn() {

        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        password=sharedPreferences.getString("password",null);
        email=sharedPreferences.getString("email",null);

        if ((password !=null) && email!=null){

            sign(email,password);
        }
        else {
            startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
        }

        return true;
    }
    public void sign(final String email, final String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                }

            }
        });
    }

}