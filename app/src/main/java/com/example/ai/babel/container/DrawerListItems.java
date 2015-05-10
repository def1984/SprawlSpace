package com.example.ai.babel.container;

import com.example.ai.babel.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ai on 15-5-10.
 */
public class DrawerListItems {

    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
    HashMap<String, Object> allNotes = new HashMap<String, Object>();

    public ArrayList<HashMap<String, Object>> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<HashMap<String, Object>> listItem) {
        this.listItem = listItem;
        allNotes.put("ItemImage", R.drawable.ic_tick);//加入图片
        allNotes.put("ItemTitle", "所有笔记");
        listItem.add(allNotes);
    }
}
