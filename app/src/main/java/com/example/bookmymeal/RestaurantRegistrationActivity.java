package com.example.bookmymeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class RestaurantRegistrationActivity extends AppCompatActivity {

    EditText OwnerName,OwnerPhone,OwnerEmail,RestaurantName,RestaurantPhone,RestaurantEmail,RestaurantAddress,Password,ConfirmPassword;
    ProgressDialog progressDialog;
    MaterialButton sendRegistrationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        OwnerName=(EditText)findViewById(R.id.ownerName);
        OwnerPhone=(EditText)findViewById(R.id.ownerPhone);
        OwnerEmail=(EditText)findViewById(R.id.ownerEmail);
        RestaurantName=(EditText)findViewById(R.id.restaurantName);
        RestaurantPhone=(EditText)findViewById(R.id.restaurantPhone);
        RestaurantEmail=(EditText)findViewById(R.id.restaurantEmail);
        RestaurantAddress=(EditText)findViewById(R.id.restaurantAddress);
        Password=(EditText)findViewById(R.id.password);
        ConfirmPassword=(EditText)findViewById(R.id.confirmPassword);

        sendRegistrationRequest=(MaterialButton)findViewById(R.id.registerBtn);

        sendRegistrationRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRestaurantInfo();
            }
        });
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 6;
    }


    private void showDialog(){
        this.progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
    }

    private void getRestaurantInfo(){


        String OName=OwnerName.getText().toString();
        String OPhone=OwnerPhone.getText().toString();
        String OMail=OwnerEmail.getText().toString();
        String RName=RestaurantName.getText().toString();
        String RPhone=RestaurantPhone.getText().toString();
        String RMail=RestaurantEmail.getText().toString();
        String RAddress=RestaurantAddress.getText().toString();
        String Pass=Password.getText().toString();
        String CPass=ConfirmPassword.getText().toString();

        if(!isPasswordValid(Password.getText())){
            Password.setError("Password must contain at least 6 characters.");
        }
        else{
            if(!OName.isEmpty() && !OPhone.isEmpty() && !OMail.isEmpty() && !RName.isEmpty() && !RPhone.isEmpty() && !RMail.isEmpty() && !RAddress.isEmpty() && !Pass.isEmpty() && !CPass.isEmpty()){
                if(Pass.equals(CPass)){
                    restaurantRegistrationRequest(OName,OMail,OPhone,RName,RMail,RPhone,RAddress,Pass);
                }
                else{
                    Password.setError("Password do not matched");
                }
            }
            else{
                Toast.makeText(RestaurantRegistrationActivity.this,"Field can't be empty",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void restaurantRegistrationRequest(String OName,String OMail,String OPhone,String RName,String RMail,String RPhone,String RAddress,String Pass){
        showDialog();
        JSONObject restaurantRequest=new JSONObject();

        try{
            restaurantRequest.put("ownerName",OName);
            restaurantRequest.put("ownerEmail",OMail);
            restaurantRequest.put("ownerPhoneNumber",OPhone);
            restaurantRequest.put("restaurantName",RName);
            restaurantRequest.put("restaurantEmail",RMail);
            restaurantRequest.put("restaurantPhone",RPhone);
            restaurantRequest.put("resturantAddress",RAddress);
            restaurantRequest.put("password",Pass);


        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyUrl.RestaurantRegistration, restaurantRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Boolean isSuccess=jsonObject.getBoolean("succeeded");
                    JSONArray jsonArray= new JSONArray(jsonObject.getString("errors"));

                    String []errorList=new String[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject errorObject= jsonArray.getJSONObject(i);
                        String code=errorObject.getString("code");
                        errorList[i]=code;
                    }


                    if(isSuccess){
                        new MaterialAlertDialogBuilder(RestaurantRegistrationActivity.this)
                                .setTitle("Registration Successful")
                                .setMessage("Please Confirm Your Email.You are not able to login without email verification")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.dismiss();
                                    }
                                })
                                .show();
                    }
                    else{
                        new MaterialAlertDialogBuilder(RestaurantRegistrationActivity.this)
                                .setTitle("Registration  Unsuccessful")
                                .setItems(errorList, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.dismiss();
                                    }
                                })
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(RestaurantRegistrationActivity.this)
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantRegistrationActivity.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);

    }



}
