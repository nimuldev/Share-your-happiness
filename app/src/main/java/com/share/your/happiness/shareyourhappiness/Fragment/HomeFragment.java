package com.share.your.happiness.shareyourhappiness.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Activity.AddFoodActivity;
import com.share.your.happiness.shareyourhappiness.Activity.MainActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.MyFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.share.your.happiness.shareyourhappiness.adapter.MyFoodAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FloatingActionButton fab;
    private String tripId, userId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri uri;
    private StorageReference storageReference;
    List<MyFood> myFoods;
    RecyclerView recyclerView;
    MyFoodAdapter adapter;
    private TextView massageTv;
    String division, district, upazila;
    private SwipeRefreshLayout refreshLayout;

    public HomeFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        Bundle bundle = this.getArguments();
        division = bundle.getString("division");
        district = bundle.getString("district");
        upazila = bundle.getString("upazila");


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFoodActivity.class);
                startActivity(intent);

            }
        });
        foodlist();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                massageTv.setVisibility(View.GONE);
                division ="Division";
                district = "District";
                upazila ="Upazila";
                foodlist();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void foodlist() {
        myFoods.clear();
        adapter.notifyDataSetChanged();
        final DatabaseReference dataRef = databaseReference.child("users");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        User user = data.getValue(User.class);


                        if (user.getUid().equals(userId)) {
                            dataRef.child(user.getUid()).child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {


                                        for (DataSnapshot data2 : snapshot.getChildren()) {


                                            MyFood myFood = data2.getValue(MyFood.class);

                                            if (division.equals("Division") && district.equals("District") && upazila.equals("Upazila")) {
                                                myFoods.add(myFood);

                                                adapter.notifyDataSetChanged();
                                            } else if (!division.equals("Division") && district.equals("District") && upazila.equals("Upazila")) {
                                                if (myFood.getDivision().equals(division)) {
                                                    myFoods.add(myFood);
                                                    adapter.notifyDataSetChanged();
                                                }
//                                                else {
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            } else if (!division.equals("Division") && !district.equals("District") && upazila.equals("Upazila")) {
                                                if (myFood.getDivision().equals(division) && myFood.getDistirct().equals(district)) {
                                                    myFoods.add(myFood);
                                                    adapter.notifyDataSetChanged();
                                                }
//                                                else {
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            } else if (!division.equals("Division") && !district.equals("District") && !upazila.equals("Upazila")) {
                                                if (myFood.getDivision().equals(division) && myFood.getDistirct().equals(district) && myFood.getUpazila().equals(upazila)) {
                                                    myFoods.add(myFood);
                                                    adapter.notifyDataSetChanged();
                                                }
//                                                else {
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            }

                                            if (myFoods.size() == 0) {
                                                massageTv.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        if (myFoods.size() == 0) {
                                            massageTv.setVisibility(View.VISIBLE);
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void init(View view) {
        massageTv = view.findViewById(R.id.massage);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        myFoods = new ArrayList<>();

        recyclerView = view.findViewById(R.id.myFoodRecycling);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyFoodAdapter(myFoods, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        refreshLayout = view.findViewById(R.id.refreshHome);
    }
}