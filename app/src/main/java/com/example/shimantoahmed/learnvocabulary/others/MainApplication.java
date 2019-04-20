package com.example.shimantoahmed.learnvocabulary.others;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainApplication extends Application {

    public static MainApplication instance=null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        copyTessDataForTextRecognizor();
    }

    private String tessDataPath(){
        return MainApplication.instance.getExternalFilesDir(null)+"/tessdata/";
    }

    public String getTessDataParentDirectory(){
        return MainApplication.instance.getExternalFilesDir(null).getAbsolutePath();
    }

    private void copyTessDataForTextRecognizor(){
        Runnable run=new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager=MainApplication.instance.getAssets();
                OutputStream out =null;
                try{
                    InputStream in=assetManager.open("eng.traineddata");
                    String tesspath=instance.tessDataPath();
                    File tessFolder=new File(tesspath);
                    if (!tessFolder.exists()){
                        tessFolder.mkdir();
                    }
                    String tessData=tesspath+"/"+"eng.traineddata";
                    File tessFile=new File(tessData);
                    if (!tessFile.exists()){
                        out=new FileOutputStream(tessData);
                        byte[] buffer=new byte[1024];
                        int read=in.read(buffer);
                        while (read!=-1){
                            out.write(buffer,0,read);
                            read=in.read(buffer);
                        }
                        Log.d("MainApplication","finish copy tess file");
                    }
                    else {
                        Log.d("MainApplication","tess file exist ");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("MainApplication","could not coppy with this following error: "+e.getMessage());
                }
                finally {
                    try {
                        if(out!=null){
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(run).start();
    }

}
