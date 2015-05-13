package com.example.ai.babel.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.ai.babel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyService extends Service {

    ArrayList<HashMap<String, Object>> drawListItem = new ArrayList<HashMap<String,Object>>();


    HashMap<String, Object> allDrawNavTag = new HashMap<String, Object>();

    public MyService() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Post");
        query.whereEqualTo("objec", "steve");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List <AVObject> avObjects, AVException e) {
                if (e == null) {
                    Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
                } else {
                    Log.d("失败", "查询错误: " + e.getMessage());
                }
            }
        });
    }

    public void setDrawListItem(ArrayList<HashMap<String, Object>> drawListItem) {
        this.drawListItem = drawListItem;
        allDrawNavTag.put("ItemImage", R.drawable.ic_tick);//加入图片
        allDrawNavTag.put("ItemTitle", "所有日记");
        drawListItem.add(allDrawNavTag);
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
