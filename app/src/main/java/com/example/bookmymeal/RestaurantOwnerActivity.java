package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RestaurantOwnerActivity extends AppCompatActivity {

    LinearLayout cardProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_owner);

        cardProfile=(LinearLayout)findViewById(R.id.cardProfile);

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantOwnerActivity.this,RestaurantProfileActivity.class));
            }
        });
    }
}
