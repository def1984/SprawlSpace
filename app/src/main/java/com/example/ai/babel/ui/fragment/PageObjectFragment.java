package com.example.ai.babel.ui.fragment;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PageObjectFragment extends android.support.v4.app.Fragment {

    private AVObject pageObj;
    private EditText pageTitle;
    private ArrayList<View> pgViewList = new ArrayList<View>();
    private LinearLayout pageBox ;
    @Override
    public void onResume() {
        super.onResume();
        new UpDataPostList().execute();
    }

    public PageObjectFragment(AVObject pageObj) {
        this.pageObj = pageObj;
    }

    public PageObjectFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pageObj.put("title", pageTitle.getText());
        pageObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                } else {
                    Log.e("LeanCloud", "Save failed.");
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_page, container, false);
        pageTitle = (EditText) rootView.findViewById(R.id.page_title);
        pageTitle.setText(pageObj.get("title").toString());
        pageBox= (LinearLayout) rootView.findViewById(R.id.page_box);
        return rootView;
    }

    class UpDataPostList extends AsyncTask<Void, Integer, Boolean> {
        ArrayList<HashMap<String, Object>> listItemMain = new ArrayList<HashMap<String, Object>>();
        ArrayList<String> postObjIDList = new ArrayList<String>();
        String errMs = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> query = AVQuery.getQuery("Post");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.whereEqualTo("pgObjectId", pageObj);
            query.orderByDescending("createdAt");
            LayoutParams param = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, 1.0f);
            LayoutParams param2 = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT, 1.0f);
            List<AVObject> commentList = null;
            try {
                commentList = query.find();
                for (int i = 0; i < commentList.size(); i++) {
                    LinearLayout postItem = new LinearLayout(getActivity());
                    postItem.setLayoutParams(param);
                    postItem.setOrientation(LinearLayout.VERTICAL);
                    EditText editTitle = new EditText(getActivity());
                    EditText editContent = new EditText(getActivity());
                    editTitle.setLayoutParams(param2);
                    editContent.setLayoutParams(param2);
                    editTitle.setText(commentList.get(i).getString("title"));
                    editContent.setText(commentList.get(i).getString("content"));
                    postItem.addView(editTitle);
                    postItem.addView(editContent);
                    pgViewList.add(postItem);
                    postObjIDList.add(commentList.get(i).getObjectId());
                }
            } catch (AVException e) {
                e.printStackTrace();
                errMs=e.getMessage();
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (errMs == null) {
                for (int i = 0; i <pgViewList.size() ; i++) {
                    pageBox.addView(pgViewList.get(i));
                }
            }else {
                Toast.makeText(getActivity(), "查询错误,错误代码:" + errMs, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定删除？");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                   new UpDataPostList().execute();
                    }
                }, 10);
            }
        });
        builder.create().show();
    }

}
