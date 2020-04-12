package com.example.bookmymeal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookmymeal.Backend.DataHold;
import com.example.bookmymeal.Backend.FAQEntity;
import com.example.bookmymeal.Backend.MVVM.FAQModelView;
import com.example.bookmymeal.Backend.MVVM.ListViewAdapterFAQ;
import com.example.bookmymeal.Backend.MyUrl;
import com.example.bookmymeal.Backend.RecyclerViewFAQAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaqListActivity extends AppCompatActivity implements RecyclerViewFAQAdapter.OnItemClickListener{


    ProgressDialog progressDialog;
    ArrayList<FAQEntity>faqEntityArrayList;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    //private RecyclerViewFAQAdapter recyclerViewFAQAdapter;
    //private ListViewAdapterFAQ listViewAdapterFAQ;
    private ListView listView;
    public static final int ADD_FAQ_REQUEST = 1;
    public static final int EDIT_FAQ_REQUEST = 2;

    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_list);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.faqAddBtn);
        SharedPreferences sharedPreferences =getSharedPreferences("DataFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.contains("Role")){
            role=sharedPreferences.getString("Role","Not Found");
            if(role.equals("Admin")){
                floatingActionButton.setVisibility(View.VISIBLE);
            }
            else{
                floatingActionButton.setVisibility(View.GONE);
            }
        }
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewFAQ);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final RecyclerViewFAQAdapter faqAdapter=new RecyclerViewFAQAdapter(this);
        recyclerView.setAdapter(faqAdapter);
        //listView=(ListView)findViewById(R.id.recyclerViewFAQ);

        final FAQModelView faqModelView= ViewModelProviders.of(FaqListActivity.this).get(FAQModelView.class);
        faqModelView.getFAQLiveData().observe(this, new Observer<List<FAQEntity>>() {
            @Override
            public void onChanged(List<FAQEntity> faqEntities) {
                //recyclerViewFAQAdapter=new RecyclerViewFAQAdapter(FaqListActivity.this,faqEntities);
                //recyclerView.setAdapter(recyclerViewFAQAdapter);
                //listViewAdapterFAQ= new ListViewAdapterFAQ(FaqListActivity.this,faqEntities);
                //listView.setAdapter(listViewAdapterFAQ);
                faqAdapter.setFAQ(faqEntities);
                System.out.println(faqEntities.size());
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String id =String.valueOf(faqAdapter.getFAQAT(viewHolder.getAdapterPosition()).getId());
                System.out.println("Delete:"+id);
                deleteFAQ(id);
            }
        }).attachToRecyclerView(recyclerView);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FaqListActivity.this,AddFaqActivity.class);
                startActivityForResult(intent,ADD_FAQ_REQUEST);
            }
        });

        //getFAQList();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_FAQ_REQUEST && resultCode==RESULT_OK){
            String question = data.getStringExtra(AddFaqActivity.ExtraQuestion);
            String answer = data.getStringExtra(AddFaqActivity.ExtraAnswer);

            System.out.println(question+answer);

            addFAQ(question,answer);

        }
        else if(requestCode==EDIT_FAQ_REQUEST && resultCode==RESULT_OK){
            int id=data.getIntExtra(AddFaqActivity.ExtraId,-1);
            if(id==-1){
                Toast.makeText(FaqListActivity.this,"Item Can't Update",Toast.LENGTH_SHORT).show();
                return;
            }
            String question=data.getStringExtra(AddFaqActivity.ExtraQuestion);
            String answer=data.getStringExtra(AddFaqActivity.ExtraAnswer);
            System.out.println("Edit:"+id+"\n"+question+"\n"+answer);;
            editFAQ(question,answer,id);
        }
    }

    private void editFAQ(String question,String answer,int id){
        String url=MyUrl.EditFAQ+id;
        showDialog();
        JSONObject editfaq=new JSONObject();
        try{
            editfaq.put("id",id);
            editfaq.put("question",question);
            editfaq.put("solution",answer);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, editfaq, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    System.out.println(jsonObject);
                    progressDialog.dismiss();
                    startActivity(new Intent(FaqListActivity.this, FaqListActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(FaqListActivity.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(FaqListActivity.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void addFAQ(String question,String answer){
        showDialog();
        JSONObject addfaq= new JSONObject();
        try{
            addfaq.put("question",question);
            addfaq.put("solution",answer);
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyUrl.FAQ, addfaq, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    System.out.println(jsonObject);
                    progressDialog.dismiss();
                    startActivity(new Intent(FaqListActivity.this,FaqListActivity.class));
                    finish();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(FaqListActivity.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(FaqListActivity.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);


    }

    private void deleteFAQ(String id){
        String url=MyUrl.EditFAQ+id;
        showDialog();
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject= new JSONObject(response.toString());
                    System.out.println(jsonObject);
                    boolean success=jsonObject.getBoolean("isSuccess");

                    if(success){
                        new MaterialAlertDialogBuilder(FaqListActivity.this)
                                .setTitle("Success")
                                .setMessage("Item Deleted")
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(FaqListActivity.this,FaqListActivity.class));
                                        finish();
                                    }
                                })
                                .show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new MaterialAlertDialogBuilder(FaqListActivity.this)
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

        RequestQueue requestQueue = Volley.newRequestQueue(FaqListActivity.this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

            if(!(DataHold.faqEntityArrayList==null) && (DataHold.faqEntityArrayList.size()>0)){

                        //recyclerViewFAQAdapter= new RecyclerViewFAQAdapter(FaqListActivity.this,faqEntityArrayList);
                        //recyclerView.setAdapter(recyclerViewFAQAdapter);
                        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

                progressDialog.dismiss();
            }else{
               // recyclerView.setAdapter(null);
                progressDialog.dismiss();
            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onItemClick(int position) {
        System.out.println(position);
        String value=String.valueOf(DataHold.faqEntityArrayList.get(position).getId());
        String question=DataHold.faqEntityArrayList.get(position).getQuestion();
        String answer=DataHold.faqEntityArrayList.get(position).getSolution();
        System.out.println(value+" "+question);
        String rol=role;
        System.out.println(rol);

            Intent intent=new Intent(FaqListActivity.this,AddFaqActivity.class);
            intent.putExtra(AddFaqActivity.ExtraId,DataHold.faqEntityArrayList.get(position).getId());
            intent.putExtra(AddFaqActivity.ExtraQuestion,question);
            intent.putExtra(AddFaqActivity.ExtraAnswer,answer);
            if(rol==null){

                startActivity(intent);
            }

            else{
                System.out.println("You are Admin");
                startActivityForResult(intent,EDIT_FAQ_REQUEST);
            }





    }
}
