package com.share.your.happiness.shareyourhappiness.Developer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.share.your.happiness.shareyourhappiness.Activity.AddFoodActivity;
import com.share.your.happiness.shareyourhappiness.Activity.MainActivity;
import com.share.your.happiness.shareyourhappiness.R;

public class DevelopersActivity extends AppCompatActivity {
    private int checkFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        setTitle("Developers");
        checkFragment= getIntent().getIntExtra("checkFragment", 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("checkFragment",checkFragment);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}