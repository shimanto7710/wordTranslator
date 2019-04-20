package com.example.shimantoahmed.learnvocabulary.Fragment;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.shimantoahmed.learnvocabulary.Activity.CropImageActivity;
import com.example.shimantoahmed.learnvocabulary.Activity.ProcessActivity;
import com.example.shimantoahmed.learnvocabulary.Activity.ResultActivity;
import com.example.shimantoahmed.learnvocabulary.Activity.TestActivity;
import com.example.shimantoahmed.learnvocabulary.R;
import com.example.shimantoahmed.learnvocabulary.db.DatabaseHelperForSmallData;
import com.google.android.gms.vision.text.TextRecognizer;


import com.example.shimantoahmed.learnvocabulary.Activity.WordTranslation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class ImageProcessingFrag extends Fragment {

    View view;
    ImageView imageView;
    Button btnTakeInput, btnCapture, btnloadImage;
    private final int requestCode = 20;
    private TextRecognizer textRecognizer;
    DatabaseHelperForSmallData databaseHelperForSmallData;

    public ImageProcessingFrag() {
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
        view = inflater.inflate(R.layout.fragment_image_processing, container, false);
        imageView=(ImageView)view.findViewById(R.id.imageView);

//        if (checkSelfPermission(Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA},
//                    requestCode);
//        }



        btnTakeInput = (Button) view.findViewById(R.id.takeInput);
        btnCapture = (Button) view.findViewById(R.id.captureImage);
        btnloadImage = (Button) view.findViewById(R.id.loadImage);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED){
//                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, requestCode);
//                }
//
//                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(photoCaptureIntent, requestCode);

                Intent i=new Intent(getActivity(),ResultActivity.class);
                i.putExtra("btn",2);
                startActivity(i);
            }
        });


        btnloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),WordTranslation.class);
//                i.putExtra("btn",1);
                startActivity(i);


//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, 10);


            }
        });

        btnTakeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),CropImageActivity.class);
//                i.putExtra("btn",1);
                startActivity(i);



            }
        });




        return view;
    }


    public void capturePopup(View view) {

        final View popUpView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        ViewGroup container = (ViewGroup) popUpView;
        final PopupWindow popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView textView=(TextView)popUpView.findViewById(R.id.popup_text);
//        textView.setText("popup");

        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.CENTER, location[0], location[0] + view.getHeight());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//            textRecognizer = new TextRecognizer.Builder(getContext()).build();
//
//            SparseArray<TextBlock> item = textRecognizer.detect(frame);
//            StringBuilder stringBuilder = new StringBuilder();
//
//            for (int i = 0; i < item.size(); i++) {
//                stringBuilder.append(item.valueAt(i).getValue());
//            }

//            OcrManager manager = new OcrManager();
//            manager.intAPI();
////            String s = String.valueOf(stringBuilder);
//            String s = manager.startRecognize(bitmap);
//            Log.d("fffff", "result: " + s);

            //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            imageView.setImageBitmap(bitmap);
            Intent in1 = new Intent(getActivity(), ResultActivity.class);
            in1.putExtra("image",byteArray);
            startActivity(in1);



        } else if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

//                Frame frame = new Frame.Builder().setBitmap(selectedImage).build();
//                textRecognizer = new TextRecognizer.Builder(getContext()).build();
//
//                SparseArray<TextBlock> item = textRecognizer.detect(frame);
//                StringBuilder stringBuilder = new StringBuilder();
//
//                for (int i = 0; i < item.size(); i++) {
//                    stringBuilder.append(item.valueAt(i).getValue());
//                }
//
//                String s = String.valueOf(stringBuilder);

//                OcrManager manager = new OcrManager();
//                manager.intAPI();
//                String s = manager.startRecognize(selectedImage);
//                Log.d("fffff", "result: " + s);

                //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
                imageView.setImageBitmap(selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent in1 = new Intent(getActivity(), WordTranslation.class);
                in1.putExtra("image",byteArray);
                startActivity(in1);



            } catch (Exception e) {
                e.printStackTrace();
                Log.d("mmmmm", e.getMessage());
//                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }

    }


//    @Override

//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == requestCode) {
//
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
//
//            } else {
//
//                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
//
//            }
//
//        }}//end onRequestPermissionsResult


}


