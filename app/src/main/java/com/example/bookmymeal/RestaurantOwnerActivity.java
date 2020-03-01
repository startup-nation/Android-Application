package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RestaurantOwnerActivity extends AppCompatActivity {

    LinearLayout cardProfile,cardFoods,cardOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_owner);

        cardProfile=(LinearLayout)findViewById(R.id.cardProfile);
        cardFoods=(LinearLayout)findViewById(R.id.cardFoods);
        cardOffers=(LinearLayout)findViewById(R.id.cardOffers);

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantOwnerActivity.this,RestaurantProfileActivity.class));
            }
        });

        cardFoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantOwnerActivity.this,RestaurantFoodsActivity.class));
            }
        });

        cardOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantOwnerActivity.this,RestaurantOffersActivity.class));
            }
        });
    }
}
