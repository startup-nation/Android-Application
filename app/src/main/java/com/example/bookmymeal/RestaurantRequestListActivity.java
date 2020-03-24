package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.DataHold;
import com.example.bookmymeal.Backend.MyUrl;
import com.example.bookmymeal.Backend.RestaurantRequestAdapter;
import com.example.bookmymeal.Backend.RestaurantRequestInfo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantRequestListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ArrayList<RestaurantRequestInfo>restaurantRequestInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_request_list);

        recyclerView=(RecyclerView)findViewById(R.id.restaurantRequestList);

        showDialog();
       new getRequestList().execute();
    }

    private void showDialog(){
        this.progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Getting Data");
        progressDialog.show();
    }

    class getRequestList extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            getVolley();
            return null;
        }

        private void getVolley(){
            StringRequest stringRequest=new StringRequest(Request.Method.GET, MyUrl.RestaurantRequestList, new Response.Listener<String>() {
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
            RequestQueue requestQueue = Volley.newRequestQueue(RestaurantRequestListActivity.this);
            requestQueue.add(stringRequest);
        }

        private void parseData(String response){
            try{
                JSONArray reqArray=new JSONArray(response);
                restaurantRequestInfos=new ArrayList<RestaurantRequestInfo>();
                System.out.println(response);
                for(int i=0;i<reqArray.length();i++){
                    RestaurantRequestInfo restaurantRequestInfo=new RestaurantRequestInfo();
                    JSONObject reqObj=reqArray.getJSONObject(i);
                    restaurantRequestInfo.setIsVerified(reqObj.getString("isVerified"));
                    String verified=reqObj.getString("isVerified");

                    JSONObject restaurant=reqObj.getJSONObject("restaurent");
                    restaurantRequestInfo.setRestaurantName("name");
                    restaurantRequestInfo.setRestaurantImage("imageUrl");

                    String resName=restaurant.getString("name");
                    String imgurl=restaurant.getString("imageUrl");
                    System.out.println(verified+resName+imgurl);
                    restaurantRequestInfos.add(restaurantRequestInfo);


                    if(!(DataHold.restaurantRequestInfo==null)){
                        DataHold.restaurantRequestInfo.clear();
                    }
                    DataHold.restaurantRequestInfo=restaurantRequestInfos;

                    if(!(DataHold.restaurantRequestInfo==null) && (DataHold.restaurantRequestInfo.size()>0)){
                        RestaurantRequestAdapter recyclerViewAdapter= new RestaurantRequestAdapter(RestaurantRequestListActivity.this, restaurantRequestInfos);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(RestaurantRequestListActivity.this));
                    }else{
                        recyclerView.setAdapter(null);
                    }
                    progressDialog.dismiss();
                }


            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
