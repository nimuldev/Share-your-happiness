package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Developer.DevelopersActivity;
import com.share.your.happiness.shareyourhappiness.Fragment.DashboardFragment;
import com.share.your.happiness.shareyourhappiness.Fragment.HomeFragment;
import com.share.your.happiness.shareyourhappiness.Fragment.MoreFragment;

import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;

public class MainActivity extends AppCompatActivity {

    private String email, password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int check = 0;
    String userId, firstName;
    String lastnameP, firstnamPe, emailP, pictureP;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    int select_fragment = 0;
    private int checkFragment = 0;
    private Spinner divisionSP, districtSP, upazilaSP;
    private String[] division, district, upazila;
    ArrayAdapter<String> adapter;
    private ProgressDialog progress;
    BottomNavigationView bottomNavigationView;
    private int checkSpinner = 0;
    Menu myMenu;
    Menu menu;
    String confirmEmail;
    Bundle bundle;
    String frag;
    int checkFrag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = getIntent().getStringExtra("firstName");
        frag = getIntent().getStringExtra("checkFragment");

        checkFragment = getIntent().getIntExtra("check", 0);
        init();


        if (checkFragment == 1) {

            View view = bottomNavigationView.findViewById(R.id.nav_self);

            view.performClick();
            select_fragment = 2;
            check = 0;
            setTitle("Home");
            replaceFragment(new HomeFragment(), "Division", "District", "Upazila");
        } else {

            select_fragment = 1;
            setTitle("Dashboard");
            replaceFragment(new DashboardFragment(), "Division", "District", "Upazila");
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_dashboard:
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().show();
                        }
                        checkFragment = 0;
                        select_fragment = 1;
                        replaceFragment(new DashboardFragment(), "Division", "District", "Upazila");
                        setTitle("Dashboard");
                        return true;

                    case R.id.nav_self:
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().show();
                        }
                        checkFragment = 2;
                        select_fragment = 2;
                        replaceFragment(new HomeFragment(), "Division", "District", "Upazila");
                        setTitle("Home");
                        return true;
                    case R.id.more:
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().hide();
                        }

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frameLayout, new MoreFragment());
                        ft.commit();
                        select_fragment = 3;
                        setTitle("More");
                        invalidateOptionsMenu();

                        return true;

                }


                return false;
            }


        });
    }


    private void init() {

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        check = getIntent().getIntExtra("check", 0);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        checkFragment = getIntent().getIntExtra("check", 0);
        bottomNavigationView = findViewById(R.id.bottomnav);


    }


    public void replaceFragment(Fragment fragment, String division, String district, String upazila) {
        bundle = new Bundle();

        bundle.putString("division", division);
        bundle.putString("district", district);
        bundle.putString("upazila", upazila);
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();

    }

    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.search:


                if (select_fragment == 3) {

                    Toast.makeText(this, "Not Working Here", Toast.LENGTH_SHORT).show();
                } else {
                    search();
                }

                return true;
            case R.id.profile:


                intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("checkFragment", checkFragment);
                startActivity(intent);

                return true;

            case R.id.developer:

                intent = new Intent(this, DevelopersActivity.class);
                intent.putExtra("checkFragment", checkFragment);
                startActivity(intent);


                return true;

            case R.id.delete:

                deleteAccount();

//                intent=  new Intent(this, DevelopersActivity.class);
//                intent.putExtra("checkFragment",checkFragment);
//                startActivity(intent);


                return true;

            case R.id.logOut:
                sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.clear();
                editor.apply();
                Toast.makeText(this, "Remove account from this device", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);


                sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                editor = sharedPreferences.edit();

                editor.clear();
                editor.apply();

                finish();


                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void deleteAccount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        View view = getLayoutInflater().inflate(R.layout.deletecaccount, null);

        Button deleteBtn = view.findViewById(R.id.deleteBtn);
        final EditText passwordET = view.findViewById(R.id.passwordET);
        final EditText emailEt = view.findViewById(R.id.emailET);
        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.show();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = emailEt.getText().toString();
                String pass = passwordET.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {




                            final DatabaseReference dataRef2 = databaseReference.child("users");
                            dataRef2.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                            final User user = data.getValue(User.class);

                                            if (user.getUid().equals(userId)) {

                                                 StorageReference photoRef = FirebaseStorage.getInstance().getReference(user.getProfilePicture());
                                                photoRef.delete();
                                            }



                                        }


                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



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

                                                            Request request = data2.getValue(Request.class);

                                                            if(request.getFromRequestUserId().equals(userId) && !request.getStatus().equals("Receive")){

                                                                final DatabaseReference dataRq = FirebaseDatabase.getInstance().getReference("users").child(request.getToRequestUserId()).child("Request").child(request.getRequestId());
                                                                dataRq.removeValue();
                                                            }



                                                        }


//
//
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


                            final DatabaseReference dataRef1 = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            dataRef1.removeValue();

                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            currentUser.delete();

                            sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                            editor = sharedPreferences.edit();

                            editor.clear();
                            editor.apply();
                            Toast.makeText(MainActivity.this, "Successfully Delete Account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, SignInActivity.class);

                            startActivity(intent);
                            dialog.dismiss();
                        } else {

                            Toast.makeText(MainActivity.this, "Something going to wrong", Toast.LENGTH_SHORT).show();


                        }

                    }
                });


            }
        });

    }


    private void search() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        System.out.print("2");
        View view = getLayoutInflater().inflate(R.layout.search, null);

        Button search_Btn = view.findViewById(R.id.search_btn);
        Button close_Btn = view.findViewById(R.id.close_btn);
        divisionSP = view.findViewById(R.id.divisionSP);
        districtSP = view.findViewById(R.id.districtSP);
        upazilaSP = view.findViewById(R.id.upazilaSP);


        divisionSP.setEnabled(true);
        upazilaSP.setEnabled(true);
        districtSP.setEnabled(true);
        districtSP.setFocusable(false);
        upazilaSP.setFocusable(false);

        division = getResources().getStringArray(R.array.division);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, division);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        divisionSP.setAdapter(adapter);


        divisionSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();


                if (!text.equals("Division") && checkSpinner == 0) {

                    String no = String.valueOf(check);

                    if (text.equals("Dhaka")) {
                        district = getResources().getStringArray(R.array.Dhaka_district);

                    } else if (text.equals("Barishal")) {
                        district = getResources().getStringArray(R.array.Barishal_district);
                    } else if (text.equals("Chattogram")) {
                        district = getResources().getStringArray(R.array.Chattogram_district);
                    } else if (text.equals("Khulna")) {
                        district = getResources().getStringArray(R.array.Khulna_district);
                    } else if (text.equals("MYMENSINGH")) {
                        district = getResources().getStringArray(R.array.Mymensingh_district);
                    } else if (text.equals("Rajshahi")) {
                        district = getResources().getStringArray(R.array.Rajshahi_district);
                    } else if (text.equals("Rangpur")) {
                        district = getResources().getStringArray(R.array.Rangpur_district);
                    } else if (text.equals("Sylhet")) {
                        district = getResources().getStringArray(R.array.Sylhet_district);
                    }

                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, district);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    districtSP.setAdapter(adapter);
                    divisionSP.setEnabled(false);
                    districtSP.setFocusable(true);

                    //   spinner(district, "District");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        districtSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();

                if (!text.equals("District")) {
                    districtSP.setEnabled(false);
                    upazilaSP.setFocusable(true);
                    selectedUpazila(text);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        upazilaSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();

                if (!text.equals("Upazila")) {
                    upazilaSP.setEnabled(false);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.show();

        close_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String select_division = divisionSP.getSelectedItem().toString();
                String select_distirct = districtSP.getSelectedItem().toString();
                String select_upazila = upazilaSP.getSelectedItem().toString();

                if (select_fragment == 1) {

                    replaceFragment(new DashboardFragment(), select_division, select_distirct, select_upazila);

                } else if (select_fragment == 2) {
                    replaceFragment(new HomeFragment(), select_division, select_distirct, select_upazila);
                }

            }
        });
    }


    private void selectedUpazila(String text) {

        if (text.equals("Tangail")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Tangail_upazila);
        } else if (text.equals("Shariatpur")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Shariatpur_upazila);
        } else if (text.equals("Dhaka")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Dhaka_upazila);
        } else if (text.equals("Rajbari")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Rajbari_upazila);
        } else if (text.equals("Narshingdi")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Narshingdi_upazila);
        } else if (text.equals("Narayanganj")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Narayanganj_upazila);
        } else if (text.equals("Munshiganj")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Munshiganj_upazila);
        } else if (text.equals("Manikganj")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Manikganj_upazila);
        } else if (text.equals("Madaripur")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Madaripur_upazila);
        } else if (text.equals("Kishoreganj")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Kishoreganj_upazila);
        } else if (text.equals("Gopalganj")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Gopalganj_upazila);
        } else if (text.equals("Gazipur")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Gazipur_upazila);
        } else if (text.equals("Faridpur")) {
            upazila = getResources().getStringArray(R.array.Dhaka_Faridpur_upazila);
        }
        //barisal
        else if (text.equals("Pirojpur")) {
            upazila = getResources().getStringArray(R.array.Barishal_Pirojpur_upazila);
        } else if (text.equals("Patuakhali")) {
            upazila = getResources().getStringArray(R.array.Barishal_Patuakhali_upazila);
        } else if (text.equals("Jhalokathi")) {
            upazila = getResources().getStringArray(R.array.Barishal_Jhalokathi_upazila);
        } else if (text.equals("Barishal")) {
            upazila = getResources().getStringArray(R.array.Barishal_Barishal_upazila);
        } else if (text.equals("Bhola")) {
            upazila = getResources().getStringArray(R.array.Barishal_Bhola_upazila);
        } else if (text.equals("Barguna ")) {
            upazila = getResources().getStringArray(R.array.Barishal_Barguna_upazila);
        }
//ctg
        else if (text.equals("Feni")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Feni_upazila);
        } else if (text.equals("Rangamati")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Rangamati_upazila);
        } else if (text.equals("Noakhali")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Noakhali_upazila);
        } else if (text.equals("Laxmipur")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Laxmipur_upazila);
        } else if (text.equals("Cumilla")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Cumilla_upazila);
        } else if (text.equals("Khagrachari")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Khagrachari_upazila);
        } else if (text.equals("Coxs bazar")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Coxs_bazarr_upazila);
        } else if (text.equals("Chattogram")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Chattogram_upazila);
        } else if (text.equals("Chandpur")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Chandpur_upazila);
        } else if (text.equals("Bandarban")) {
            upazila = getResources().getStringArray(R.array.Chattogram_Bandarban_upazila);
        } else if (text.equals("B.baria")) {
            upazila = getResources().getStringArray(R.array.Chattogram_B_baria_upazila);
        }

//Khulna
        else if (text.equals("Narail")) {
            upazila = getResources().getStringArray(R.array.Khulna_Narail_upazila);
        } else if (text.equals("Meherpur")) {
            upazila = getResources().getStringArray(R.array.Khulna_Meherpur_upazila);
        } else if (text.equals("Magura")) {
            upazila = getResources().getStringArray(R.array.Khulna_Magura_upazila);
        } else if (text.equals("Kushtia")) {
            upazila = getResources().getStringArray(R.array.Khulna_Kushtia_upazila);
        } else if (text.equals("Khulna")) {
            upazila = getResources().getStringArray(R.array.Khulna_Khulna_upazila);
        } else if (text.equals("Jashore")) {
            upazila = getResources().getStringArray(R.array.Khulna_Jashore_upazila);
        } else if (text.equals("Bagerhat")) {
            upazila = getResources().getStringArray(R.array.Khulna_Bagerhat_upazila);
        } else if (text.equals("Chuadanga")) {
            upazila = getResources().getStringArray(R.array.Khulna_Chuadanga_upazila);
        } else if (text.equals("Jhenaidah")) {
            upazila = getResources().getStringArray(R.array.Khulna_Jhenaidah_upazila);
        } else if (text.equals("Satkhira")) {
            upazila = getResources().getStringArray(R.array.Khulna_Satkhira_upazila);
        }
        //Rangpur

        else if (text.equals("Thakurgaon")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Thakurgaon_upazila);
        } else if (text.equals("Rangpur")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Rangpur_upazila);
        } else if (text.equals("Panchagarh")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Panchagarh_upazila);
        } else if (text.equals("Lalmonirhat")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Lalmonirhat_upazila);
        } else if (text.equals("Nilphamari")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Nilphamari_upazila);
        } else if (text.equals("Kurigram")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Kurigram_upazila);
        } else if (text.equals("Gaibandha")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Gaibandha_upazila);
        } else if (text.equals("Dinajpur")) {
            upazila = getResources().getStringArray(R.array.Rangpur_Dinajpur_upazila);
        }
//Sylhet
        else if (text.equals("Sylhet")) {
            upazila = getResources().getStringArray(R.array.Sylhet_Sylhet_upazila);
        } else if (text.equals("Sunamganj")) {
            upazila = getResources().getStringArray(R.array.Sylhet_Sunamganj_upazila);
        } else if (text.equals("Moulvibazar")) {
            upazila = getResources().getStringArray(R.array.Sylhet_Moulvibazar_upazila);
        } else if (text.equals("Habiganj")) {
            upazila = getResources().getStringArray(R.array.Sylhet_Habiganj_upazila);
        }

        //  Rajshahi
        else if (text.equals("Sirajganj")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Sirajganj_upazila);
        } else if (text.equals("Rajshahi")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Rajshahi_upazila);
        } else if (text.equals("Pabna")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Pabna_upazila);
        } else if (text.equals("Natore")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Natore_upazila);
        } else if (text.equals("Joypurhat")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Joypurhat_upazila);
        } else if (text.equals("Bogura")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Joypurhat_upazila);
        } else if (text.equals("Naogaon")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Naogaon_upazila);
        } else if (text.equals("nawabganj")) {
            upazila = getResources().getStringArray(R.array.Rajshahi_Nawabganj_upazila);
        }

        //Mymensingh
        else if (text.equals("Sherpur")) {
            upazila = getResources().getStringArray(R.array.Mymensingh_Sherpur_upazila);
        } else if (text.equals("Netrokona")) {
            upazila = getResources().getStringArray(R.array.Mymensingh_Netrokona_upazila);
        } else if (text.equals("Mymensingh")) {
            upazila = getResources().getStringArray(R.array.Mymensingh_Mymensingh_upazila);
        } else if (text.equals("Jamalpur")) {
            upazila = getResources().getStringArray(R.array.Mymensingh_Jamalpur_upazila);
        }


        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, upazila);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upazilaSP.setAdapter(adapter);
    }


}
