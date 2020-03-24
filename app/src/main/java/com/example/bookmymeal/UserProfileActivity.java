package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    EditText editTextEmail,editTextName,editTextPhone;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);




        editTextEmail=(EditText)findViewById(R.id.profileEmailEditText);
        editTextName=(EditText)findViewById(R.id.profileNameEditText);
        editTextPhone=(EditText)findViewById(R.id.profilePhoneEditText);
        showDialog();
        showData();
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
        progressDialog.dismiss();

    }


}
