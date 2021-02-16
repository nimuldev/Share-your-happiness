package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
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
import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.share.your.happiness.shareyourhappiness.adapter.AllFoodListAdapter;
import com.share.your.happiness.shareyourhappiness.adapter.RequestAdapter;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String imageUrl, toRequestUserId, fromRequestUserId, foodName;
    private ProgressDialog progress;
    List<Request> requestList;
    RecyclerView recyclerView;
    RequestAdapter adapter;
    private String userId;
    private String requestType;
    private int totalReceive = 0;
    private int totalDelivery = 0;
    private CardView cardView;
    private TextView tReceiveTv, tDeliveryTv;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();
        dataSet();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataSet();
                refreshLayout.setRefreshing(false);
            }
        });


    }

    private void dataSet() {
        requestList.clear();
        adapter.notifyDataSetChanged();
        final DatabaseReference dataRef = databaseReference.child("users");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    final User user = data.getValue(User.class);
                    dataRef.child(user.getUid()).child("Request").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot data2 : snapshot.getChildren()) {

                                Request request = data2.getValue(Request.class);


                                if (requestType.equals("requestOpen") && request.getToRequestUserId().equals(userId) && !request.getStatus().equals("Rejected") && !request.getStatus().equals("Received")) {

                                    setTitle("Request");
                                    requestList.add(request);


                                } else if (requestType.equals("otherRequest") && request.getFromRequestUserId().equals(userId) && !request.getStatus().equals("Received")) {
                                    setTitle("Send Request");
                                    requestList.add(request);

                                } else if (request.getStatus().equals("Rejected") && request.getToRequestUserId().equals(userId) && requestType.equals("Rejected")) {

                                    setTitle("Request Rejected");
                                    requestList.add(request);

                                } else if (request.getStatus().equals("Received") && request.getToRequestUserId().equals(userId) && requestType.equals("Received") ||
                                        request.getStatus().equals("Received") && request.getFromRequestUserId().equals(userId) && requestType.equals("Received")) {


                                    setTitle("Deal");
                                    requestList.add(request);


                                }

                            }
                            adapter.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void init() {
        refreshLayout = findViewById(R.id.refresh);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        cardView = findViewById(R.id.deliveryData);
        tDeliveryTv = findViewById(R.id.deliveryTv);
        tReceiveTv = findViewById(R.id.receiveTv);
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();
        requestList = new ArrayList<>();
        requestType = getIntent().getStringExtra("Request");
        recyclerView = findViewById(R.id.requestRecycling);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAdapter(this, requestList, 1, requestType);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (requestType.equals("requestOpen")) {
            setTitle("Request");

        } else if (requestType.equals("otherRequest")) {
            setTitle("Request Send");

        } else if (requestType.equals("Received")) {
            setTitle("Deal");
            cardView.setVisibility(View.VISIBLE);
            tDeliveryTv.setText("0");
            tReceiveTv.setText("0");
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            changeDataSet();
        } else if (requestType.equals("Rejected")) {
            setTitle("Rejected List");
        }
    }

    public void test(String userId, String requestType) {


    }

    private void changeDataSet() {
        DatabaseReference dataRef1 = FirebaseDatabase.getInstance().getReference();

        dataRef1.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String delivery = snapshot.child("totalDelivery").getValue().toString();
                    String receive = snapshot.child("totalReceive").getValue().toString();
                    tDeliveryTv.setText("");
                    tDeliveryTv.setText(delivery);
                    tReceiveTv.setText("");
                    tReceiveTv.setText(String.valueOf(receive));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}