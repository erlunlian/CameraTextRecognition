package dev.edmt.androidcamerarecognitiontext;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configureTypeButton();
        configureScanButton();
    }

    private void configureScanButton() {
        Button scanActivity = (Button) findViewById(R.id.toScanActivity);
        if(!hasCamera()) {
            scanActivity.setEnabled(false);
        }
        else {
            scanActivity.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View view) {
                            startActivity(new Intent(HomeActivity.this , MainActivity.class));
                        }
                    }
            );
        }
    }

    private void configureTypeButton() {
        Button TypeActivity = (Button) findViewById(R.id.toTypeActivity);
        TypeActivity.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(HomeActivity.this, TypeActivity.class));
                    }
                }
        );
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
}
