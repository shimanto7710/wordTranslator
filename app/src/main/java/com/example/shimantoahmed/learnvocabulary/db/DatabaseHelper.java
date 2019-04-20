package com.example.shimantoahmed.learnvocabulary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.shimantoahmed.learnvocabulary.model.SampleModel;
import com.example.shimantoahmed.learnvocabulary.model.SmallWord;
import com.example.shimantoahmed.learnvocabulary.model.Word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.shimantoahmed.learnvocabulary.Activity.ProcessActivity.getBitmapAsByteArray;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    int newTourParentId;

    String DB_PATH = null;
    private static String DB_NAME = "sample.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

//    public static final String COL_1 = "";
//    public static final String COL_2 = "NAME";
//    public static final String COL_3 = "SURNAME";
//    public static final String COL_4 = "MARKS";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 10);
        this.myContext = context;
//        this.DB_PATH= String.valueOf(context.getDatabasePath(DB_NAME));

//        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context,DB_NAME);
//        SQLiteDatabase database = helper.getReadableDatabase();
//        String filePath = database.getPath();
//        database.close()

        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.d("xxx Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            this.close();
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
        Cursor cursor = myDataBase.rawQuery("select * from user_info;", null);
        Log.d("cccc", "count: " + String.valueOf(cursor.getCount()));
    }

    public void insertData(String engWord, String bangWord, String bangSyn, String engSyn, String example, String engPron, String bangPron) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("engWord", engWord);
        contentValues.put("bangWord", bangWord);
        contentValues.put("bangSyn", bangSyn);
        contentValues.put("engSyn", engSyn);
        contentValues.put("example", example);
        contentValues.put("engPron", engPron);
        contentValues.put("bangPron", bangPron);
        database.insert("bigData", null, contentValues);
//        Log.d("ccccc","enter");

    }


    public List<Word> getData() {
        openDataBase();
        List<Word> dataLis = new ArrayList<>();
        Cursor cursor = myDataBase.rawQuery("select * from bigData;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            dataLis.add(new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)));
            cursor.moveToNext();
        }
        cursor.close();
        return dataLis;

    }

//    public Word getSpecificDataById(int id) {
//        openDataBase();
//        Word dataLis = null;
//        Cursor cursor = myDataBase.rawQuery("select * from bigData where id=" + id + ";", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//
//            dataLis = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return dataLis;
////        Log.d("vvvvv", "count: " + String.valueOf(cursor.getCount()));
//    }


    public List<Word> getSearchedData(String newText) {
        openDataBase();
        List<Word> dataLis = new ArrayList<>();
        Cursor cursor = myDataBase.rawQuery("select * from bigData where engWord like '" + newText + "%';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            dataLis.add(new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return dataLis;
    }

    public void copyData() {
        List<Word> wordLists;
        wordLists = new ArrayList<>();
        wordLists = this.getData();

        Log.d("55555", "started");


        openDataBase();
        Cursor cursor = myDataBase.rawQuery("select * from mainDictionary;", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Cursor cursor1 = null;

//            Cursor cursor1 = myDataBase.rawQuery("select * from bigData where engWord='" + cursor.getString(1) + "';", null);
            cursor1 = myDataBase.rawQuery("select * from bigData where engWord=?", new String[]{cursor.getString(1)});
//            rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});


            if (cursor1.getCount() > 0) {
                if (!cursor.getString(4).equals(null)) {

                    cursor1.moveToFirst();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("type", cursor.getString(4));

//                    Log.d("55555", "update Id: " + cursor1.getInt(0));
//                    Log.d("55555", "update type: " + cursor.getString(4));
//                    Log.d("55555", "update " + cursor1.getString(1));

                    update(contentValues, cursor1.getInt(0));
                }
            } else {
//                Log.d("22222","id "+cursor.getInt(0));
//                Log.d("22222","engWord "+cursor.getString(1));
//                Log.d("22222","bangWord "+cursor.getString(2));
//                Log.d("22222","bangSyn "+cursor.getString(5));
//                Log.d("22222","bangPron "+cursor.getString(3));
//                Log.d("22222","type "+cursor.getString(4));
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("engWord", cursor.getString(1));
                contentValues1.put("bangWord", cursor.getString(2));
                contentValues1.put("bangSyn", cursor.getString(5));
                contentValues1.put("bangPron", cursor.getString(3));
                contentValues1.put("type", cursor.getString(4));
                insert(contentValues1);
//                Log.d("55555", "inserted Data " + cursor.getString(1));
            }
            cursor1.close();

            cursor.moveToNext();
        }
        cursor.close();
    }

    public void insert(ContentValues contentValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert("bigData", null, contentValues);
        database.close();
    }

    public void update(ContentValues contentValues, int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.update("bigData", contentValues, "id = ?", new String[]{String.valueOf(id)});
        database.close();
    }

    public void margeDatabase() {


        openDataBase();
        DatabaseHelperForSmallData databaseHelperForSmallData = new DatabaseHelperForSmallData(myContext);
        Cursor cursor = databaseHelperForSmallData.allData();
        cursor.moveToFirst();
        Log.d("ffffff", "2nd Database Size: " + cursor.getCount());
        while (!cursor.isAfterLast()) {
            Cursor cursor1 = null;
            cursor1 = myDataBase.rawQuery("select * from bigData where engWord=?", new String[]{cursor.getString(1)});
            cursor1.moveToFirst();
            if (cursor1.getCount() > 0) {

//                Log.d("fffff", String.valueOf(cursor1.getCount()));
//                Log.d("fffff", cursor1.getString(1));
//
//                Log.d("fffff","syn: "+cursor1.getString(4));
//                if (compare(cursor1.getString(4),null) || compare(cursor1.getString(4),"")){
//                    Log.d("fffff","true");
//                }
                if (!compare(cursor1.getString(4), null) || !compare(cursor1.getString(4), "")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("engSyn", cursor.getString(3));
                    update(contentValues, cursor1.getInt(0));
//                    Log.d("fffff","engSyn : "+cursor1.getString(1)+"  "+cursor.getString(3));
                }
                if (!compare(cursor1.getString(5), null) || !compare(cursor1.getString(5), "")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("example", cursor.getString(5));
                    update(contentValues, cursor1.getInt(0));
//                    Log.d("fffff","example : "+cursor1.getString(1)+"  "+cursor.getString(5));

                }
                if (!compare(cursor1.getString(9), null) || !compare(cursor1.getString(9), "")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("defination", cursor.getString(6));
                    update(contentValues, cursor1.getInt(0));
//                    Log.d("fffff","defination : "+cursor1.getString(1)+"  "+cursor.getString(6));

                }
                if (!compare(cursor1.getString(10), null) || !compare(cursor1.getString(10), "")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("antonyms", cursor.getString(4));
                    update(contentValues, cursor1.getInt(0));
//                    Log.d("fffff","antonyms : "+cursor1.getString(1)+"  "+cursor.getString(4));
                }

            }
            cursor1.close();

            cursor.moveToNext();
        }
        cursor.close();
    }

    public static boolean compare(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    public Word getWord(String word) {
        openDataBase();

        word = word.toLowerCase();
        Log.d("kkkkk","word: "+word);
//        Cursor cursor = myDataBase.rawQuery("select * from bigData where engWord='" + word + "';", null);
        Cursor cursor = myDataBase.rawQuery("select * from bigData where engWord=?", new String[]{word});

        cursor.moveToFirst();
        Word w;
        if (cursor.getCount()!=0){
            w=new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
        }
        else return null;

        cursor.close();
        return w;
    }


    public void insertImg( byte[]data,String time,String result ) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("title", result);
        contentValues1.put("time", time);

        contentValues1.put("image", data);
//        insert(contentValues1);

        long newRowId=database.insert("user_info", null, contentValues1);

//        String query =  "INSERT  INTO user_info(title,time,image) " +
//                "VALUES ('"+result+"','"+time+"','"+data+"');";
//        Log.d("777777","inserted Id "+newRowId);
//        database.execSQL(query);
        database.close();

    }

    public List<SampleModel> getUserInfo() {
        openDataBase();


        Cursor cursor = myDataBase.rawQuery("select * from user_info;",null);
        Log.d("pppppp","Count: "+cursor.getCount());

        cursor.moveToFirst();
        List<SampleModel>sampleModelList = new ArrayList<>();
        if (cursor.getCount()!=0){
            while (!cursor.isAfterLast()) {

                byte[] imgByte = cursor.getBlob(4);
                Bitmap bitmap=BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                sampleModelList.add(new SampleModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),bitmap));

                cursor.moveToNext();
            }
        }

        cursor.close();

        return sampleModelList;
    }

    public boolean checkDuplicateItem(String result) {
        openDataBase();


        Cursor cursor = myDataBase.rawQuery("select * from user_info where title=?", new String[]{result});
//        Log.d("pppppp","Count: "+cursor.getCount());

        cursor.moveToFirst();
        List<SampleModel>sampleModelList = new ArrayList<>();
        if (cursor.getCount()!=0){
            return true;
        }

        cursor.close();

        return false;
    }

    public void deleteItem(int id) {

        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("user_info", "id" + "=" + id, null);
        database.close();

    }

    public int getCountItem() {
        openDataBase();


        Cursor cursor = myDataBase.rawQuery("select * from user_info;",null);
        Log.d("qqqqqq","ccc: "+cursor.getCount());

        cursor.moveToFirst();
//        List<SampleModel>sampleModelList = new ArrayList<>();
        if (cursor.getCount()!=0){
            return cursor.getCount();
        }

        return 0;
    }

    public SampleModel getInfoOfASpecificItem(SampleModel sampleModel) {
        openDataBase();

//        word = word.toLowerCase();
//        Log.d("kkkkk","word: "+word);
//        Cursor cursor = myDataBase.rawQuery("select * from bigData where engWord='" + word + "';", null);
        Cursor cursor = myDataBase.rawQuery("select * from user_info where id=?", new String[]{String.valueOf(sampleModel.getId())});

        cursor.moveToFirst();
        if (cursor.getCount()!=0){
            byte[] imgByte = cursor.getBlob(4);
            Bitmap bitmap=BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            return new SampleModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),bitmap);
//            w=new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
        }

        cursor.close();

        return null;
    }


}

//class MySQLiteOpenHelper extends SQLiteOpenHelper {
//
//    MySQLiteOpenHelper(Context context, String databaseName) {
//        super(context, databaseName, null, 2);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }
//}