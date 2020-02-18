package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class LogInActivity extends AppCompatActivity {

    TextView textViewSignUp,textViewFogotPassword;
    MaterialButton btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        textViewSignUp=(TextView)findViewById(R.id.textViewSignup);
        textViewFogotPassword=(TextView)findViewById(R.id.textViewforgotPassword);
        btnlogin=(MaterialButton)findViewById(R.id.buttonLogin);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,RegisterActivity.class));
            }
        });

        textViewFogotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,ForgotPasswordActivity.class));
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogInActivity.this,RestaurantOwnerActivity.class));
            }
        });
    }
}
