package dev.edmt.androidcamerarecognitiontext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;

// Google Imported Code
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraView;
    TextView textView;
    TextView textView2;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;

    private void configureBackButton() {
        Button scanActivity = (Button) findViewById(R.id.returnHome);
        scanActivity.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this , HomeActivity.class));
                    }
                }
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure Buttons, Views, Texts, Database
        configureBackButton();
        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);
        textView2 = (TextView) findViewById(R.id.text_view2);

        Context main_context = getApplicationContext();
        final drug_list d;
        try {
            d = new drug_list(main_context);
            // Configure Google's Text Recognizer
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            if (!textRecognizer.isOperational()) {
                Log.w("MainActivity", "Detector dependencies are not yet available");
            } else {
                // Set up the source of the camera
                cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(10.0f)
                        .setAutoFocusEnabled(true)
                        .build();
                // Set up the view displayed by the camera
                cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {

                        try {
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        RequestCameraPermissionID);
                                return;
                            }
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        cameraSource.stop();
                    }
                });


                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {

                        final SparseArray<TextBlock> items = detections.getDetectedItems();

                        Button scanButton = (Button) findViewById(R.id.Scan);

                        scanButton.setOnClickListener(new OnClickListener() {
                            public void onClick (View v) {
                                if(items.size() != 0) {
                                    textView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            textView2.post(new Runnable() {
                                                public void run() {
                                                    StringBuilder stringBuilder = new StringBuilder();
                                                    for (int i = 0; i < 1; i++) {
                                                        TextBlock item = items.valueAt(i);
                                                        stringBuilder.append(item.getValue());
                                                        stringBuilder.append("\n");
                                                    }
                                                    String drug_name = stringBuilder.toString().toLowerCase().trim();
                                                    textView.setText(stringBuilder.toString());
                                                    if (d.find_drug(drug_name) == true) {
                                                        String side_effect = "Strength of Drug: " + d.display_value(drug_name);
                                                        textView2.setText(side_effect);
                                                    }
                                                    else {
                                                        textView2.setText(R.string.Error);
                                                    }
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    textView.setText("No text detected.");
                                    textView2.setText(R.string.Clear);
                                }
                            }
                        });
                    }
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
