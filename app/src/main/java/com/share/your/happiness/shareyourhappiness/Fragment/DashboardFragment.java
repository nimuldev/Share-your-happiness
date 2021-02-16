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
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Activity.MainActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.share.your.happiness.shareyourhappiness.adapter.AllFoodListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class DashboardFragment extends Fragment {
    private String userId;
    private TextView checkData;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri uri;
    private StorageReference storageReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    List<ListFood> listFoods;
    List<ListFood> tempFoods;
    RecyclerView recyclerView;
    AllFoodListAdapter adapter;
    String division, district, upazila;
    private TextView massageTv;
    private SwipeRefreshLayout refreshLayout;


    public DashboardFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(view);


        Bundle bundle = this.getArguments();
        division = bundle.getString("division");
        district = bundle.getString("district");
        upazila = bundle.getString("upazila");


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();
        foolList();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                massageTv.setVisibility(View.GONE);
                division ="Division";
                district = "District";
                upazila ="Upazila";

                foolList();
                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void foolList() {

        listFoods.clear();
        adapter.notifyDataSetChanged();
        massageTv.setVisibility(View.GONE);
        final DatabaseReference dataRef = databaseReference.child("users");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int c = 0;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        User user = data.getValue(User.class);


                        dataRef.child(user.getUid()).child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {

                                    int abc = 0;
                                    for (DataSnapshot data2 : snapshot.getChildren()) {


                                        ListFood listFood = data2.getValue(ListFood.class);

                                        if (listFood.getAvailable().equals("yes")) {

                                            if (division.equals("Division") && district.equals("District") && upazila.equals("Upazila")) {
                                                abc++;
                                                listFoods.add(listFood);
                                                adapter.notifyDataSetChanged();
                                                massageTv.setVisibility(View.GONE);

                                            } else if (!division.equals("Division") && district.equals("District") && upazila.equals("Upazila")) {
                                                if (listFood.getDivision().equals(division)) {
                                                    massageTv.setVisibility(View.GONE);
                                                    abc++;
                                                    listFoods.add(listFood);
                                                    adapter.notifyDataSetChanged();
                                                }
//                                                else {
//                                                    Toast.makeText(getContext(), "def", Toast.LENGTH_SHORT).show();
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            } else if (!division.equals("Division") && !district.equals("District") && upazila.equals("Upazila")) {
                                                if (listFood.getDivision().equals(division) && listFood.getDistirct().equals(district)) {
                                                    massageTv.setVisibility(View.GONE);
                                                    abc++;
                                                    listFoods.add(listFood);
                                                    adapter.notifyDataSetChanged();

                                                }
//                                                else {
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            } else if (!division.equals("Division") && !district.equals("District") && !upazila.equals("Upazila")) {
                                                if (listFood.getDivision().equals(division) && listFood.getDistirct().equals(district) && listFood.getUpazila().equals(upazila)) {
                                                    massageTv.setVisibility(View.GONE);
                                                    abc++;
                                                    listFoods.add(listFood);
                                                    adapter.notifyDataSetChanged();
                                                }
//                                                else {
//                                                    massageTv.setVisibility(View.VISIBLE);
//                                                }

                                            }

                                            if (listFoods.size() == 0) {
                                                massageTv.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        if (listFoods.size() == 0) {
                                            massageTv.setVisibility(View.VISIBLE);
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


        massageTv.setVisibility(View.GONE);


    }


    private void init(View view) {
        massageTv = view.findViewById(R.id.massage);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
        listFoods = new ArrayList<>();
        tempFoods = new ArrayList<>();

        recyclerView = view.findViewById(R.id.myFoodRecycling);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AllFoodListAdapter(listFoods, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout = view.findViewById(R.id.refresh);
    }
}