package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.MyFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class FoodInfoActivity extends AppCompatActivity {

    private EditText nameEt, houseEt, areaEt, PostEt, descriptionEt, UpazilaEt, districtEt, divisionEt, frizzingEt;
    private ImageView foodIv;
    private EditText dateEt, phoneEt;
    private String image;
    private String requestName, userId, requestFoodId, requestFoodIv, requestUserIv;
    private TextInputLayout commentET,numberEt;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String imageUrl, toRequestUserId, fromRequestUserId, foodName;
    private ProgressDialog progress;
    String requestId, ToRequestUserIv, ToRequestUserName;
    private Button submitBTN;
    LinearLayout notExist,exist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        init();
        setData();


       getMyInfo();
        foodExist();
       checkItem();
    }

    private void foodExist() {
        final DatabaseReference dataRef = databaseReference.child("users");

        dataRef.child(toRequestUserId).child("Food").child(requestFoodId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    notExist.setVisibility(View.VISIBLE);
                    exist.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(FoodInfoActivity.this, "Item Remove Right Now From Owner", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getMyInfo() {
        final DatabaseReference dataRef = databaseReference.child("users");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        User user = data.getValue(User.class);


                        dataRef.child(user.getUid()).child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {


                                    for (DataSnapshot data2 : snapshot.getChildren()) {



                                        Request request=data2.getValue(Request.class);

                                        if (request.getFromRequestUserId().equals(fromRequestUserId) && request.getRequestFoodId().equals(requestFoodId)) {

                                            submitBTN.setVisibility(View.GONE);

                                        }


                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setData() {
        image = getIntent().getStringExtra("foodImage");
        ToRequestUserIv = getIntent().getStringExtra("profilePicture");
        toRequestUserId = getIntent().getStringExtra("userId");
        requestFoodId = getIntent().getStringExtra("requestFoodId");
        requestFoodIv = getIntent().getStringExtra("foodImage");
        ToRequestUserName = getIntent().getStringExtra("firstName");
        foodName = getIntent().getStringExtra("foodName");
        nameEt.setText(getIntent().getStringExtra("foodName"));
        UpazilaEt.setText(getIntent().getStringExtra("upazila"));
        districtEt.setText(getIntent().getStringExtra("district"));
        divisionEt.setText(getIntent().getStringExtra("division"));
        frizzingEt.setText(getIntent().getStringExtra("frizzing"));
        phoneEt.setText(getIntent().getStringExtra("phone"));
        dateEt.setText(getIntent().getStringExtra("date"));
        houseEt.setText(getIntent().getStringExtra("house"));

        PostEt.setText(getIntent().getStringExtra("post"));
        descriptionEt.setText(getIntent().getStringExtra("description"));
        areaEt.setText(getIntent().getStringExtra("area"));
        Picasso.get().load(getIntent().getStringExtra("foodImage")).into(foodIv);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        fromRequestUserId = currentUser.getUid();
        if (fromRequestUserId.equals(toRequestUserId)) {
            submitBTN.setVisibility(View.GONE);
        }



    }
    public void checkItem(){
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(toRequestUserId).child("Food").child(requestFoodId);
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    ListFood listFood= snapshot.getValue(ListFood.class);

                    image = listFood.getFoodPicture(); //getIntent().getStringExtra("foodImage");
                    ToRequestUserIv = listFood.getProfilePicture();//getIntent().getStringExtra("profilePicture");
                    toRequestUserId =  listFood.getUserId(); // getIntent().getStringExtra("userId");
                    requestFoodId =listFood.getFoodId(); //getIntent().getStringExtra("requestFoodId");
                    requestFoodIv =listFood.getFoodPicture(); //getIntent().getStringExtra("foodImage");
                    ToRequestUserName = listFood.getFirstName(); //getIntent().getStringExtra("firstName");
                    foodName = listFood.getName(); //getIntent().getStringExtra("foodName");
                    nameEt.setText(foodName);
                    UpazilaEt.setText(listFood.getUpazila());
                    districtEt.setText(listFood.getDistirct());
                    divisionEt.setText(listFood.getDivision());
                    frizzingEt.setText(listFood.getFrizzing());
                    phoneEt.setText(listFood.getPhone());
                    dateEt.setText(listFood.getDate());
                    houseEt.setText(listFood.getHouse());

                    PostEt.setText(listFood.getPost());
                    descriptionEt.setText(listFood.getDescription());
                    areaEt.setText(listFood.getArea());
                    Picasso.get().load(listFood.getFoodPicture()).into(foodIv);

                    if (listFood.getAvailable().equals("No")) {
                        submitBTN.setVisibility(View.GONE);

                        notExist.setVisibility(View.VISIBLE);
                        exist.setVisibility(View.GONE);


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        submitBTN = findViewById(R.id.submitBTN);
        notExist=findViewById(R.id.notExist);
        exist=findViewById(R.id.exist);

        nameEt = findViewById(R.id.nameEt);
        foodIv = findViewById(R.id.foodIv);
        nameEt.setFocusable(false);
        UpazilaEt = findViewById(R.id.UpazilaEt);
        UpazilaEt.setFocusable(false);
        districtEt = findViewById(R.id.districtEt);
        districtEt.setFocusable(false);
        divisionEt = findViewById(R.id.divisionEt);
        divisionEt.setFocusable(false);
        frizzingEt = findViewById(R.id.frizzingEt);
        frizzingEt.setFocusable(false);
        phoneEt = findViewById(R.id.phoneEt);
        phoneEt.setFocusable(false);
        dateEt = findViewById(R.id.dateEt);
        dateEt.setFocusable(false);
        houseEt = findViewById(R.id.houseEt);
        houseEt.setFocusable(false);
        areaEt = findViewById(R.id.areaEt);
        areaEt.setFocusable(false);
        PostEt = findViewById(R.id.PostEt);
        PostEt.setFocusable(false);
        descriptionEt = findViewById(R.id.descriptionEt);
        descriptionEt.setFocusable(false);


        phoneEt.setFocusable(false);
        dateEt.setText("Nahid");

        phoneEt.setText("01834959055");
    }

    public void request(View views) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         View view = getLayoutInflater().inflate(R.layout.request_comment, null);
        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.show();
        Button sendBtn = view.findViewById(R.id.sentBtn);
        commentET = view.findViewById(R.id.commentEt);
        numberEt = view.findViewById(R.id.numberEt);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentET.getEditText().getText().toString();
                final String number=numberEt.getEditText().getText().toString();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                fromRequestUserId = currentUser.getUid();

                if(number.equals("")){
                    Toast.makeText(FoodInfoActivity.this, "Number is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(number.length()==11){
                    send(comment,number);


                    dialog.dismiss();
                    progress = new ProgressDialog(FoodInfoActivity.this);
                    progress.setMessage("Please Wait");
                    progress.setTitle("Add Food");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(true);
                    progress.show();
                    progress.setCancelable(true);

                }
                else {
                    Toast.makeText(FoodInfoActivity.this, "Number is Incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void send(final String comment,final String number) {




        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(fromRequestUserId);

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    User user = dataSnapshot.getValue(User.class);
                    requestName = user.getFirstName();
                    requestUserIv = user.getProfilePicture();

                    upload(comment, requestUserIv, requestName,number);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    public void upload(String comment, String imageUrl, String requestName,String number) {


        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String status = "Pending";

        DatabaseReference requestData = databaseReference.child("users").child(toRequestUserId).child("Request");
        requestId = requestData.push().getKey();


        HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("requestName", requestName);
        requestInfo.put("requestId", requestId);
        requestInfo.put("toRequestUserId", toRequestUserId);
        requestInfo.put("requestFoodId", requestFoodId);
        requestInfo.put("requestFoodIv", requestFoodIv);
        requestInfo.put("fromRequestUserId", fromRequestUserId);
        requestInfo.put("fromRequestUserIv", imageUrl);
        requestInfo.put("time", currentTime);
        requestInfo.put("date", currentDate);
        requestInfo.put("status", status);
        requestInfo.put("comment", comment);
        requestInfo.put("foodName", foodName);
        requestInfo.put("ToRequestUserIv", ToRequestUserIv);
        requestInfo.put("ToRequestUserName", ToRequestUserName);
        requestInfo.put("fromUserNumber", number);




        requestData.child(requestId).setValue(requestInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {



                    Intent intent = new Intent(FoodInfoActivity.this, MainActivity.class);

                    intent.putExtra("check", 2);
                    progress.dismiss();

                    startActivity(intent);

                } else {
                    progress.dismiss();
                    Toast.makeText(FoodInfoActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(FoodInfoActivity.this, MainActivity.class);

        intent.putExtra("check", 2);


        startActivity(intent);
    }

}