package com.example.ai.babel.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.CollectionPageAdapter;
import java.util.ArrayList;
import java.util.List;


public class PageActivity extends BaseActivity {

    private Toolbar mToolbar;
    private CollectionPageAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<String> pgObIdList = new ArrayList<String>();
    AVQuery<AVObject> queryBook = AVQuery.getQuery("Book");
    AVObject BookObject;
    private List<AVObject> pageListAll;
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new LoadPages().execute();
    }

    class LoadPages extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog progressDialog =new ProgressDialog(PageActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> queryPage = AVQuery.getQuery("Page");
            queryPage.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
            queryBook.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
            try {
                queryPage.whereEqualTo("bookObjectId", queryBook.get(getIntent().getStringExtra("bookObjectId")));
            } catch (AVException e) {
                e.printStackTrace();
            }
            queryPage.orderByAscending("createdAt");
            try {
                pageListAll=queryPage.find();
                for (int i = 0; i < pageListAll.size(); i++) {
                    pgObIdList.add(pageListAll.get(i).getObjectId());
                }
            } catch (AVException e) {
                e.printStackTrace();
                Toast.makeText(PageActivity.this, "连接超时:错误代码:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            try {
                BookObject = queryBook.get(getIntent().getStringExtra("bookObjectId"));
            } catch (AVException e) {
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
            mDemoCollectionPagerAdapter = new CollectionPageAdapter(getSupportFragmentManager());
            mDemoCollectionPagerAdapter.setPageList(pageListAll);
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mDemoCollectionPagerAdapter);
            mViewPager.setCurrentItem(BookObject.getInt("pageIndex"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        intiView();
    }

    private void intiView() {
        mToolbar = getActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        BookObject.put("pageIndex", mViewPager.getCurrentItem());
        BookObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                return;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_page, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search_into);
        searchItem.setIcon(android.support.v7.appcompat.R.drawable.abc_ic_search_api_mtrl_alpha);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.delete_this_page:
                DeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PageActivity.this);
        builder.setMessage("确定要删除此页面");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageListAll.get(mViewPager.getCurrentItem()).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null && pageListAll.get(mViewPager.getCurrentItem())!=null ) {
                                    Toast.makeText(PageActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    new LoadPages().execute();
                                }
                            }
                        });
                    }
                }, 1);
            }
        });
        builder.create().show();
    }

}
