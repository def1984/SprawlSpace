package com.example.ai.babel.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.AVObject;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.MainActivity;
import com.example.ai.babel.ui.fragment.BookObjectFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * Created by ai on 15-5-28.
 */
public class NavListAdapter {

     public SimpleAdapter getNavSimpleAdapter(Context context){
         ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
         HashMap<String, Object> map = new HashMap<String, Object>();
         HashMap<String, Object> map2 = new HashMap<String, Object>();

         map.put("ItemImage",R.drawable.ic_settings_black_24dp);//加入图片
         map.put("ItemTitle", "个人设置");

         map2.put("ItemImage", R.drawable.ic_polymer_black_24dp);//加入图片
         map2.put("ItemTitle", "关于巴别");
         listItem.add(map);
         listItem.add(map2);
         SimpleAdapter mSimpleAdapter = new SimpleAdapter(context,listItem,//需要绑定的数据
                 R.layout.drawer_item,//每一行的布局
//动态数组中的数据源的键对应到定义布局的View中
                 new String[] {"ItemImage","ItemTitle"  },
                 new int[] {R.id.ItemImage,R.id.ItemTitle }
         );
         return mSimpleAdapter;
     }
}
