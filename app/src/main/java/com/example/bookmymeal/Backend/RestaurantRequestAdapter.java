package com.example.bookmymeal.Backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmymeal.R;
import com.example.bookmymeal.RestaurantRequestListActivity;

import java.util.ArrayList;

public class RestaurantRequestAdapter extends RecyclerView.Adapter<RestaurantRequestAdapter.ViewHolder> {


    private ArrayList<RestaurantRequestInfo>restaurantRequestInfos;
    private Context mcontext;

    public RestaurantRequestAdapter(Context context,ArrayList<RestaurantRequestInfo>restaurantRequestInfos){
        mcontext=context;
        this.restaurantRequestInfos=restaurantRequestInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_request_list_item,parent,false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtVerified.setText(restaurantRequestInfos.get(position).getIsVerified());
        holder.txtResName.setText(restaurantRequestInfos.get(position).getRestaurantName());

    }

    @Override
    public int getItemCount() {
        if(restaurantRequestInfos==null){
            return 0;
        }
        else{
            return  restaurantRequestInfos.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtResName,txtVerified;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtResName=(TextView)itemView.findViewById(R.id.restaurantName);
            imageView=(ImageView) itemView.findViewById(R.id.restaurantImage);
            txtVerified=(TextView)itemView.findViewById(R.id.resIsVerified);
        }
    }
}
