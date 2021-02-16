package com.share.your.happiness.shareyourhappiness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.share.your.happiness.shareyourhappiness.Activity.FoodInfoActivity;
import com.share.your.happiness.shareyourhappiness.Activity.MyFoodActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.Modle_Class.MyFood;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFoodAdapter extends RecyclerView.Adapter<MyFoodAdapter.ViewHolder>{


    List<MyFood> myFoodList;
  Context context;

    public MyFoodAdapter(List<MyFood> myFoodList, Context context) {
        this.myFoodList = myFoodList;
        this.context = context;


    }

    @NonNull
    @Override
    public MyFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myfood_recycleview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFoodAdapter.ViewHolder holder, int position) {
       final MyFood myFood=myFoodList.get(position);
        Picasso.get().load(myFood.getFoodPicture()).into(holder.foodIv);
        holder.foodNameTv.setText(myFood.getName());
        holder.timeTv.setText("Time:"+myFood.getTime());
        holder.profileNameTv.setText(myFood.getFirstName());
        holder.availableTv.setText("Available:"+myFood.getAvailable());
        holder.dateTv.setText("Date:"+myFood.getDate());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MyFoodActivity.class);
                intent.putExtra("foodName", myFood.getName());
                intent.putExtra("date", myFood.getDate());
                intent.putExtra("house", myFood.getHouse());
                intent.putExtra("post", myFood.getPost());
                intent.putExtra("upazila", myFood.getUpazila());
                intent.putExtra("district", myFood.getDistirct());
                intent.putExtra("division", myFood.getDivision());
                intent.putExtra("frizzing", myFood.getFrizzing());
                intent.putExtra("description", myFood.getDescription());
                intent.putExtra("foodImage", myFood.getFoodPicture());
                intent.putExtra("phone", myFood.getPhone());
                intent.putExtra("area", myFood.getArea());
                intent.putExtra("foodId", myFood.getFoodId());
                intent.putExtra("firstName", myFood.getFirstName());
                intent.putExtra("profilePicture", myFood.getProfilePicture());

                context.startActivity(intent);
            }
        });
        holder.requestIv.setVisibility(View.GONE);
        holder.requestIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        ImageView  foodIv,requestIv;
        TextView foodNameTv,profileNameTv,timeTv,dateTv,availableTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodIv=itemView.findViewById(R.id.foodIv);
            foodNameTv=itemView.findViewById(R.id.foodNameTv);
            profileNameTv=itemView.findViewById(R.id.profileNameTv);
            availableTv=itemView.findViewById(R.id.availableTv);
            dateTv=itemView.findViewById(R.id.dateTv);
            item=itemView.findViewById(R.id.item);
            requestIv=itemView.findViewById(R.id.requestIv);
            timeTv=itemView.findViewById(R.id.timeTv);

        }
    }
}
