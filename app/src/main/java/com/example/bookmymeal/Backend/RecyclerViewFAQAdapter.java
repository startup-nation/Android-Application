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

public class RecyclerViewFAQAdapter extends RecyclerView.Adapter<RecyclerViewFAQAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FAQEntity>faqEntityArrayList;

    public RecyclerViewFAQAdapter(Context context,ArrayList<FAQEntity>faqEntityArrayList){
        this.context=context;
        this.faqEntityArrayList=faqEntityArrayList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(faqEntityArrayList!=null){
            holder.question.setText(faqEntityArrayList.get(position).getQuestion());
            holder.solution.setText(faqEntityArrayList.get(position).getSolution());
        }
    }

    @Override
    public int getItemCount() {
        if(faqEntityArrayList==null){
            return 0;
        }
        else {
           return faqEntityArrayList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView question,solution;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question=(TextView)itemView.findViewById(R.id.faqquestion);
            solution=(TextView)itemView.findViewById(R.id.faqanswer);
        }
    }
}
