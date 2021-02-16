package com.share.your.happiness.shareyourhappiness.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class MyFoodActivity extends AppCompatActivity {
    private EditText nameEt, houseEt, areaEt, PostEt, descriptionEt, UpazilaEt, districtEt, divisionEt, frizzingEt;
    private ImageView foodIv;
    private EditText dateEt, phoneEt;
    private String imageUrl, foodId;
    private Spinner divisionSP, districtSP, upazilaSP;
    private RadioButton noR, yesR;
    private RadioGroup radioGR;
    private String frizzing = "";
    private int check = 0;
    private String firstName;
    private String profilePicture;
    private String available = "yes";
    private Button deleteBTN, updateBTN;
    TextInputLayout dateUp;
    String userId;
    private String[] division, district, upazila;
    ArrayAdapter<String> adapter;
    private int checkSpinner = 0;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Uri uri;
    private Bitmap bitmap;
    private StorageReference storageReference;
    //private String downloadLink = null;
    private int checkImage = 0;
    private ProgressDialog progress;
    String downloadLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food);
        init();
        setData();
    }

    private void setData() {
        imageUrl = getIntent().getStringExtra("foodImage");
        profilePicture = getIntent().getStringExtra("profilePicture");
        foodId = getIntent().getStringExtra("foodId");
        Toast.makeText(this, foodId, Toast.LENGTH_SHORT).show();
        firstName = getIntent().getStringExtra("firstName");
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
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();
        nameEt = findViewById(R.id.nameEt);
        foodIv = findViewById(R.id.foodIv);
        dateUp = findViewById(R.id.dateUp);

        //  nameEt.setFocusable(false);
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
        divisionSP = findViewById(R.id.divisionSP);
        districtSP = findViewById(R.id.districtSP);
        upazilaSP = findViewById(R.id.upazilaSP);
        divisionSP.setFocusable(true);
        districtSP.setFocusable(false);
        upazilaSP.setFocusable(false);
        phoneEt.setFocusable(false);
        noR = findViewById(R.id.noR);
        yesR = findViewById(R.id.yesR);
        radioGR = findViewById(R.id.radioGR);
        deleteBTN = findViewById(R.id.deleteBTN);
        updateBTN = findViewById(R.id.updateBTN);

    }

    public void delete(View view) {
       // Toast.makeText(this, foodId, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are You sure?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Food").child(foodId);
                        dataRef.removeValue();



                        StorageReference desertRef = storageReference.child(imageUrl);
                        desertRef.delete();
                        deletRequest();


                    }
                })
                .setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deletRequest() {
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


                                        Toast.makeText(MyFoodActivity.this, "11", Toast.LENGTH_SHORT).show();
                                        Request request = data2.getValue(Request.class);


                                        if (request.getToRequestUserId().equals(userId) && request.getRequestFoodId().equals(foodId)) {
                                            Toast.makeText(MyFoodActivity.this, "sss", Toast.LENGTH_SHORT).show();

                                            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Request").child(request.getRequestId());
                                            dataRef.removeValue();


                                            Intent intent = new Intent(MyFoodActivity.this, MainActivity.class);

                                            intent.putExtra("check", 1);

                                            startActivity(intent);

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

    public void edit(View view) {
        check = 1;
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

        nameEt.setFocusableInTouchMode(true);
        houseEt.setFocusableInTouchMode(true);
        areaEt.setFocusableInTouchMode(true);
        phoneEt.setFocusableInTouchMode(true);
        PostEt.setFocusableInTouchMode(true);

        descriptionEt.setFocusableInTouchMode(true);

        UpazilaEt.setVisibility(View.GONE);
        districtEt.setVisibility(View.GONE);
        divisionEt.setVisibility(View.GONE);
        frizzingEt.setVisibility(View.GONE);
        frizzingEt.setVisibility(View.GONE);
        deleteBTN.setVisibility(View.GONE);
        dateEt.setVisibility(View.GONE);
        dateUp.setVisibility(View.GONE);


        updateBTN.setVisibility(View.VISIBLE);
        upazilaSP.setVisibility(View.VISIBLE);
        districtSP.setVisibility(View.VISIBLE);
        divisionSP.setVisibility(View.VISIBLE);
        radioGR.setVisibility(View.VISIBLE);
        districtSP.setFocusable(false);
        upazilaSP.setFocusable(false);
        if (check == 1) {
            dateEt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                            month = month + 1;
                            String currentDate = year + "/" + month + "/" + day;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                            Date date = null;

                            try {
                                date = dateFormat.parse(currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            dateEt.setText(dateFormat.format(date));


                        }
                    };

                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MyFoodActivity.this, dateSetListener, year, month, day);
                    datePickerDialog.show();
                }
            });

            foodIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getImage();
                }
            });
            spinner();
        }


    }

    public void spinner() {
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

                    adapter = new ArrayAdapter<>(MyFoodActivity.this, android.R.layout.simple_spinner_item, district);
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
                //  Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
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
                //  Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                if (!text.equals("Upazila")) {
                    upazilaSP.setEnabled(false);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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


        adapter = new ArrayAdapter<>(MyFoodActivity.this, android.R.layout.simple_spinner_item, upazila);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upazilaSP.setAdapter(adapter);
    }

    private void getImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MyFoodActivity.this);
        View view = getLayoutInflater().inflate(R.layout.picture, null);
        Button cameraBtn = view.findViewById(R.id.cameraBTN);
        Button gallaryBtn = view.findViewById(R.id.galleryBTN);


        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.show();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();
                check = 1;

                dialog.dismiss();
            }
        });

        gallaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
                check = 2;
                dialog.dismiss();
            }
        });


    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    private void getImageFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {
                uri = data.getData();
                foodIv.setImageURI(uri);
                checkImage = 1;
            } else if (requestCode == 1) {

                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                foodIv.setImageBitmap(bitmap);
                checkImage = 1;

            }
        }


    }

    public void rbClick(View view) {
        if (noR.isChecked()) {
            yesR.setChecked(false);
            frizzing = "No";

        } else if (yesR.isChecked()) {
            noR.setChecked(false);
            frizzing = "Yes";

        }
    }

    public void update(View view) {
        frizzing = "No";
        String name = nameEt.getText().toString();
        String phone = phoneEt.getText().toString();
        String house = houseEt.getText().toString();
        String area = areaEt.getText().toString();
        String description = descriptionEt.getText().toString();
        String post = PostEt.getText().toString();
        String date = dateEt.getText().toString();
        String division = divisionSP.getSelectedItem().toString();
        String distirct = districtSP.getSelectedItem().toString();
        String upazila = upazilaSP.getSelectedItem().toString();

        if (phone.length() != 11) {
            Toast.makeText(this, "Number is not correct", Toast.LENGTH_SHORT).show();

        } else if (division.equals("Division") || distirct.equals("District") || upazila.equals("Upazila")) {
            Toast.makeText(this, "Address is not fill up", Toast.LENGTH_SHORT).show();

        } else if (name.equals("") || house.equals("") || area.equals("") || description.equals("") || post.equals("") || date.equals("")) {
            Toast.makeText(this, "Something is Empty", Toast.LENGTH_SHORT).show();
        } else {
            progress = new ProgressDialog(this);
            progress.setMessage("Please Wait");
            progress.setTitle("Add Food");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            progress.setCancelable(true);


            addFood(name, phone, house, area, post, date, division, distirct, upazila, description);
        }

    }

    private void addFood(final String name, final String phone, final String house, final String area, final String post, final String date, final String division, final String distirct, final String upazila, final String description) {
        final String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
        final String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("Food").child(foodId);
        if (check == 1) {
            foodIv.setDrawingCacheEnabled(true);
            foodIv.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) foodIv.getDrawable()).getBitmap();

            //   Uri fileUri = Uri.fromFile(new File(currentPhotoPath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference imgRef =
                    storageReference.child("foodImage" + UUID.randomUUID());

            UploadTask uploadTask = imgRef.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            final String downloadLink1 = String.valueOf(uri);


                            HashMap<String, Object> foodInfo = new HashMap<>();
                            foodInfo.put("userId", userId);
                            foodInfo.put("name", name);
                            foodInfo.put("phone", phone);
                            foodInfo.put("area", area);
                            foodInfo.put("post", post);
                            foodInfo.put("date", currentDate);
                            foodInfo.put("time", currentTime);
                            foodInfo.put("house", house);
                            foodInfo.put("division", division);
                            foodInfo.put("distirct", distirct);
                            foodInfo.put("upazila", upazila);
                            foodInfo.put("frizzing", frizzing);
                            foodInfo.put("description", description);
                            foodInfo.put("firstName", firstName);
                            foodInfo.put("profilePicture", profilePicture);
                            foodInfo.put("available", available);


                            foodInfo.put("FoodPicture", downloadLink1);
                            foodInfo.put("foodId", foodId);

                            dataRef.updateChildren(foodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        progress.setMessage("Successfully Add Food");
                                        Intent intent = new Intent(MyFoodActivity.this, MainActivity.class);

                                        intent.putExtra("check", 1);

                                        startActivity(intent);
                                        progress.dismiss();

                                        Toast.makeText(MyFoodActivity.this, "Successfully Add Food.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(MyFoodActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//
                        }
                    });


                }
            });

//
        } else if (check == 2) {


            final StorageReference imageRef = storageReference.child("image" + uri.getLastPathSegment());

            imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//
                            final String downloadLink1 = String.valueOf(uri);


                            DatabaseReference foodData = databaseReference.child("users").child(userId).child("Food").child(foodId);

                            HashMap<String, Object> foodInfo = new HashMap<>();
                            foodInfo.put("userId", userId);
                            foodInfo.put("name", name);
                            foodInfo.put("phone", phone);
                            foodInfo.put("area", area);
                            foodInfo.put("post", post);
                            foodInfo.put("date", currentDate);
                            foodInfo.put("time", currentTime);
                            foodInfo.put("division", division);
                            foodInfo.put("distirct", distirct);
                            foodInfo.put("upazila", upazila);
                            foodInfo.put("frizzing", frizzing);
                            foodInfo.put("description", description);
                            foodInfo.put("firstName", firstName);
                            foodInfo.put("profilePicture", profilePicture);
                            foodInfo.put("available", available);
                            foodInfo.put("FoodPicture", downloadLink1);
                            foodInfo.put("foodId", foodId);

                            dataRef.updateChildren(foodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        progress.setMessage("Successfully Add Trip");
                                        Intent intent = new Intent(MyFoodActivity.this, MainActivity.class);

                                        intent.putExtra("check", 1);

                                        startActivity(intent);
                                        progress.dismiss();
                                        startActivity(intent);


                                    } else {
                                        progress.dismiss();
                                        Toast.makeText(MyFoodActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });


                }
            });
        } else {


            HashMap<String, Object> foodInfo = new HashMap<>();
            foodInfo.put("userId", userId);
            foodInfo.put("name", name);
            foodInfo.put("phone", phone);
            foodInfo.put("area", area);
            foodInfo.put("post", post);
            foodInfo.put("date", date);
            foodInfo.put("house", house);
            foodInfo.put("division", division);
            foodInfo.put("distirct", distirct);
            foodInfo.put("upazila", upazila);
            foodInfo.put("frizzing", frizzing);
            foodInfo.put("description", description);
            foodInfo.put("firstName", firstName);
            foodInfo.put("profilePicture", profilePicture);
            foodInfo.put("available", available);


            foodInfo.put("FoodPicture", imageUrl);
            foodInfo.put("foodId", foodId);
            dataRef.updateChildren(foodInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progress.setMessage("Successfully Add Trip");
                    Toast.makeText(MyFoodActivity.this, "Added.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MyFoodActivity.this, MainActivity.class);

                    intent.putExtra("check", 1);

                    startActivity(intent);
                    progress.dismiss();
                    startActivity(intent);
                }
            });
        }
    }
}