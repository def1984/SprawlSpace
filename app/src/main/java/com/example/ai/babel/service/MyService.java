package com.example.ai.babel.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.avos.avoscloud.AVUser;


public class MyService extends Service {

    private int PageCount;
    final AVUser currentUser = AVUser.getCurrentUser();
    @Override
    public void onCreate() {
        super.onCreate();

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int getPageCount() {
        return PageCount;
    }
}
