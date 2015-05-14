package com.example.ai.babel.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.ai.babel.R;
import com.avos.avoscloud.AVUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyService extends Service {

    private String userId;

    ArrayList<HashMap<String, Object>> drawListItem = new ArrayList<HashMap<String,Object>>();

    @Override
    public void onCreate() {
        super.onCreate();
        final AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getObjectId();
        }

//        // 创建微博信息
//        AVObject post = new AVObject("Post");
//        post.put("content", "很好时节");
//
//        post.put("userObjectId", currentUser);
//
//        post.saveInBackground();

    }

    public void setDrawListItem(ArrayList<HashMap<String, Object>> drawListItem) {
        this.drawListItem = drawListItem;
    }

    public ArrayList<HashMap<String, Object>> getDrawListItem() {
        return drawListItem;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
