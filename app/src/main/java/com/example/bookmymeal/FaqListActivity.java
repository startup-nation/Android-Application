package com.example.bookmymeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.DataHold;
import com.example.bookmymeal.Backend.FAQEntity;
import com.example.bookmymeal.Backend.MyUrl;
import com.example.bookmymeal.Backend.RecyclerViewFAQAdapter;
import com.example.bookmymeal.Backend.RecyclerViewRestaurantRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaqListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ArrayList<FAQEntity>faqEntityArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_list);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewFAQ);

        getFAQList();
    }

    private void showDialog(){
        this.progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setTitle("Processing...");
        progressDialog.show();
    }

    private void getFAQList(){

        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, MyUrl.FAQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(FaqListActivity.this);
        requestQueue.add(stringRequest);
    }

    private void parseData(String response){

        try {
            JSONArray jsonArray = new JSONArray(response);
            faqEntityArrayList=new ArrayList<>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                FAQEntity faqEntity=new FAQEntity();
                faqEntity.setId(jsonObject.getInt("id"));
                faqEntity.setQuestion(jsonObject.getString("question"));
                faqEntity.setSolution(jsonObject.getString("solution"));
                faqEntityArrayList.add(faqEntity);
            }

            if(!(DataHold.faqEntityArrayList==null)){
                DataHold.faqEntityArrayList.clear();
            }
            DataHold.faqEntityArrayList=faqEntityArrayList;
            System.out.println(DataHold.faqEntityArrayList.size());

            if(!(DataHold.restaurantRequestInfo==null) && (DataHold.restaurantRequestInfo.size()>0)){
                RecyclerViewFAQAdapter recyclerViewFAQAdapter= new RecyclerViewFAQAdapter(this,faqEntityArrayList);
                recyclerView.setAdapter(recyclerViewFAQAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                progressDialog.dismiss();
            }else{

                progressDialog.dismiss();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

}
