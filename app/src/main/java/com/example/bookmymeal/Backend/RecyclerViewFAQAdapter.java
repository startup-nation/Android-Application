package com.example.bookmymeal.Backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmymeal.R;

import java.util.ArrayList;
import java.util.List;

public  class RecyclerViewFAQAdapter  extends RecyclerView.Adapter<RecyclerViewFAQAdapter.ViewHolder>{

    //private Context mcontext;
    private List<FAQEntity>faqEntityList=new ArrayList<>();

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }

   public RecyclerViewFAQAdapter(OnItemClickListener onItemClickListener){
        listener=onItemClickListener;
    }

    public void setFAQ(List<FAQEntity>faqEntityList){
        this.faqEntityList=faqEntityList;
        DataHold.faqEntityArrayList=faqEntityList;

        notifyDataSetChanged();
    }

    public FAQEntity getFAQAT(int position){
        return faqEntityList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list_item,parent,false);
        return new ViewHolder(view,listener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        FAQEntity currentFAQ=faqEntityList.get(position);
        if(faqEntityList!=null){
            holder.textViewQuestionNo.setText(String.valueOf(currentFAQ.getId()));
            holder.textViewQ.setText(currentFAQ.getQuestion());
            holder.textViewA.setText(currentFAQ.getSolution());
        }
    }

    @Override
    public int getItemCount() {
        if(faqEntityList==null) {
            return 0;
        }else{
            return faqEntityList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewQ,textViewA,textViewQuestionNo;
        LinearLayout linearLayout;
        OnItemClickListener onItemClickListener;
        public ViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {

            super(itemView);
            textViewQuestionNo=(TextView)itemView.findViewById(R.id.questionno);
            textViewQ=(TextView)itemView.findViewById(R.id.faqquestion);
            textViewA=(TextView)itemView.findViewById(R.id.faqanswer);
            this.onItemClickListener=onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }


}
