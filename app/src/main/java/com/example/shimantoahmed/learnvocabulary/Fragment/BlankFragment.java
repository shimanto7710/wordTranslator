package com.example.shimantoahmed.learnvocabulary.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.SearchView;
//import android.widget.SearchView;

import com.example.shimantoahmed.learnvocabulary.Activity.WordDetailActivity;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.adapter.MyAdapter;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelperForSmallData;
import com.example.shimantoahmed.learnvocabulary.model.Word;
//import com.example.shimantoahmed.learnvocabulary.others.Dictionary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class BlankFragment extends Fragment implements SearchView.OnQueryTextListener {


    //    DictionaryDBOpenHelper dbHelper;;
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private List<Word> wordLists, finalWordList;
    private MyAdapter adptr;
    DatabaseHelper myDbHelper;
    DatabaseHelperForSmallData databaseHelperForSmallData;


    //    MaterialSearchView searchView;
    SearchView textSearch;

    public BlankFragment() {
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
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        myDbHelper = new DatabaseHelper(getContext());
        initializeDatabase();
//        copyData();
        databaseHelperForSmallData = new DatabaseHelperForSmallData(getContext());
//        searchView = (MaterialSearchView) view.findViewById(R.id.search_view) ;
        textSearch = (SearchView) view.findViewById(R.id.sView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wordLists = new ArrayList<>();
        finalWordList = new ArrayList<>();

//        finalWordList=myDbHelper.getData();


        adptr = new MyAdapter(finalWordList, getContext());
        recyclerView.setAdapter(adptr);

        textSearch.setOnQueryTextListener(this);


//        myDbHelper.getData();
//        initializeDatabase();


        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                Intent i = new Intent(getContext(), WordDetailActivity.class);
                i.putExtra("wordId", wordLists.get(position).getId());
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {


            }
        }));

        return view;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        adptr.getFilter().filter(newText);

        if (newText.length() == 0) {
            adptr = new MyAdapter(finalWordList, getContext());
            recyclerView.setAdapter(adptr);
        } else {
            wordLists.clear();
            wordLists = myDbHelper.getSearchedData(newText);
            adptr = new MyAdapter(wordLists, getContext());
            recyclerView.setAdapter(adptr);
        }
        return false;
    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    // for test
    public void getWord() {
        try {
            InputStream path = getActivity().getAssets().open("BengaliDictionary.json");
            int size = path.available();
            byte[] buffer = new byte[size];
            path.read(buffer);
            path.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String en, example, bn_syns, en_syns, pron, bn;
                pron = obj.getString("pron");
                bn = obj.getString("bn");
                en = obj.getString("en");
                example = obj.getString("sents");
                bn_syns = obj.getString("bn_syns");
                en_syns = obj.getString("en_syns");

                if (en.equals("abandon")) {

//                    correctStrings = spellChecker.getSuggestionSpellsOfAWord(bn);
//                    for (int j = 0; i < correctStrings.size(); j++) {
//                        Log.d("ccccc","spell: "+ correctStrings.get(j));
//                    }

                    Log.d("ccccc", "Eng: " + en);
                    Log.d("ccccc", "Bng: " + bn);
                    Log.d("ccccc", "Pron: " + pron);
                    Log.d("ccccc", "en_syns: " + en_syns);
                    Log.d("ccccc", "bn_syns: " + bn_syns);
                    Log.d("ccccc", "example: " + example);


                    break;
                }


            }
//            System.out.println(dictionary.search("heart"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // nothing
//    public void getWord2()
//    {
//        try {
//            InputStream path = getActivity().getAssets().open("BengaliDictionary_17.txt");
//            Dictionary dictionary = new Dictionary(path);
//            String a = dictionary.search("abandon");
//            Log.d("ccccc", a);
////            System.out.println(dictionary.search("heart"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    // data initialization to sample database by databaseHelper
    public void initializeDatabase() {


        try {
            myDbHelper.createDataBase();
//            Toast.makeText(MainActivitya.this, "Create Success", Toast.LENGTH_SHORT).show();
            Log.d("xxx ", "createDataBase(): success");
        } catch (IOException ioe) {
            Log.d("xxx ", "createDataBase(): failed");
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
            Log.d("xxx ", "openDataBase(): success");
//            Toast.makeText(MainActivitya.this, "open Success", Toast.LENGTH_SHORT).show();
        } catch (SQLException sqle) {
            Log.d("xxx ", "openDataBase(): faild");
            throw sqle;
        }

//
//        String en, example, bn_syns = null, en_syns = null, pron, bn,p1 = null,p2 = null;
//        try {
//            InputStream path = getActivity().getAssets().open("BengaliDictionary.json");
//            int size = path.available();
//            byte[] buffer = new byte[size];
//            path.read(buffer);
//            path.close();
//            String json = new String(buffer, "UTF-8");
//            JSONArray jsonArray = new JSONArray(json);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject obj = jsonArray.getJSONObject(i);
//
//
//                pron = obj.getString("pron");
//                bn = obj.getString("bn");
//                en = obj.getString("en");
//                example = obj.getString("sents");
//                bn_syns = obj.getString("bn_syns");
//                en_syns = obj.getString("en_syns");
//
//
//                String string = pron;
//                String[] splitted= string.split(",");
//                p1= splitted[0]; //live
//                p2 = splitted[1]; //coding
//                bn=trim2(bn);
//                en=trim2(en);
//                en_syns=trim(en_syns);
//                bn_syns=trim(bn_syns);
//                example=trim(example);
//                p1=trim2(p1);
//                p2=trim2(p2);
//
//                myDbHelper.insertData(en,bn,bn_syns,en_syns,example,p1,p2);
//
//
////                if (en.equals("abandon")) {
////
//////                    correctStrings = spellChecker.getSuggestionSpellsOfAWord(bn);
//////                    for (int j = 0; i < correctStrings.size(); j++) {
//////                        Log.d("ccccc","spell: "+ correctStrings.get(j));
//////                    }
////
//////                    Log.d("ccccc", "Eng: " + en);
//////                    Log.d("ccccc", "Bng: " + bn);
//////                    Log.d("ccccc", "Pron: " + pron);
//////                    Log.d("ccccc", "en_syns: " + en_syns);
//////                    Log.d("ccccc", "bn_syns: " + bn_syns);
//////                    Log.d("ccccc", "example: " + example);
//////                    Log.d("ccccc", "engPron: " + p1);
//////                    Log.d("ccccc", "bangPron: " + p2);
////
////
////
////
////                    break;
////                }
//
//
//            }
////            System.out.println(dictionary.search("heart"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public String trim(String raw) {
        String result = "";
        String[] splitted = raw.split(",");

        boolean f = false;

        for (int i = 0; i < splitted.length; i++) {

            String[] a = splitted[i].split("\"");
            for (int j = 0; j < a.length; j++) {
                if (a[j].length() > 2) {
                    result += a[j];
                    result += ",";
//                    Log.d("ccccc",a[j]);
//                    f=true;
                }
//                if (true){
//                    f=false;
//                    result+=",";
//                }
            }
        }

        return result;
    }

    public String trim2(String raw) {
        String result = "";
        String[] splitted = raw.split(",");

        boolean f = false;

        for (int i = 0; i < splitted.length; i++) {

            String[] a = splitted[i].split("\"");
            for (int j = 0; j < a.length; j++) {
                if (a[j].length() > 2) {
                    result += a[j];
//                    result+=",";
//                    Log.d("ccccc",a[j]);
//                    f=true;
                }
//                if (true){
//                    f=false;
//                    result+=",";
//                }
            }
        }

        return result;
    }

    public void copyData() {
//        myDbHelper.copyData();
        myDbHelper.margeDatabase();
    }

}
