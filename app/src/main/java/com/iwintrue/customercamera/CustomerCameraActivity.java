package com.iwintrue.customercamera;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    //声明一个camera对象
    private  Camera  camera;
    //图片的实时预览
    private SurfaceView suf_camera;
    private SurfaceHolder surfaceHolder;
    //相机参数设置
    private Camera.Parameters parameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_camera);
        initView();


    }

    private void initView() {
        suf_camera = (SurfaceView) findViewById(R.id.suf_camera);
        surfaceHolder =  suf_camera.getHolder();
        surfaceHolder.addCallback(this);
    }


    public void click(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                takePhoto();
                break;
        }
    }

    public void takePhoto(){
        parameters = camera.getParameters();
        //设置照片格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setPictureSize(400, 800);
        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"temp.jpeg"));
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                            Intent intent  = new Intent(CustomerCameraActivity.this,ResultActivity.class);
                            startActivity(intent);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
//        camera.takePicture();
    }


    public  void  getCamera(){
        //获取相机实例
        if(camera==null)
            camera = Camera.open();
    }

    public void releaseCamera(){
        if(camera!=null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }

    public  void  setPrive(Camera camera,SurfaceHolder surfaceHolder){
        try {
            camera.setPreviewDisplay(surfaceHolder);
            //开始预览
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        getCamera();
        if(surfaceHolder!=null){
            setPrive(camera,surfaceHolder);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setPrive(camera,surfaceHolder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        setPrive(camera, surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();

    }


}
