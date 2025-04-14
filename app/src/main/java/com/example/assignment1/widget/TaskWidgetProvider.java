package com.example.assignment1.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.assignment1.R;

public class TaskWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Force immediate update
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_task_list);

            // Set up the intent
            Intent intent = new Intent(context, TaskWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // Bind the adapter
            views.setRemoteAdapter(R.id.widget_task_list, intent);

            // Set empty view
            views.setEmptyView(R.id.widget_task_list, R.id.empty_view);

            // Update immediately
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_task_list);
        }
    }
}
