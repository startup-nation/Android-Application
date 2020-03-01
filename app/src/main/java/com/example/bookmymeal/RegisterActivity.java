package com.example.bookmymeal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextemail,editTextpassword,editTextconfirmPassword;
    MaterialButton signUp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextemail=(TextInputEditText)findViewById(R.id.textInputEditTextregEmail);
        editTextpassword=(TextInputEditText)findViewById(R.id.textInputEditTextregPassword);
        editTextconfirmPassword=(TextInputEditText)findViewById(R.id.textInputEditTextregConfirmPassword);
        signUp=(MaterialButton)findViewById(R.id.buttonSignUp);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationCredentials();
            }
        });


    }



    //methods are here

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 6;
    }


    private void showDialog(){
        this.progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
    }


    private void registrationCredentials(){
        String email=editTextemail.getText().toString();
        String pass=editTextpassword.getText().toString();
        String passConfirm=editTextconfirmPassword.getText().toString();

        if(!isPasswordValid(editTextpassword.getText())){
            editTextpassword.setError("Password must contain at least 6 characters.");
        }
        else{

            if(!email.isEmpty()){

                if(pass.equals(passConfirm)){
                    registration(email,pass);
                }
                else{
                    editTextpassword.setError("Passwords not matched");
                }//comparing passwords

            }
            else{
                editTextemail.setError("Field can't be empty.");
            }//email empty check

        }
//method close
    }


    private void registration(String email,String password){
        showDialog();

        JSONObject registration = new JSONObject();

        try {

            registration.put("email",email);
            registration.put("password",password);

        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, MyUrl.UserRegistrationUrl, registration, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
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
                        new MaterialAlertDialogBuilder(RegisterActivity.this)
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
                        new MaterialAlertDialogBuilder(RegisterActivity.this)
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


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                new MaterialAlertDialogBuilder(RegisterActivity.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(jsonObjectRequest);
        //method close
    }


}
