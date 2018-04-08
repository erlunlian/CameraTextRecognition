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
    TextView purpose;
    TextView side_effect;
    TextView drug;
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

    // from here to near line 145, some code was borrowed from eddydn: https://github.com/eddydn/AndroidCameraRecognitionText
    // this is where the google api text recognition was imported and integrated in our application
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
        purpose = (TextView) findViewById(R.id.text_view);
        side_effect = (TextView) findViewById(R.id.text_view2);
        drug = (TextView) findViewById(R.id.Drug);

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

                // Set up text recognizer algorithm
                textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {

                        final SparseArray<TextBlock> items = detections.getDetectedItems();

                        Button scan = (Button) findViewById(R.id.Scanner);
                        scan.setOnClickListener(new OnClickListener() {
                            public void onClick (View v) {
                                if(items.size() != 0) {
                                    drug.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // First: Display Drug Name
                                            StringBuilder stringBuilder = new StringBuilder();
                                            for (int i = 0; i < 1; i++) {
                                                TextBlock item = items.valueAt(i);
                                                stringBuilder.append(item.getValue());
                                                stringBuilder.append("\n");
                                            }
                                            final String drug_name = stringBuilder.toString().toLowerCase().trim();
                                            drug.setText(stringBuilder.toString());
                                            // Displaying Purpose
                                            purpose.post(new Runnable() {
                                                public void run() {
                                                    if (d.find_drug(drug_name)) {
                                                        String usage = "Purpose: " + d.display_purpose(drug_name);
                                                        purpose.setText(usage);
                                                    }
                                                    else {
                                                        purpose.setText(R.string.Error);
                                                    }
                                                }
                                            });
                                            // Displaying Side Effects
                                            side_effect.post(new Runnable() {
                                                public void run() {
                                                    if (d.find_drug(drug_name)) {
                                                        String effects = "Side Effects: " + d.display_side_effect(drug_name);
                                                        side_effect.setText(effects);
                                                    }
                                                    else {
                                                        side_effect.setText(R.string.Error);
                                                    }
                                                }
                                            });

                                        }
                                    });

                                } else {
                                    drug.setText("No text detected.");
                                    purpose.setText(R.string.Clear);
                                    side_effect.setText(R.string.Clear);
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
