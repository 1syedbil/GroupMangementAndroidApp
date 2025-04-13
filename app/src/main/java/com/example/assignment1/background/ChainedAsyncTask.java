package com.example.assignment1.background;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ChainedAsyncTask extends AsyncTask<Void, Void, Void> {

    private final Context context;

    public ChainedAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // First task: Wait for 30 seconds
        try {
            Thread.sleep(30000); // 30 seconds delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // Second task: Show a toast message
        Toast.makeText(context, "Background tasks complete", Toast.LENGTH_SHORT).show();
    }
}
