package com.example.ai.babel.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.etsy.android.grid.StaggeredGridView;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.BookGridAdapter;
import com.example.ai.babel.ui.MainActivity;
import com.example.ai.babel.ui.PageActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by def1984 on 15-6-30.
 */
public class LoadBooks extends AsyncTask<Void, Integer, Boolean> {

    private CircleImageView profileImage;
    private AVUser currentUser = AVUser.getCurrentUser();
    private List<AVObject> bookListAll;
    private View rootView = MainActivity.getContentView();
    private Context context;

    public Context getContext() {
        return context;
    }

    public LoadBooks(Context context) {
       progressDialog = new ProgressDialog(context);
       this.context=context;
    }

    URL picUrl = null;
    Bitmap pngBM = null;
    ProgressDialog progressDialog ;

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }


    @Override
    protected Boolean doInBackground(Void... params) {
        AVQuery<AVObject> query = AVQuery.getQuery("Book");
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
        query.whereEqualTo("userObjectId", currentUser);
        query.orderByDescending("createdAt");
        try {
            bookListAll = query.find();
        } catch (AVException e) {
            Toast.makeText(getContext(), "连接超时:错误代码:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            if (currentUser.getAVFile("AvatarImage") != null) {
                picUrl = new URL(currentUser.getAVFile("AvatarImage").getUrl());
            } else {
                picUrl = new URL("http://ac-9lv2ouk1.clouddn.com/qG55U7B45Q7bL4fgoLOy1xlN4TUJpzJfXWSirhMN.jpg");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            pngBM = BitmapFactory.decodeStream(picUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setMessage("当前下载进度：" + values[0] + "%");
    }

    @Override
    protected void onPostExecute(Boolean result) {
        progressDialog.dismiss();
        StaggeredGridView gridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
        gridView.setAdapter(new BookGridAdapter(getContext(), bookListAll));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("bookObjectId", bookListAll.get(i).getObjectId());
                intent.setClass(getContext(), PageActivity.class);
                getContext().startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                AVQuery<AVObject> pageQuery = AVQuery.getQuery("Book");
                                pageQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
                                //pageQuery.whereEqualTo("bookObjectId", bookListAll.get(mViewPager.getCurrentItem()));
                                pageQuery.findInBackground(new FindCallback<AVObject>() {
                                    @Override
                                    public void done(List<AVObject> list, AVException e) {
                                        if (e == null) {
                                            for (int i = 0; i < list.size(); i++) {
                                                list.get(i).deleteInBackground();
                                            }
//                                            bookListAll.get(mViewPager.getCurrentItem()).deleteInBackground(new DeleteCallback() {
//                                                @Override
//                                                public void done(AVException e) {
//                                                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            });
                                        }
                                    }
                                });

                            }
                        }, 1);
                    }
                });
                builder.create().show();
                return false;

            }
        });

        profileImage = (CircleImageView) rootView.findViewById(R.id.profile_image);
        profileImage.setImageBitmap(pngBM);

        TextView userCreatedAt= (TextView) rootView.findViewById(R.id.user_ctime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String t1=format.format(AVUser.getCurrentUser().getCreatedAt());
        userCreatedAt.setText(t1+"创建");
    }

}
