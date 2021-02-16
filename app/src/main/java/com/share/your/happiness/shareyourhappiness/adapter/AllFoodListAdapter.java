package com.share.your.happiness.shareyourhappiness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.share.your.happiness.shareyourhappiness.Activity.FoodInfoActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.ListFood;
import com.share.your.happiness.shareyourhappiness.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllFoodListAdapter extends RecyclerView.Adapter<AllFoodListAdapter.ViewHolder>{
        List<ListFood> listFoods;
    Context context;


    public AllFoodListAdapter(List<ListFood> listFoods, Context context) {
        this.listFoods = listFoods;
       // Toast.makeText(context, String.valueOf(listFoods.size()), Toast.LENGTH_SHORT).show();
        this.context = context;
    }

    @NonNull
    @Override
    public AllFoodListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.all_food_recyleview,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFoodListAdapter.ViewHolder holder, int position) {


        final ListFood listFood=listFoods.get(position);
       // Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
        if(listFoods.size()==0){
            Toast.makeText(context, "yes:", Toast.LENGTH_SHORT).show();

        }


        Picasso.get().load(listFood.getFoodPicture()).into(holder.foodIv);
        Picasso.get().load(listFood.getProfilePicture()).into(holder.profileIv);
        holder.foodNameTv.setText(listFood.getName());
        holder.profileNameTv.setText(listFood.getFirstName());
        holder.upazilaTv.setText("Upazila:"+listFood.getUpazila());
        holder.dateTv.setText("Date:"+listFood.getDate());
         holder.updateTimeTv.setText("Time:"+listFood.getTime());


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FoodInfoActivity.class);
                intent.putExtra("foodName", listFood.getName());
                intent.putExtra("date", listFood.getDate());
                intent.putExtra("house", listFood.getHouse());
                intent.putExtra("post", listFood.getPost());
                intent.putExtra("upazila", listFood.getUpazila());
                intent.putExtra("district", listFood.getDistirct());
                intent.putExtra("division", listFood.getDivision());
                intent.putExtra("frizzing", listFood.getFrizzing());
                intent.putExtra("description", listFood.getDescription());
                intent.putExtra("foodImage", listFood.getFoodPicture());
                intent.putExtra("phone", listFood.getPhone());
                intent.putExtra("area", listFood.getArea());
                intent.putExtra("profilePicture", listFood.getProfilePicture());

                intent.putExtra("firstName", listFood.getFirstName());
                intent.putExtra("userId", listFood.getUserId());
                intent.putExtra("requestFoodId", listFood.getFoodId());
                intent.putExtra("firstName", listFood.getFirstName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listFoods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodIv,profileIv;
            TextView foodNameTv,profileNameTv,upazilaTv,dateTv,updateTimeTv,massage;

        LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodIv=itemView.findViewById(R.id.foodIv);
            profileIv=itemView.findViewById(R.id.profileIv);
            foodNameTv=itemView.findViewById(R.id.foodNameTv);
            profileNameTv=itemView.findViewById(R.id.profileNameTv);
            upazilaTv=itemView.findViewById(R.id.upazilaTv);
            dateTv=itemView.findViewById(R.id.dateTv);
            item=itemView.findViewById(R.id.item);
//            massage=itemView.findViewById(R.id.massage);
//            massage.setVisibility(View.VISIBLE);


            updateTimeTv=itemView.findViewById(R.id.updateTimeTv);

        }
    }


}
