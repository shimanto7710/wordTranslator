package com.example.shimantoahmed.learnvocabulary.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.adapter.MyStringAdapter;


import java.util.ArrayList;
import java.util.List;


public class FragDictionary extends Fragment  {



    private RecyclerView recyclerView;
    private MyStringAdapter adptr;

    public FragDictionary() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_dictionary, container, false);


        List<String> wordList = new ArrayList<String>();
        wordList.add("abc");
        wordList.add("xyz");

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView11);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adptr = new MyStringAdapter(wordList, getContext());
        recyclerView.setAdapter(adptr);

        return view;
    }



}
