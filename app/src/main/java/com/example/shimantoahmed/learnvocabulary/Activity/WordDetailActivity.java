package com.example.shimantoahmed.learnvocabulary.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelperForSmallData;
import com.example.shimantoahmed.learnvocabulary.model.SmallWord;
import com.example.shimantoahmed.learnvocabulary.model.Word;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordDetailActivity extends AppCompatActivity {

    int wordId;
    DatabaseHelper myDbHelper;
    DatabaseHelperForSmallData databaseHelperForSmallData;
    Word word;
    LinkedHashMap<String,String> namelist;

    List<String> wordcombimelist;
    List<String> meancombimelist;

    List<SmallWord> smallWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        Intent intent = getIntent();
        wordId = intent.getIntExtra("wordId",0);

        myDbHelper = new DatabaseHelper(getApplicationContext());
//        databaseHelperForSmallData = new DatabaseHelperForSmallData(getApplicationContext());





      smallWordList=new ArrayList<>();


//      fetchData();

//        Log.d("vvvvv", String.valueOf(wordId));
//        word=myDbHelper.getSpecificDataById(wordId);
//        Log.d("vvvvv", String.valueOf(word));
//
//        databaseHelperForSmallData.debug();
//
//        smallWordList=databaseHelperForSmallData.getData(word.getEngWord());
//        Log.d("vvvvv", String.valueOf(smallWordList));


        // need to create xml and show data;

    }

    public void createDatabase(){
                try {
            databaseHelperForSmallData.createDataBase();
//            Toast.makeText(MainActivitya.this, "Create Success", Toast.LENGTH_SHORT).show();
            Log.d("xxx ", "createDataBase(): success");
        } catch (IOException ioe) {
            Log.d("xxx ", "createDataBase(): failed");
            throw new Error("Unable to create database");
        }
        databaseHelperForSmallData.openDataBase();
        Log.d("xxx ", "openDataBase(): success");
//            Toast.makeText(MainActivitya.this, "open Success", Toast.LENGTH_SHORT).show();
    }


    public void fetchData()
    {
        databaseHelperForSmallData = new DatabaseHelperForSmallData(getApplicationContext());
//        db =new DatabaseHelper(this);
        try {

            databaseHelperForSmallData.createDataBase();
            databaseHelperForSmallData.openDataBase();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        namelist=new LinkedHashMap<>();
        int ii;
        SQLiteDatabase sd = databaseHelperForSmallData.getReadableDatabase();
        Cursor cursor = sd.query("Dictionary1" ,null, null, null, null, null, null);
        ii=cursor.getColumnIndex("word");
        wordcombimelist=new ArrayList<>();
        meancombimelist= new ArrayList<>();
        Log.d("bbbbb", String.valueOf(cursor.getCount()));

        while (cursor.moveToNext()){
            namelist.put(cursor.getString(ii), cursor.getString(cursor.getColumnIndex("definition")));
        }
        Iterator entries = namelist.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry thisEntry = (Map.Entry) entries.next();
            wordcombimelist.add(String.valueOf(thisEntry.getKey()));
            meancombimelist.add("- "+String.valueOf(thisEntry.getValue()));
        }

        for (int i = 0; i < wordcombimelist.size(); i++) {
            if (wordcombimelist.get(i).equals("abandon")){

                smallWordList.add(new SmallWord(wordcombimelist.get(i),"n", meancombimelist.get(i)));
                break;
            }
        }
//        adapter = new CustomAdapter(data);
//        recyclerView.setAdapter(adapter);
        Log.d("bbbbb", String.valueOf(smallWordList));
    }

}
