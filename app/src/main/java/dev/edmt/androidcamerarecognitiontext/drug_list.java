package dev.edmt.androidcamerarecognitiontext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;
import android.content.res.AssetManager;
import android.content.Context;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

// drug_list class that contains hashmap and functions manipulating hashmap
// Firebase can be used as database

class drug_list {

    private HashMap<String,String> drug_map = new HashMap<>();

    // Initialize drug_list with all of the drugs & side effects

    drug_list (Context myContext) throws IOException {
        InputStream is = myContext.getAssets().open("Products2.txt");
        BufferedReader inFile = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while (true) {
            String drug_line = inFile.readLine();

            if (inFile.readLine() == null) {
                break;
            }
            String[] parts = drug_line.split("\t");
            drug_map.put(parts[5].toLowerCase(), parts[3]);

        }
        inFile.close();
    }
    boolean find_drug(String name) {
        boolean in_key = false;
        for(HashMap.Entry<String, String> entry:drug_map.entrySet()){
            String key=entry.getKey();
            if(key.equals(name) == true) {
                in_key = true;
                break;
            }
        }
        return in_key;
    }
    String display_value(String name) {
        String output = drug_map.get(name).toString();
        return output;
    }

}
