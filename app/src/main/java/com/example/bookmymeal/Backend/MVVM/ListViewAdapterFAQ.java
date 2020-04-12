package com.example.bookmymeal.Backend.MVVM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bookmymeal.Backend.FAQEntity;
import com.example.bookmymeal.R;

import org.w3c.dom.Text;

import java.util.List;

public class ListViewAdapterFAQ extends BaseAdapter {
    private Context mcontext;
    private List<FAQEntity> faqEntityList;

    public ListViewAdapterFAQ(Context context,List<FAQEntity>faqEntities){
        mcontext=context;
        faqEntityList=faqEntities;
    }
    @Override
    public int getCount() {
        return faqEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        return faqEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewAdapterFAQ.ViewHolder holder;
        if(view==null){
            holder= new ListViewAdapterFAQ.ViewHolder();
            LayoutInflater inflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.faq_list_item,null,true);
            //
            holder.textViewQuestionNo=(TextView)view.findViewById(R.id.questionno);
            holder.textViewQ=(TextView)view.findViewById(R.id.faqquestion);
            holder.textViewA=(TextView)view.findViewById(R.id.faqanswer);
            view.setTag(holder);
        }
        else {
            holder=(ListViewAdapterFAQ.ViewHolder)view.getTag();
        }

        holder.textViewQuestionNo.setText(String.valueOf(faqEntityList.get(i).getId()));
        holder.textViewQ.setText(faqEntityList.get(i).getQuestion());
        holder.textViewA.setText(faqEntityList.get(i).getSolution());

        return view;
    }

    private class ViewHolder{
        TextView textViewQ,textViewA,textViewQuestionNo;
    }
}
