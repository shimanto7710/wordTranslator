package com.example.shimantoahmed.learnvocabulary.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.others.OcrManager;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProcessActivity extends AppCompatActivity {

    private CropImageView mCropImageView;
    Button cropBtn, rotateBtn, dBtn;
    private Uri mCropImageUri;
    OcrManager manager;
    private TextRecognizer textRecognizer;
    String result;
    ImageView imageView;
    LinearLayout doneLinear, otherLinear;
    String url;
    public DatabaseHelper myDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
        imageView = (ImageView) findViewById(R.id.image_view);

        cropBtn = (Button) findViewById(R.id.doneBtn);
//        dBtn = (Button) findViewById(R.id.d_btn);
        rotateBtn = (Button) findViewById(R.id.rotate_btn);
        otherLinear = (LinearLayout) findViewById(R.id.other_linear);
//        doneLinear = (LinearLayout) findViewById(R.id.done_linear);
        myDatabaseHelper = new DatabaseHelper(this);
        Intent i = getIntent();
         url = i.getStringExtra("data");
        Log.d("ggggg ", "Process: " + url);
        mCropImageUri = Uri.parse(url);

        manager = new OcrManager();
        manager.intAPI();
        // ----------------------------------------------------  Crop Image  --------------
//        mCropImageView.setImageUriAsync(mCropImageUri);

//        ExifInterface ei = null;
//        try {
//            ei = new ExifInterface(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_UNDEFINED);



        Bitmap b = null;
        try {
            b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Bitmap rotatedBitmap = null;
//        switch(orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                rotatedBitmap = rotateImage(b, 90);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                rotatedBitmap = rotateImage(b, 180);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                rotatedBitmap = rotateImage(b, 270);
//                break;
//
//            case ExifInterface.ORIENTATION_NORMAL:
//            default:
//                rotatedBitmap = b;
//        }



        mCropImageView.setImageBitmap(b);

//        Bitmap rotatedBitmap=rotateBitmap(b,90);
//        imageView.setImageBitmap(b);
//        mCropImageView.setImageBitmap(b);


        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
                    if (cropped != null) {
                        new Process(cropped, v.getContext()).execute();
                    }
                } catch (Exception e) {
                    Log.e("eeeee", e.getMessage());
                }

            }
        });
//        dBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
//                    if (cropped != null) {
////                        new EasyProcess(cropped, v.getContext()).execute();
////                        new EasyProcess(cropped,v.getContext()).execute();
//                        new Process(cropped, v.getContext()).execute();
//                    }
//                } catch (Exception e) {
//                    Log.e("eeeee", e.getMessage());
//                }
//            }
//        });

        final float[] degree = {90};
        final Bitmap finalB = b;
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bMap = rotateBitmap(finalB, degree[0]);
                degree[0] +=90;
                mCropImageView.setImageBitmap(bMap);
            }
        });

    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }


    public class EasyProcess extends AsyncTask<String, Boolean, Void> {

        private Bitmap bitmap;
        ProgressDialog progressDialog;
        Context context;


        public EasyProcess(Bitmap bitmap, Context context) {
         /*  progressDialog=new ProgressDialog(getApplicationContext());
           progressDialog.setMessage("Please wait");
            progressDialog.show();*/
            this.bitmap = bitmap;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "", "Please wait...");


        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                SparseArray<TextBlock> item = textRecognizer.detect(frame);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < item.size(); i++) {
                    stringBuilder.append(item.valueAt(i).getValue());
                }
                result = String.valueOf(stringBuilder);

            } catch (Exception e) {
                Log.e("eeeee", "easy: " + e.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

//            SharedPreferences.Editor editor = getSharedPreferences("myPref", MODE_PRIVATE).edit();
//            editor.putString("result", result);
//            editor.apply();
            if (!result.equals(null)) {

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();

                Log.d("ttttt", "result :" + result);
                Intent i = new Intent(context, FinalWordTranslation.class);
                i.putExtra("result", result);
//            i.putExtra("image",byteArray);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Can not be converted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class Process extends AsyncTask<String, Boolean, Void> {

        private Bitmap bitmap;
        ProgressDialog progressDialog;
        Context context;


        public Process(Bitmap bitmap, Context context) {
         /*  progressDialog=new ProgressDialog(getApplicationContext());
           progressDialog.setMessage("Please wait");
            progressDialog.show();*/
            this.bitmap = bitmap;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "", "Please wait...");


        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                result = manager.startRecognize(bitmap);
            } catch (Exception e) {
                Log.e("eeeee", "advance: " + e.getMessage());
            }

            if (!result.equals(null)) {

                final int THUMBSIZE = 64;

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(url),
                        THUMBSIZE, THUMBSIZE);

                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Log.d("456789", String.valueOf(byteArray));
                bitmap.recycle();
                Log.d("456789", String.valueOf(mydate));

                Log.d("444444","result "+result);
                boolean isDuplicate= myDatabaseHelper.checkDuplicateItem(result);
                if (!isDuplicate){
                    Log.d("777777","Fresh");
                    myDatabaseHelper.insertImg(byteArray,mydate,result);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            try {


                if (!result.equals(null)) {

//                    final int THUMBSIZE = 64;
//
//                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(url),
//                            THUMBSIZE, THUMBSIZE);
//
//                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//                    Log.d("456789", String.valueOf(byteArray));
//                    bitmap.recycle();
//                    Log.d("456789", String.valueOf(mydate));
//
//                    Log.d("444444","result "+result);
//                    boolean isDuplicate= myDatabaseHelper.checkDuplicateItem(result);
//                    if (!isDuplicate){
//                        Log.d("777777","Fresh");
//                        myDatabaseHelper.insertImg(byteArray,mydate,result);
//                    }

                    Intent i = new Intent(context, FinalWordTranslation.class);
                    i.putExtra("result", result);


                    startActivity(i);
                } else new EasyProcess(bitmap, context).execute();

            } catch (Exception e) {
                Log.e("qqqqq", "error : " + e.getMessage());
            }
        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        Matrix matrix = new Matrix();

        matrix.postRotate(degrees);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, original.getWidth(), original.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                Intent newIntent = new Intent(getApplicationContext(),CropImageActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                break;
        }
        return true;
    }

}
