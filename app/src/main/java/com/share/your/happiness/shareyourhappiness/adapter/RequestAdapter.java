package com.share.your.happiness.shareyourhappiness.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.share.your.happiness.shareyourhappiness.Activity.RequestActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.Modle_Class.User;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    List<Request> requestList;
    List<Request> requestList1;

    int check;
    int itemChecked = 0;
    String requestType, userId;
    private EditText commentET, numberEt;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    public RequestAdapter(Context context, List<Request> requestList, int check, String requestType) {
        this.context = context;
        this.requestList = requestList;
        this.check = check;
        this.requestType = requestType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_recycleview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Request request = requestList.get(position);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userId = currentUser.getUid();

        Picasso.get().load(request.getRequestFoodIv()).into(holder.foodIv);
        holder.dateTv.setText("Date:" + request.getDate());
        holder.timeTv.setText("Time:" + request.getTime());
        holder.statusTv.setText("Status:" + request.getStatus());

        holder.foodNameTv.setText(request.getFoodName());

        if (requestType.equals("requestOpen") && !request.getStatus().equals("Reject")) {


            holder.deleteIv.setVisibility(View.GONE);
            Picasso.get().load(request.getFromRequestUserIv()).into(holder.profileIv);
            holder.profileNameTv.setText(request.getRequestName());


            holder.acceptIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

                    dataRef.child("users").child(userId).child("Request").child(request.getRequestId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                String status=snapshot.child("status").getValue().toString();


                                if (!status.equals("Received")){
                                    itemCheck(userId, request.getRequestId(), "Accept","None");
                                    requestList.get(position).setStatus("Accept");

                                }
                                else {
                                    Toast.makeText(context, "Item is already delivery", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, RequestActivity.class);
                                    intent.putExtra("Request", "requestOpen");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }

                            }
                            else {
                                Toast.makeText(context, "Item is already delivery", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("Request", "requestOpen");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }







                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }
            });
            holder.rejectIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

                    dataRef.child("users").child(userId).child("Request").child(request.getRequestId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                String status=snapshot.child("status").getValue().toString();


                                if (!status.equals("Received")){

                                    itemCheck(userId, request.getRequestId(), "Reject","None");


                                }
                                else {
                                    Toast.makeText(context, "Item is already delivery", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, RequestActivity.class);
                                    intent.putExtra("Request", "requestOpen");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }

                            }
                            else {
                                Toast.makeText(context, "Item is already delivery", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("Request", "requestOpen");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }







                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






                }
            });


            holder.requestItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rView = i.inflate(R.layout.request_note, null);


                    commentET = rView.findViewById(R.id.commentEt);
                    numberEt = rView.findViewById(R.id.numberEt);

                    commentET.setText(request.getComment());
                    numberEt.setText(request.getFromUserNumber());

                    builder.setView(rView);

                    final Dialog dialog = builder.create();
                    dialog.show();
                }
            });


        }

        else if (requestType.equals("otherRequest")) {

            Picasso.get().load(request.getToRequestUserIv()).into(holder.profileIv);
            holder.profileNameTv.setText(request.getToRequestUserName());


            holder.acceptIv.setVisibility(View.GONE);
            holder.rejectIv.setVisibility(View.GONE);

            if (request.getStatus().equals("Accept")) {
                holder.receiveIv.setVisibility(View.VISIBLE);
                holder.receiveIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        getData( request.getToRequestUserId(), "totalDelivery");
                        getData( request.getFromRequestUserId(), "totalReceive");




                        itemCheck(request.getToRequestUserId(), request.getRequestId(), "Received",request.getRequestFoodId());
                    }
                });
            }

            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemChecked = 0;

                    itemCheck(request.getToRequestUserId(), request.getRequestId(), "Delete","None");


                }
            });


        }
        else if (requestType.equals("Rejected")) {

            Picasso.get().load(request.getToRequestUserIv()).into(holder.profileIv);
            holder.profileNameTv.setText(request.getRequestName());


            //   holder.acceptIv.setVisibility(View.VISIBLE);
            holder.rejectIv.setVisibility(View.GONE);

            holder.acceptIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemCheck(userId, request.getRequestId(), "RejectToAccept","None");
                    requestList.get(position).setStatus("Accept");


                }
            });

            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemChecked = 0;

                    itemCheck(request.getToRequestUserId(), request.getRequestId(), "RejectToDelete","None");


                }
            });


        }

        else if (requestType.equals("Received") && request.getStatus().equals("Received")) {
            holder.acceptIv.setVisibility(View.GONE);
            holder.receiveIv.setVisibility(View.GONE);
            holder.deleteIv.setVisibility(View.GONE);
            holder.rejectIv.setVisibility(View.GONE);
            Picasso.get().load(request.getToRequestUserIv()).into(holder.profileIv);
            holder.delivery.setVisibility(View.VISIBLE);
            holder.profileNameTv.setText(request.getToRequestUserName());
            if(userId.equals(request.getToRequestUserId())){
                holder.deleteIv.setVisibility(View.VISIBLE);
                holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.delivery.setText("Successfully Delivery");
                        itemCheck(userId, request.getRequestId(), "DealReceived","None");
                    }
                });

            }

            else if(userId.equals(request.getFromRequestUserId())){
                holder.delivery.setText("Successfully Receive");
                holder.deleteIv.setVisibility(View.VISIBLE);
                holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        itemCheck(request.getToRequestUserId(), request.getRequestId(), "DealReceived","None");
                    }
                });

            }


        }


    }


    @Override
    public int getItemCount() {

        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout requestItem;
        ImageView foodIv, profileIv;
        Button acceptIv, deleteIv, rejectIv, receiveIv;
        TextView foodNameTv, profileNameTv, timeTv, dateTv, statusTv,delivery;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodIv = itemView.findViewById(R.id.foodIv);
            profileIv = itemView.findViewById(R.id.profileIvR);
            foodNameTv = itemView.findViewById(R.id.foodNameTvR);
            profileNameTv = itemView.findViewById(R.id.profileNameTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            acceptIv = itemView.findViewById(R.id.acceptIv);
            rejectIv = itemView.findViewById(R.id.rejectIv);
            statusTv = itemView.findViewById(R.id.statusTv);
            deleteIv = itemView.findViewById(R.id.deleteIv);
            requestItem = itemView.findViewById(R.id.requestItem);
            receiveIv = itemView.findViewById(R.id.receiveIv);
            delivery = itemView.findViewById(R.id.delivery);


        }
    }

    public void itemCheck(final String userIds, final String requestId, final String type,final String foodId) {


        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();


        dataRef.child("users").child(userIds).child("Request").child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (type.equals("Accept")) {

                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);

                        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                dataSnapshot.getRef().child("time").setValue(currentTime);
                                dataSnapshot.getRef().child("date").setValue(currentDate);
                                dataSnapshot.getRef().child("status").setValue("Accept");


                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("Request", "requestOpen");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }
                        });
                    } else if (type.equals("Delete")) {
                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);
                        dataRef.removeValue();
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "otherRequest");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("Reject")) {


                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);

                        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().child("status").setValue("Rejected");

                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("Request", "requestOpen");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }
                        });


                    } else if (type.equals("RejectToAccept")) {

                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);

                        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().child("status").setValue("Accept");


                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("Request", "Rejected");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }
                        });


                    } else if (type.equals("RejectToDelete")) {

                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);
                        dataRef.removeValue();
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "Rejected");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);

                    }

                    //Received
                    else if (type.equals("Received")) {

                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);

                        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().child("status").setValue("Received");


                                //

                                final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Food").child(foodId);

                                dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        dataSnapshot.getRef().child("available").setValue("No");


                                        Intent intent = new Intent(context, RequestActivity.class);
                                        intent.putExtra("Request", "requestOpen");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.d("User", databaseError.getMessage());
                                    }
                                });

//
//
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }
                        });


                    }

                    else if (type.equals("DealReceived")){
                        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userIds).child("Request").child(requestId);
                        dataRef.removeValue();
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "Received");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                } else {
                    Toast.makeText(context, "Not exist Request", Toast.LENGTH_SHORT).show();

                    if (type.equals("Accept")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "requestOpen");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("Reject")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "requestOpen");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("Delete")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "otherRequest");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    else if (type.equals("RejectToAccept")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "Rejected");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("RejectToDelete")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "Rejected");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("Received")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "otherRequest");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    else if (type.equals("DealReceived")) {
                        Intent intent = new Intent(context, RequestActivity.class);
                        intent.putExtra("Request", "Received");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getData(final String  Id, final String updateType){


        DatabaseReference dataRef1 = FirebaseDatabase.getInstance().getReference();

        dataRef1.child("users").child(Id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    String delivery=snapshot.child("totalDelivery").getValue().toString();

                    int totalDelivery=Integer.parseInt(delivery)+1;
                 String count=String.valueOf(totalDelivery);
                    update(Id, count,updateType);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    public void update(String userId, final String count,final String updateType){
        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users").child(userId);


        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (updateType.equals("totalDelivery")){
                    dataSnapshot.getRef().child("totalDelivery").setValue(count);

                }
                else if (updateType.equals("totalReceive")){
                    dataSnapshot.getRef().child("totalReceive").setValue(count);
                    Intent intent = new Intent(context, RequestActivity.class);
                    intent.putExtra("Request", "otherRequest");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });

    }


}
