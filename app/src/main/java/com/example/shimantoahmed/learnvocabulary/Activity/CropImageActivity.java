package com.example.shimantoahmed.learnvocabulary.Activity;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shimantoahmed.learnvocabulary.AlertDialogHelper;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.adapter.MultiSelectAdapter;
import com.example.shimantoahmed.learnvocabulary.adapter.MyStringAdapter;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelper;
import com.example.shimantoahmed.learnvocabulary.model.SampleModel;
import com.example.shimantoahmed.learnvocabulary.others.RecyclerItemClickListener;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CropImageActivity extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener{

    //    private CropImageView mCropImageView;
    private RecyclerView recyclerView;
//    private MyStringAdapter adptr;
    private Uri mCropImageUri;
    Button rotateBtn;
    private Menu menu;
//    ImageView captureBtn;

//    TextView countTxt;
    public DatabaseHelper myDatabaseHelper;;
    android.view.ActionMode mActionMode;
    Menu context_menu;

    MultiSelectAdapter multiSelectAdapter;
    boolean isMultiSelect = false;

    ArrayList<SampleModel> user_list = new ArrayList<>();
    ArrayList<SampleModel> multiselect_list = new ArrayList<>();

    AlertDialogHelper alertDialogHelper;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
//        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);
//        captureBtn=(ImageView)findViewById(R.id.capture_btn);



        myDatabaseHelper = new DatabaseHelper(this);

        // get action bar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        // Enabling Up / Back navigation
//        actionBar.setDisplayHomeAsUpEnabled(true);

        alertDialogHelper =new AlertDialogHelper(this);
        data_load();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView11);
        multiSelectAdapter = new MultiSelectAdapter(this,user_list,multiselect_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(multiSelectAdapter);

//        countTxt=(TextView)findViewById(R.id.count_txt);

        myDatabaseHelper.debug();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

//        adptr = new MyStringAdapter(topicList, getApplicationContext());
//        recyclerView.setAdapter(adptr);

//        Button txtBtn = (Button) findViewById(R.id.txtBtn);
//        txtBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TextActivity.class);
//                startActivity(intent);
//            }
//        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else{

                    SampleModel sampleModel= myDatabaseHelper.getInfoOfASpecificItem(user_list.get(position));
                    Intent i = new Intent(getApplicationContext(), FinalWordTranslation.class);
                    i.putExtra("result", sampleModel.getName());
                    startActivity(i);
                }
//                Toast.makeText(getApplicationContext(), "Details Page", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<SampleModel>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);

            }
        }));


//        captureBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TextActivity.class);
//                startActivity(intent);
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    startActivityForResult(takePictureIntent, 400);
//                }



//                final int RC_TAKE_PHOTO = 400;
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
                Uri outputFileUri = getCaptureImageOutputUri();
//
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
////                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, outputFileUri);
//                startActivityForResult(intent, RC_TAKE_PHOTO);




                final int RC_TAKE_PHOTO = 400;

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getApplicationContext().getPackageName() + ".my.package.name.provider", createImageFile());

                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivityForResult(intent, RC_TAKE_PHOTO);

//                File file = null;
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                Uri apkURI = FileProvider.getUriForFile(
//                        getApplicationContext(),
//                        getApplicationContext().getApplicationContext()
//                                .getPackageName() + ".provider", file);
//
//                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                startActivityForResult(install, 400);



            }
        });

//        int count=myDatabaseHelper.getCountItem();
//        countTxt.setText(count+" Items");


    }

    public void data_load() {

        String name[] = {"Gokul", "Rajesh", "Ranjith", "Madhu", "Ameer", "Sonaal"};
        String posting[] = {"Manager", "HR", "Android Developer", "iOS Developer", "Team Leader", "Designer"};
        Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                R.drawable.back);

        List<SampleModel> sampleModelList=new ArrayList<>();
        sampleModelList=myDatabaseHelper.getUserInfo();
//        Log.d("asdfgh", String.valueOf(sampleModelList.size()));
        for (int i = 0; i < sampleModelList.size(); i++) {
            SampleModel mSample = new SampleModel(sampleModelList.get(i).getId(),sampleModelList.get(i).getName(),sampleModelList.get(i).getPosting(),sampleModelList.get(i).getDate(),sampleModelList.get(i).getBitmap());
            user_list.add(mSample);
//            Log.d("pppppp", String.valueOf(user_list.size()));
        }
//        Log.d("pppppp","total: "+sampleModelList.size());


    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(user_list.get(position)))
                multiselect_list.remove(user_list.get(position));
            else
                multiselect_list.add(user_list.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size()+" Selected Item");
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }


    public void refreshAdapter()
    {
        multiSelectAdapter.selected_usersList=multiselect_list;
        multiSelectAdapter.usersList=user_list;
        multiSelectAdapter.notifyDataSetChanged();
    }

    private android.view.ActionMode.Callback mActionModeCallback = new android.view.ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("","Delete Contact","DELETE","CANCEL",1,false);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<SampleModel>();
            refreshAdapter();
        }
    };

    // AlertDialog Callback Functions

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiselect_list.size()>0)
            {
                for(int i=0;i<multiselect_list.size();i++){

                    user_list.remove(multiselect_list.get(i));
                    myDatabaseHelper.deleteItem(multiselect_list.get(i).getId());
                }

                multiSelectAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
                Toast.makeText(getApplicationContext(), "Delete Click", Toast.LENGTH_SHORT).show();
            }
        }
        else if(from==2)
        {
            if (mActionMode != null) {
                mActionMode.finish();
            }
            Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                    R.drawable.back);
            SampleModel mSample = new SampleModel( -1,"Name"+user_list.size(),"Designation"+user_list.size(),"asd",icon);
            user_list.add(mSample);
            multiSelectAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }

    /**
     * On load image button click, start pick image chooser activity.
     */
    public void onLoadImageClick(View view) {

        startActivityForResult(getPickImageChooserIntent(), 200);


    }

    /**
     * Crop the image and set it back to the cropping view.
     */
//    public void onCropImageClick(View view) {
//        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
//        if (cropped != null){
//
//            mCropImageView.setImageBitmap(cropped);
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

            Uri imageUri = getPickImageResultUri(data);
            Log.d("111111", "nn "+String.valueOf(imageUri));

            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {


                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            }
            if (!requirePermissions) {
                Intent i = new Intent(this, ProcessActivity.class);
                i.putExtra("data", imageUri.toString());
                startActivity(i);


            }
        }

        else if (requestCode == 400 && resultCode == Activity.RESULT_OK){



            Uri imageUri = getPickImageResultUri(data);
//            Log.d("111111", "nn "+String.valueOf(imageUri));




            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {


                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            }
            if (!requirePermissions) {
                Intent i = new Intent(this, ProcessActivity.class);
                i.putExtra("data", imageUri.toString());
                startActivity(i);


            }

        }




    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.d("444444", "OnRequest");
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            popup(getWindow().getDecorView(), mCropImageUri);
            Log.d("444444", "mCropImageUri image Uri: " + mCropImageUri);

//            mCropImageView.setImageUriAsync(mCropImageUri);
            Intent i = new Intent(this, ProcessActivity.class);
            i.putExtra("data", mCropImageUri.toString());
            startActivity(i);
        } else {
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        Log.d("444444", "clicked get Image picker");
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
//                Log.d("444444","camera");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.example.shimantoahmed.learnvocabulary.Activity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }




    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (e.getCause() instanceof ErrnoException) {
                    return true;
                }
            }
        } catch (Exception e) {
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
//                    Log.d("main","OnLongPress");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        int positionOfMenuItem = 0;
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("My red MenuItem");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
//        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.text:

                Intent intent2 = new Intent(getApplicationContext(), TextActivity.class);
                startActivity(intent2);

                // search action
                return true;

            case R.id.gallery:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 400);
                // search action
                return true;
            case R.id.file:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("image/*");
                startActivityForResult(Intent.createChooser(intent1, "Select Picture"),400);

                // search action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
