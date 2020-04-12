package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    EditText editTextEmail,editTextName,editTextPhone;
    ProgressDialog progressDialog;
    ImageButton nameEditBtn,phoneEditBtn;
    MaterialButton doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        SharedPreferences sharedPreferences =getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        doneBtn=(MaterialButton)findViewById(R.id.buttonDone);
        editTextEmail=(EditText)findViewById(R.id.profileEmailEditText);
        editTextName=(EditText)findViewById(R.id.profileNameEditText);
        editTextPhone=(EditText)findViewById(R.id.profilePhoneEditText);
        nameEditBtn=(ImageButton)findViewById(R.id.nameEditBtn);
        phoneEditBtn=(ImageButton)findViewById(R.id.phoneEditBtn);

        nameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextName.setEnabled(true);
            }
        });

        phoneEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextPhone.setEnabled(true);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=editTextName.getText().toString();
                String phone=editTextPhone.getText().toString();
                updateProfile(name,phone);
            }
        });

        showData();

    }

    private void updateProfile(final String name, final String phone){
        showDialog();
        JSONObject profile=new JSONObject();
        try{
            profile.put("name",name);
            profile.put("phoneNumber",phone);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.POST, MyUrl.ProfileUpdate, profile, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObject= new JSONObject(response.toString());
                    System.out.println(jsonObject);
                    SharedPreferences sharedPreferences =getSharedPreferences("DataFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.remove("name");
                    editor.remove("phone");
                    editor.apply();
                    editor.putString("name",name);
                    editor.putString("phone",phone);
                    new MaterialAlertDialogBuilder(UserProfileActivity.this)
                            .setTitle("Message")
                            .setMessage("Go To Dashboard")
                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(UserProfileActivity.this,CustomerDashboardActivity.class));
                                    finish();
                                }
                            })
                            .show();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(UserProfileActivity.this)
                        .setTitle("Error")
                        .setMessage(error.toString())
                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.dismiss();
                            }
                        })
                        .show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private  void showDialog(){
        this.progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Wait for a moment...");
        progressDialog.show();
    }

    private void showData(){
        SharedPreferences sharedPreferences=getSharedPreferences("DataFile",Context.MODE_PRIVATE);
        if(sharedPreferences.contains("email")){
            editTextEmail.setText(sharedPreferences.getString("email","NotFound"));
            editTextName.setText(sharedPreferences.getString("name","NotFound"));
            editTextPhone.setText(sharedPreferences.getString("phone","NotFound"));
        }

    }


}
