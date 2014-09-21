package com.whitepowder.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.util.Log;

public class ReadFile {
	public static String read_file(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } 
        catch (FileNotFoundException e) {
            Log.i("Error","File not found exception in Maps file");
            return null;
        } 
        catch (UnsupportedEncodingException e) {
            Log.i("Error","Encoding Exception in Maps file");
            return null;
        } 
        catch (IOException e) {
        	Log.i("Error","IO Exception when reading Maps file");
            return null;
        }
    };
}
