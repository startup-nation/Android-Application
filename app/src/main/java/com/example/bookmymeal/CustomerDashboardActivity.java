package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.Decode;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomerDashboardActivity extends AppCompatActivity {
    LinearLayout linearCardProfile,linearCardLogout;
    //String tok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        final String token = getIntent().getStringExtra("token");

        linearCardProfile=(LinearLayout)findViewById(R.id.cardProfile);
        linearCardLogout=(LinearLayout)findViewById(R.id.cardLogout);
        final SharedPreferences sharedPreferences = getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        new getProfileValue().execute();
        //tok=sharedPreferences.getString("Token",null);
        linearCardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                new MaterialAlertDialogBuilder(CustomerDashboardActivity.this)
                        .setTitle("Token")
                        .setMessage(token)
                        .setIcon(R.drawable.ic_warning)
                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();


 */
         startActivity(new Intent(CustomerDashboardActivity.this,UserProfileActivity.class));

            }
        });

        linearCardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               editor.clear();
               editor.commit();
               editor.apply();
                    finish();
                    startActivity(new Intent(CustomerDashboardActivity.this,LogInActivity.class));


            }
        });

    }

    class getProfileValue extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            getVolley();
            return null;
        }

        private void getVolley(){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, MyUrl.UserProfile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println(response);
                    parseData(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    String tok=getSharedPreferences("DataFile",Context.MODE_PRIVATE).getString("Token",null);
                    System.out.println(tok);
                    headers.put("Content-Type","application/json");
                    headers.put("Authorization", "Bearer " +tok );

                    return headers;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(CustomerDashboardActivity.this);
            requestQueue.add(stringRequest);
        }

        public void parseData(String response){
            try{
                JSONObject jsonObject=new JSONObject(response);
                String email=jsonObject.getString("email");
                String name=jsonObject.getString("name");
                String phone=jsonObject.getString("phoneNumber");
                SharedPreferences sharedPreferences = getSharedPreferences("DataFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("name",name);
                editor.putString("phone",phone);
                editor.putString("email",email);
                editor.commit();
                editor.apply();
                System.out.println(email+name+phone);


            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }//method close


//activity ends
    }


