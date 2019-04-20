package com.example.shimantoahmed.learnvocabulary.adapter;

import android.widget.Filter;

import com.example.shimantoahmed.learnvocabulary.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shimanto Ahmed on 12/27/2017.
 */

public class CustomFilter extends Filter {
    MyAdapter adapter;
    List<Word> filterList;

    public CustomFilter(List<Word> filterList, MyAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results=new FilterResults();
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            List<Word> filteredItem=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getEngWord().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredItem.add(filterList.get(i));
                }
            }

            results.count=filteredItem.size();
            results.values=filteredItem;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.listItems= (ArrayList<Word>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
