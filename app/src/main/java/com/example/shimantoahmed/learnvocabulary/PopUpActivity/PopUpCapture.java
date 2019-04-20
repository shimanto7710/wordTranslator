package com.example.shimantoahmed.learnvocabulary.PopUpActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shimantoahmed.learnvocabulary.Activity.MainActivity;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.others.OcrManager;
import com.google.android.gms.vision.text.TextRecognizer;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

public class PopUpCapture extends AppCompatActivity {
    public static String imagePathInGallery=""; // this path is used to display the image from gallery
    private static String imageFullPath="";
    private Button takePictureButton;
    private ImageView imageHolder;
    private final int requestCode = 20;
    private TextRecognizer textRecognizer;
    private static CharSequence[] selectOptions={"Select photo","Capture a photo","Cancel"};
    TextView txtV;
    ImageView imageView;
    private static final int selectImage=1;
    public static final String DATA_PATH= Environment.getExternalStorageDirectory().toString() + "/tesseract_languages/";
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pop_up_capture);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .8));
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = -20;
        getWindow().setAttributes(layoutParams);


        byte[] byteArray = getIntent().getByteArrayExtra("image");
        final Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

//        final Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
//                R.raw.im);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bmp);


        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Please wait...");
        new Thread() {
            public void run() {
                try {
                    //your code here.....
                    OcrManager manager = new OcrManager();
                    manager.intAPI();
                    String s = manager.startRecognize(bmp);
                    Log.d("wwwww", "result: " + s);
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
                // dismiss the progress dialog
                progressDialog.dismiss();
            }
        }.start();

//        showSelectOptions();

    }

//
//    private void showSelectOptions() {
//        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
//        alertDialog.setItems(selectOptions, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int position) {
//                switch (position){
//                    case 0:
//                        selectPhotoFromGallery(); //dialog option for select photo from gallery
//                        break;
//                    case 1:
//                        capturePhoto();
//                        break;
//                    case 2:
//                        dialog.dismiss();
//                        break;
//                    default:
//                        dialog.dismiss();
//                }
//            }
//        });
//        AlertDialog dialog1=alertDialog.create();
//        dialog1.show();
//    }
//
//    private void selectPhotoFromGallery() {
//        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, selectImage);
//    }
//
//    private void capturePhoto() {
//        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,selectImage);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            Log.d("qqqqq","onActivity");
//            if (requestCode == selectImage && data != null) {
//                System.out.println("Hello");
//                Uri selectedImage = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imageFullPath = cursor.getString(columnIndex);
//                System.out.println("image ...." + imageFullPath);
//                cursor.close();
//                imagePathInGallery = imageFullPath; // give the image path to display image that hoose by user
//
//           /* Bitmap bitmap= BitmapFactory.decodeFile(imageFullPath);
//            System.out.println("bitmap... :" + bitmap);
//            String text=detectText(bitmap);
//            textOfImage=text; // display text of image
//            progreess.dismiss();
//            Toast.makeText(MainActivity.this, "image text :"+text, Toast.LENGTH_LONG).show();
//            changeFragment(new ImageShowFragment());*/
//
//                new LoadImage(imageFullPath).execute();
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            Log.d("qqqqq","error " +e.getMessage());
//        }
//
//    }
//
//
//    public class LoadImage extends AsyncTask<String,Boolean,Void> {
//
//        private String imageUrl="";
//        ProgressDialog progressDialog;
//
//        public LoadImage(String imageFullPath){
//         /*  progressDialog=new ProgressDialog(getApplicationContext());
//           progressDialog.setMessage("Please wait");
//            progressDialog.show();*/
//            this.imageUrl=imageFullPath;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog=new ProgressDialog(getApplicationContext());
//            progressDialog.setMessage("Please wait..");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.setProgress(0);
//            progressDialog.show();
//            Log.d("qqqqq","start pre execution");
//        }
//        @Override
//        protected Void doInBackground(String... params) {
//            //imageUrl=params[0];
//            /* Bitmap bitmap=BitmapFactory.decodeFile(imageFullPath);*/
//            //  progressDialog.dismiss();
//
////            performOcr(imageFullPath);
//            Log.d("qqqqq","image path: "+imageFullPath);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
//            Log.d("qqqqq","post execution");
//            Log.d("qqqqq","result: "+result);
////            changeFragment(new ImageShowFragment());
//
//        }
//        private void performOcr(String imagePath){
//
//            Bitmap bitmap=BitmapFactory.decodeFile(imageFullPath);
//
//            try {
//                ExifInterface exif = new ExifInterface(imagePath);
//                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//                Log.v("Image :", "Orient: " + exifOrientation);
//
//                int rotate = 0;
//                switch (exifOrientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        rotate = 90;
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        rotate = 180;
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        rotate = 270;
//                        break;
//                }
//
//                Log.v("Pic Rotate:", "Rotation: " + rotate);
//
//                if (rotate != 0) {
//
//                    // Getting width & height of the given image.
//                    int w = bitmap.getWidth();
//                    int h = bitmap.getHeight();
//
//                    // Setting pre rotate
//                    Matrix mtx = new Matrix();
//                    mtx.preRotate(rotate);
//
//                    // Rotating Bitmap
//                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
//                    // tesseract req. ARGB_8888
//                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            TessBaseAPI tessBaseAPI = new TessBaseAPI();
//            tessBaseAPI.setDebug(true);
//            tessBaseAPI.init(DATA_PATH, "eng");
//            tessBaseAPI.setImage(bitmap);
//            String text = tessBaseAPI.getUTF8Text();
//            Log.d("data :", "Got data: " + text);
//            tessBaseAPI.end();
//            result=text;
////            imageIsLoad=true;
//
//        }

//    }


}
