package com.example.shimantoahmed.learnvocabulary.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.R;

import java.util.List;

/**
 * Created by Shimanto Ahmed on 12/23/2017.
 */

public class MyStringAdapter extends RecyclerView.Adapter<MyStringAdapter.ViewHolder>  {

    public String tag="zzz";
    List<String> listItems,filterList;
    private Context context;
//    CustomFilter filter;
    //private MainActivitya mainActivity;

    public MyStringAdapter(List<String> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        this.filterList=listItems;
    }

    public MyStringAdapter(List<String> listItems) {
        this.listItems = listItems;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.string_list,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String listItem=listItems.get(position);
        holder.que.setText(listItem);




    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView que;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            que =(TextView) itemView.findViewById(R.id.que);

            linearLayout=(LinearLayout) itemView.findViewById(R.id.linear);
        }
    }


//    public void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
//        int states[][] = {{android.R.attr.state_checked}, {}};
//        int colors[] = {checkedColor, uncheckedColor};
//        CompoundButtonCompat.setButtonTintList(checkBox, new
//                ColorStateList(states, colors));
//    }

}
