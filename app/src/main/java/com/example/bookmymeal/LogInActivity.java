package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.bookmymeal.Backend.Decode;
import com.example.bookmymeal.Backend.MyUrl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    TextView textViewSignUp,textViewFogotPassword;
    TextInputEditText editTextEmail,editTextPassword;
    MaterialButton btnlogin;
    ProgressDialog progressDialog;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editTextEmail=(TextInputEditText)findViewById(R.id.textInputEditTextLoginEmail);
        editTextPassword=(TextInputEditText)findViewById(R.id.textInputEditTextloginPassword);
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
                getCredentials();
            }
        });
    }

    private  void showDialog(){
        this.progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
    }

    private void getCredentials(){
        String email=editTextEmail.getText().toString();
        String pass=editTextPassword.getText().toString();

        if(!email.isEmpty() && !pass.isEmpty()){
            Login(email,pass);
        }
        else{
            new MaterialAlertDialogBuilder(LogInActivity.this)
                    .setTitle("Wanring")
                    .setMessage("Field can't be empty")
                    .setIcon(R.drawable.ic_warning)
                    .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void Login(String email,String password){
        showDialog();

        JSONObject login = new JSONObject();

        try{
            login.put("email",email);
            login.put("password",password);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyUrl.UserLogin, login, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String token = jsonObject.getString("token");
                    SharedPreferences sharedPreferences = getSharedPreferences("DataFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("Token",token);
                    editor.commit();

                    JSONObject tokenDecodeObj=new JSONObject(Decode.decoded(token));
                    role=tokenDecodeObj.getString("role");
                    editor.putString("Role",role);
                    editor.commit();
                    //String decode= Decode.decoded(token);
                    System.out.println(role);
                    if(role.equals("Customer")){
                        Intent intent = new Intent(LogInActivity.this,CustomerDashboardActivity.class);
                        intent.putExtra("token",token);
                        startActivity(intent);
                        progressDialog.dismiss();
                    }
                    else if(role.equals("Admin")){
                        startActivity(new Intent(LogInActivity.this,AdminDashboardActivity.class));
                    }

/*


*/

                }catch (JSONException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(LogInActivity.this)
                        .setTitle("Wanring")
                        .setMessage("Email or Password incorrect")
                        .setIcon(R.drawable.ic_warning)
                        .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        RequestQueue requestQueue =  Volley.newRequestQueue(LogInActivity.this);
        requestQueue.add(jsonObjectRequest);
        //method close
    }



}
