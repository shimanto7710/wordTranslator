package com.example.shimantoahmed.learnvocabulary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.shimantoahmed.learnvocabulary.model.SmallWord;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DatabaseHelperForSmallData extends SQLiteOpenHelper {



    String DB_PATH = null;
    private static String DB_NAME = "RayDiction.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    public DatabaseHelperForSmallData(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.d("xxx Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        // myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }


    public void debug() {
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select * from dictEnglish;", null);
        Log.d("fffff", "count: " + String.valueOf(cursor.getCount()));
    }

//    public void insertData(String engWord,String bangWord,String bangSyn,String engSyn,String example,String engPron,String bangPron)
//    {
//
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("engWord", engWord);
//        contentValues.put("bangWord", bangWord);
//        contentValues.put("bangSyn", bangSyn);
//        contentValues.put("engSyn", engSyn);
//        contentValues.put("example", example);
//        contentValues.put("engPron", engPron);
//        contentValues.put("bangPron", bangPron);
//        database.insert("bigData", null, contentValues);
////        Log.d("ccccc","enter");
//
//    }


//    public List<Word> getData() {
//        openDataBase();
//        List<Word> dataLis=new ArrayList<>();
//        Cursor cursor = myDataBase.rawQuery("select * from bigData;", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//
//            dataLis.add(new Word(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return dataLis;
////        Log.d("vvvvv", "count: " + String.valueOf(cursor.getCount()));
//    }

//    public Word getSpecificDataById(int id) {
//        openDataBase();
//         Word dataLis=null;
//        Cursor cursor = myDataBase.rawQuery("select * from bigData where id="+id+";", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//
//             dataLis=new Word(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return dataLis;
////        Log.d("vvvvv", "count: " + String.valueOf(cursor.getCount()));
//    }

    public List<SmallWord> getData(String word) {
        openDataBase();
        List<SmallWord> dataLis=new ArrayList<>();
        Log.d("fffff", word);
        Cursor cursor = myDataBase.rawQuery("select * from dictionary1;", null);
//        Cursor cursor = myDataBase.rawQuery("select * from dictionary1 where word="+word+";", null);
        cursor.moveToFirst();
        Log.d("fffff", String.valueOf(cursor.getCount()));
        while (!cursor.isAfterLast()) {
            if (cursor.getString(0).equals(word)){

                dataLis.add(new SmallWord(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
            }
//            dataLis=new Word(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7));
            cursor.moveToNext();
        }
        cursor.close();
        return dataLis;
//        Log.d("vvvvv", "count: " + String.valueOf(cursor.getCount()));
    }


    public Cursor allData(){
        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select * from dictEnglish;", null);
        return cursor;
    }

}
