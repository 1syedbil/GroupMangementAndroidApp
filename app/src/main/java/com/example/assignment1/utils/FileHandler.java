package com.example.assignment1.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.OutputStream;

public class FileHandler {

    private static final String TAG = "FileHandler";

    public static File createCSVFile(Context context, List<String> tasks, String employeeName) {
        String fileName = employeeName + "_tasks.csv";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Use MediaStore API for Android 11 and higher
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        // Write the header
                        outputStream.write("Task Description\n".getBytes());

                        // Write each task to the file
                        for (String task : tasks) {
                            outputStream.write((task + "\n").getBytes());
                        }

                        Log.d(TAG, "CSV file created: " + fileName);
                        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error creating CSV file", e);
                }
            }
        }

        return null;
    }
}
