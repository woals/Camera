package com.yinyxn.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView surfaceView;// 不能直接使用 需要SurfaceHolder(操作SurfaceView的手段)
    SurfaceHolder holder;

    Button button;

    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        button = (Button) findViewById(R.id.button);

        holder = surfaceView.getHolder();
        holder.addCallback(this);

        camera = Camera.open();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 拍照
                camera.takePicture(
                        null,//快门
                        new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {

                                //原始数据
                            }
                        },
                        new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {

                                // JPEG 编码后的数据（会压缩）
//                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                Date d = new Date();

                                String t = d.toString();
                                File file = new File(
                                        Environment.getExternalStorageDirectory(),t+".jpg");

                                try {
                                    // 写数据
                                    FileOutputStream out = new FileOutputStream(file);
                                    out.write(data);
                                    out.close();

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                // 复位预览模式(不设置 拍一次就会停止 不能继续拍)
                                camera.stopPreview();
                                camera.startPreview();

                            }
                        }
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }

    /**
     * 创建
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            // 设置预览(相机采集的数据显示在SurfaceView 中)
            camera.setPreviewDisplay(holder);// holder所控制的SurfaceView
            camera.setDisplayOrientation(90);// 相机显示方向

            // 面部识别
//            camera.setFaceDetectionListener();

            // 设置模式
//            camera.getParameters().setFocusMode();

            // 拍的照片显示方向
//            camera.getParameters().setRotation(90);getParameters()//这个方法中new了一个Parameters 没有和自己设置的关联上
            Camera.Parameters p = camera.getParameters();// 重新实例化一个
            p.setRotation(90);
            p.setJpegQuality(100);
//            p.setPictureSize();拍照大小
//            p.setFocusAreas();
            camera.setParameters(p);

            // 开始预览
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        // 停止预览
        camera.stopPreview();
    }
}
