package com.example.assignment1.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TaskWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TaskCountRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
