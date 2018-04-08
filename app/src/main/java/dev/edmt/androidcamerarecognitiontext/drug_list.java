package dev.edmt.androidcamerarecognitiontext;

import java.io.IOException;
import java.util.HashMap;
import java.io.*;
import android.content.Context;
import java.util.List;
import java.util.ArrayList;

// drug_list class that contains hashmap and functions manipulating hashmap
// Firebase can be used as database

class drug_list {

    private HashMap<String,Integer> drug_map = new HashMap<>();
    private List <String> side_effects = new ArrayList<>();
    private List <String> purposes = new ArrayList<>();

    // Initialize drug_list with all of the drugs & side effects

    drug_list (Context myContext) throws IOException {
        InputStream is = myContext.getAssets().open("Products2.txt");
        BufferedReader inFile = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        int i = 0;
        while (true) {
            String drug_line = inFile.readLine();

            if (inFile.readLine() == null) {
                break;
            }
            String[] parts = drug_line.split("\t");
            drug_map.put(parts[5].toLowerCase(), i);
            side_effects.add(parts[1]);
            purposes.add(parts[2]);
            i++;

        }
        inFile.close();
    }
    boolean find_drug(String name) {
        boolean in_key = false;
        for(HashMap.Entry<String, Integer> entry:drug_map.entrySet()){
            String key=entry.getKey();
            if(key.equals(name)) {
                in_key = true;
                break;
            }
        }
        return in_key;
    }
    String display_side_effect(String name) {
        int index = drug_map.get(name);
        return side_effects.get(index);
    }
    String display_purpose(String name) {
        int index = drug_map.get(name);
        return purposes.get(index);
    }
}
