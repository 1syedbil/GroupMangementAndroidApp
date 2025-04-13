package com.example.assignment1.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.core.content.FileProvider;

import java.io.File;

public class EmailSender {

    public static void sendEmail(Context context, String email, File file) {
        if (file == null) {
            Toast.makeText(context, "No file to send", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the URI using FileProvider
        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

        // Create an intent to send the email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Exported Task List");
        intent.putExtra(Intent.EXTRA_TEXT, "Attached is the exported task list.");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);

        // Grant temporary read permission to the email app
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Start the email activity
        context.startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
