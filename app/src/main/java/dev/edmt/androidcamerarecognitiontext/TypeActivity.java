package dev.edmt.androidcamerarecognitiontext;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.Context;


public class TypeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        configureBackButton();
        final drug_list d;
        Button my_button = (Button) findViewById(R.id.Button1);
        Button purpose_button = (Button) findViewById(R.id.Purpose);

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
                                String display = "Side Effects: " + d.display_side_effect(my_text);
                                text.setText(display);
                            }
                            else {
                                text.setText(R.string.Error);
                            }
                        }
                    }
            );
            purpose_button.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            EditText input_text = (EditText) findViewById(R.id.editText2);
                            String my_text = input_text.getText().toString();
                            my_text = my_text.toLowerCase().trim();
                            TextView text = (TextView) findViewById(R.id.textView);
                            if (d.find_drug(my_text)) {
                                String display = "Main Usage: " + d.display_purpose(my_text);
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
}

