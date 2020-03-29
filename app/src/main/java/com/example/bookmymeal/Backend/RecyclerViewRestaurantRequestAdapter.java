package com.example.bookmymeal.Backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmymeal.R;

import java.util.ArrayList;


public class RecyclerViewRestaurantRequestAdapter extends RecyclerView.Adapter<RecyclerViewRestaurantRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RestaurantRequestInfo>requestInfos;

    public RecyclerViewRestaurantRequestAdapter(Context context,ArrayList<RestaurantRequestInfo>requestInfos){
        this.context=context;
        this.requestInfos=requestInfos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_request_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(requestInfos!=null){
            holder.resName.setText(requestInfos.get(position).getRestaurant());
            holder.resVerified.setText(requestInfos.get(position).getIsVerified());
            holder.resDate.setText(requestInfos.get(position).getRegistrationDateTime());
        }
    }

    @Override
    public int getItemCount() {
        if(requestInfos==null){
            return 0;
        }else {
            return requestInfos.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView resName,resVerified,resDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resName=(TextView)itemView.findViewById(R.id.restaurantName);
            resVerified=(TextView)itemView.findViewById(R.id.resIsVerified);
            resDate=(TextView)itemView.findViewById(R.id.regDate);
        }
    }
}
