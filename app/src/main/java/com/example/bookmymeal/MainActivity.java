package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bookmymeal.Backend.Decode;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.textview);

        SharedPreferences sharedPreferences =getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains("Token")){
            String token=sharedPreferences.getString("Token","NotFound");
            System.out.println(token);
            JSONObject tokenDecodeObj= null;
            try {
                tokenDecodeObj = new JSONObject(Decode.decoded(token));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                assert tokenDecodeObj != null;
                role=tokenDecodeObj.getString("role");
                editor.putString("Role",role);
                editor.commit();
                editor.apply();
                if(role.equals("Customer")){
                    startActivity(new Intent(MainActivity.this,CustomerDashboardActivity.class));
                }
                else if(role.equals("Admin")){
                    startActivity(new Intent(MainActivity.this,AdminDashboardActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            startActivity(new Intent(MainActivity.this,LogInActivity.class));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LogInActivity.class));
            }
        });
    }
}
