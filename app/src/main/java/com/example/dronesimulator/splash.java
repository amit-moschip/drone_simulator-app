package com.example.dronesimulator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash extends AppCompatActivity {

    private CameraManager cameraManager;
    private  String cameraId;
    boolean isOn =false;

    private Handler handler;
    private Runnable toggleFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
       boolean hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
       if(hasFlash){
           cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
           try{
               cameraId = cameraManager.getCameraIdList()[0];
               isOn = true;
           }catch (Exception e){
               e.printStackTrace();
           }
       }
     handler = new Handler();
       toggleFlash = new Runnable() {
           @Override
           public void run() {
               isOn = !isOn;
               changeFlashState(isOn);
               handler.postDelayed(this,10000);
           }
       };
       handler.post(toggleFlash);
    }

    private void turnOnFlash(boolean isOn){
        if(cameraId != null){
            try {cameraManager.setTorchMode(cameraId,isOn);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private  void changeFlashState(boolean newValue){
        turnOnFlash(newValue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler != null && toggleFlash != null){
            handler.removeCallbacks(toggleFlash);
        }
        changeFlashState(false);
    }
}