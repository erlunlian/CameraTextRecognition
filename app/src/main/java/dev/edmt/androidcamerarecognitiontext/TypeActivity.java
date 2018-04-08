package dev.edmt.androidcamerarecognitiontext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Color;
import android.content.res.Resources; // DIP package
import android.util.TypedValue; // DIP package
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.Context;


// I try my best
public class TypeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        configureBackButton();
        final drug_list d;
        Button my_button = (Button) findViewById(R.id.Button1);

        try {
            Context bro = getApplicationContext();
            d = new drug_list(bro);
            my_button.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            EditText input_text = (EditText) findViewById(R.id.editText2);
                            String my_text = input_text.getText().toString();
                            my_text = my_text.toLowerCase().trim();
                            TextView text = (TextView) findViewById(R.id.textView);
                            if (d.find_drug(my_text) == true) {
                                String display = "Dosage: " + d.display_value(my_text);
                                text.setText(display);
                            }
                            else {
                                text.setText(R.string.Error);
                            }
                        }
                    }
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditText input_text = (EditText) findViewById(R.id.editText2);
        input_text.setOnClickListener(
                new EditText.OnClickListener() {
                    public void onClick(View v) {
                        TextView text = (TextView) findViewById(R.id.editText2);
                        text.setText(R.string.Clear);
                    }
                }
        );


    }

    private void configureBackButton() {
        Button scanActivity = (Button) findViewById(R.id.returnHome);
        scanActivity.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        startActivity(new Intent(TypeActivity.this , HomeActivity.class));
                    }
                }
        );
    }
    // If they click the edittext, then make it clear

}

