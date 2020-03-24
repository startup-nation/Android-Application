package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout linearRestaurantList,linearLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        linearRestaurantList=(LinearLayout)findViewById(R.id.cardRestaurantList);
        linearLogOut=(LinearLayout)findViewById(R.id.cardLogout);
        final SharedPreferences sharedPreferences = getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        linearLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                editor.apply();
                finish();
                startActivity(new Intent(AdminDashboardActivity.this,LogInActivity.class));
            }
        });

        linearRestaurantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboardActivity.this,RestaurantRequestListActivity.class));
            }
        });
    }
}
