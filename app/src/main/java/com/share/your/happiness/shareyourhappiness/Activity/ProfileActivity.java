package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String userId;
    private StorageReference storageReference;
    private ImageView userIv;
    private TextView firstNameTv, lastname, emailTv;
    private ProgressDialog progress;
    private int checkFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        checkFragment= getIntent().getIntExtra("checkFragment", 0);
        init();
        getDataFromfirebase();
        checkFragment =  getIntent().getIntExtra("checkFragment", 0);
        Toast.makeText(this, String.valueOf(checkFragment), Toast.LENGTH_SHORT).show();
    }

    private void getDataFromfirebase() {
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {



                    User user = dataSnapshot.getValue(User.class);
                    Picasso.get().load(user.getProfilePicture()).into(userIv);
                    firstNameTv.setText("First Name: "+user.getFirstName());
                    lastname.setText("Last Name: "+user.getLastName());
                    emailTv.setText("Email: "+user.getEmail());




                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }

    private void init() {
        userIv = findViewById(R.id.userIV);
        firstNameTv = findViewById(R.id.fristNameTv);
        lastname = findViewById(R.id.lastNameTv);
        emailTv = findViewById(R.id.emailTv);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("checkFragment",checkFragment);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}