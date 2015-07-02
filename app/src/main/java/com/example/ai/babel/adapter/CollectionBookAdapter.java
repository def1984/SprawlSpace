package com.example.ai.babel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.ai.babel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionBookAdapter extends BaseAdapter {

    Context context;
    private List<AVObject> bookListAll;

    public final class ViewHolder{
        public TextView title;
        public TextView text;
    }


    private ArrayList<HashMap<String, Object>> getData(){
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<bookListAll.size();i++)
        {
            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("ItemTitle", bookListAll.get(i).get("title"));
            map.put("ItemText", bookListAll.get(i).get("description"));
            listItem.add(map);
        }
        return listItem;
    }

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

    /*构造函数*/
    public CollectionBookAdapter(Context context, List<AVObject> bookListAll) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.bookListAll=bookListAll;
    }

    @Override
    public int getCount() {
        return getData().size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*书中详细解释该方法*/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //观察convertView随ListView滚动情况
        Log.v("MyListViewBase", "getView " + position + " " + convertView);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.book_item,
                    null);
            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.title = (TextView) convertView.findViewById(R.id.text_title);
            holder.text = (TextView) convertView.findViewById(R.id.text_description);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
        holder.title.setText(this.getData().get(position).get("ItemTitle").toString());
        holder.text.setText(this.getData().get(position).get("ItemText").toString());
        return convertView;
    }
}
/*存放控件*/

