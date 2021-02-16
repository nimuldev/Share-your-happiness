package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.R;



public class SignInActivity extends AppCompatActivity {

    private TextInputLayout emailET, passwordET;
    private Button signinBTN;
    private String email="";
    String password="";
    private DatabaseReference databaseReference;
    private Uri uri;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progress;
    private String [] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.CONTROL_LOCATION_UPDATES};
    private SharedPreferences sharedPreferences;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
        autoLogIn();
        signinBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailET.getEditText().getText().toString();
                password = passwordET.getEditText().getText().toString();

                if(email.matches(emailPattern) && email.length()>0){

                    saveAccount();

                }
                else {
                    Toast.makeText(SignInActivity.this,"Invalid email address",Toast.LENGTH_SHORT).show();

                }





            }
        });

    }

    private void init() {
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signinBTN = findViewById(R.id.signInBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

    }
    private void saveAccount() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = getLayoutInflater().inflate(R.layout.save,null);

        Button saveBtn = view.findViewById(R.id.saveBtn);
        Button noBtn = view.findViewById(R.id.noBtn);
        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                editor =sharedPreferences.edit();

                editor.putString("password",password);
                editor.putString("email",email);
                editor.apply();
                signIn(email, password);

            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if(!password.equals("")&& !email.equals("")){
                    signIn(email, password);

                }

            }
        });



    }
    private boolean autoLogIn() {

        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        password=sharedPreferences.getString("password",null);
        email=sharedPreferences.getString("email",null);

        if ((password !=null) && email!=null){

            signIn(email,password);
        }
        return true;
    }
    private void signIn(final String email, final String password) {

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait");
        progress.setTitle("LogIn ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        progress.setCancelable(true);


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if (task.isSuccessful()) {

                    Toast.makeText(SignInActivity.this, "LogIn Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    emailET.getEditText().setText("");
                    passwordET.getEditText().setText("");
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(SignInActivity.this, "SignIn is Worng", Toast.LENGTH_SHORT).show();

                    sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                    editor =sharedPreferences.edit();

                    editor.clear();
                    editor.apply();

                }

            }
        });

    }


    public void SignUp(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }
}