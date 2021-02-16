package com.share.your.happiness.shareyourhappiness.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.share.your.happiness.shareyourhappiness.Activity.FoodInfoActivity;
import com.share.your.happiness.shareyourhappiness.Activity.RequestActivity;
import com.share.your.happiness.shareyourhappiness.Modle_Class.Request;
import com.share.your.happiness.shareyourhappiness.R;


public class MoreFragment extends Fragment {

private CardView requestOpen,requestSend,rejectList,deliveryBtn;
    public MoreFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_more, container, false);

        requestOpen=view.findViewById(R.id.requestOpen);
        requestOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), RequestActivity.class);
                intent.putExtra("Request", "requestOpen");
                startActivity(intent);
            }
        });

        requestSend=view.findViewById(R.id.requestSend);
        requestSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), RequestActivity.class);
                intent.putExtra("Request", "otherRequest");
                startActivity(intent);
            }
        });

        rejectList=view.findViewById(R.id.rejectList);
        deliveryBtn=view.findViewById(R.id.deliveryBtn);

        rejectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), RequestActivity.class);
                intent.putExtra("Request", "Rejected");
                startActivity(intent);
            }
        });

        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getContext(), RequestActivity.class);
                intent.putExtra("Request", "Received");
                startActivity(intent);
            }
        });


        return view;
    }


}