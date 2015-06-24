package com.example.ai.babel.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.CollectionBookAdapter;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button logoutButton, BtnBgc;
    private CircleImageView profileImage;
    private CollectionBookAdapter mDemoCollectionBookAdapter;
    private ViewPager mViewPager;
    private MyFloatingActionButton fabBtn;
    private Boolean isCheck = false;
    private LinearLayout mLinearLayout;
    private AVUser currentUser = AVUser.getCurrentUser();
    private ArrayList<String> pgObIdList = new ArrayList<>();
    private List<AVObject> bookListAll;



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentUser.put("bookIndex", mViewPager.getCurrentItem());
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                return;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadBooks().execute();
    }

    class LoadBooks extends AsyncTask<Void, Integer, Boolean> {
        URL picUrl = null;
        Bitmap pngBM = null;
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

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
                for (int i = 0; i < bookListAll.size(); i++) {
                    pgObIdList.add(bookListAll.get(i).getObjectId());
                }
            } catch (AVException e) {
                Toast.makeText(MainActivity.this, "连接超时:错误代码:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            mDemoCollectionBookAdapter = new CollectionBookAdapter(getSupportFragmentManager());
            mDemoCollectionBookAdapter.setPageList(bookListAll);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("当前下载进度：" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            mViewPager = (ViewPager) findViewById(R.id.pager);
            if (mDemoCollectionBookAdapter != null) {
                mViewPager.setAdapter(mDemoCollectionBookAdapter);
                mViewPager.setCurrentItem(currentUser.getInt("bookIndex"));
            }
            profileImage = (CircleImageView) findViewById(R.id.profile_image);
            profileImage.setImageBitmap(pngBM);
            TextView userEmail= (TextView) findViewById(R.id.user_email);
            TextView userCreatedAt= (TextView) findViewById(R.id.user_ctime);
            userCreatedAt.setText(String.valueOf(AVUser.getCurrentUser().getCreatedAt())+"创建");
            userEmail.setText(AVUser.getCurrentUser().getEmail());
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        fabBtnAm();
        addNewBook();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search_into);
        searchItem.setIcon(android.support.v7.appcompat.R.drawable.abc_ic_search_api_mtrl_alpha);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_this_book:
                editThisBook();
                break;
            case R.id.delete_this_book:
                    DeleteDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void intiView() {
        // 在这里我们获取了主题暗色，并设置了status bar的颜色
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int color = typedValue.data;
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(color);
        mToolbar = getActionBarToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LayoutClass().execute();
            }
        });
    }

    class LayoutClass extends AsyncTask<Void, Integer, Boolean> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            AVUser.logOut();
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("当前下载进度：" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            Intent interIntent = new Intent(MainActivity.this, InitActivity.class);
            if (currentUser == null) {
                Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_SHORT).show();
            }
            startActivity(interIntent);
            finish();
        }
    }

    private void showAllMinFab() {
        Animation minFabSet = AnimationUtils.loadAnimation(MainActivity.this, R.anim.min_fab_anim);
        mLinearLayout = (LinearLayout) findViewById(R.id.mini_fab_content);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            LinearLayout mini = (LinearLayout) mLinearLayout.getChildAt(i);
            mini.setVisibility(View.VISIBLE);
            mini.startAnimation(minFabSet);
        }
    }

    private void hideAllMinFab() {
        mLinearLayout = (LinearLayout) findViewById(R.id.mini_fab_content);
        Animation minFabSetRve = AnimationUtils.loadAnimation(MainActivity.this, R.anim.min_fab_anim_rev);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            LinearLayout mini = (LinearLayout) mLinearLayout.getChildAt(i);
            minFabSetRve.setFillAfter(true);
            mini.startAnimation(minFabSetRve);
            mini.setVisibility(View.GONE);
        }
    }


    private void fabBtnAm() {
        fabBtn = (MyFloatingActionButton) findViewById(R.id.fab);
        BtnBgc = (Button) findViewById(R.id.background_cover);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animSetFab = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_anim);
                Animation animSetCbg = AnimationUtils.loadAnimation(MainActivity.this, R.anim.background_cover_anim);
                ImageView fabImageView = (ImageView) findViewById(R.id.img_fab);
                if (!isCheck) {
                    animSetFab.setFillAfter(true);
                    animSetCbg.setFillAfter(true);
                    showAllMinFab();
                    BtnBgc.setVisibility(View.VISIBLE);
                    BtnBgc.startAnimation(animSetCbg);
                    fabImageView.startAnimation(animSetFab);
                    isCheck = true;
                } else {
                    animSetFab.setInterpolator(new ReverseInterpolator());
                    animSetCbg.setInterpolator(new ReverseInterpolator());
                    fabImageView.startAnimation(animSetFab);
                    BtnBgc.startAnimation(animSetCbg);
                    BtnBgc.setVisibility(View.GONE);
                    hideAllMinFab();
                    isCheck = false;
                }
            }
        });

        BtnBgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animSetFab = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_anim);
                Animation animSetCbg = AnimationUtils.loadAnimation(MainActivity.this, R.anim.background_cover_anim);
                ImageView fabImageView = (ImageView) findViewById(R.id.img_fab);
                if (!isCheck) {
                    animSetFab.setFillAfter(true);
                    animSetCbg.setFillAfter(true);
                    showAllMinFab();
                    BtnBgc.setVisibility(View.VISIBLE);
                    BtnBgc.startAnimation(animSetCbg);
                    fabImageView.startAnimation(animSetFab);
                    isCheck = true;
                } else {
                    animSetFab.setInterpolator(new ReverseInterpolator());
                    animSetCbg.setInterpolator(new ReverseInterpolator());
                    fabImageView.startAnimation(animSetFab);
                    BtnBgc.startAnimation(animSetCbg);
                    BtnBgc.setVisibility(View.GONE);
                    hideAllMinFab();
                    isCheck = false;
                }
            }
        });
    }

    public class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float paramFloat) {
            return Math.abs(paramFloat - 1f);
        }
    }

    private void editThisBook() {
        if (mViewPager != null) {
            Intent intent = new Intent();
            intent.putExtra("bookObjectId", bookListAll.get(mViewPager.getCurrentItem()).getObjectId());
            intent.setClass(MainActivity.this, EditBook.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
        } else {
            Toast.makeText(MainActivity.this,"没有可以编辑的笔记本",Toast.LENGTH_LONG).show();
        }
    }

    private void addNewBook() {
        ImageButton addNewBook = (ImageButton) findViewById(R.id.add_new_book);
        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, AddNewBook.class));
                MainActivity.this.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            }
        });
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定要删除此笔记吗？如果删除，那么此笔记下的所有页面也将被删除");
        builder.setTitle("提示");
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        AVQuery<AVObject> pageQuery = AVQuery.getQuery("Page");
                        pageQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
                        pageQuery.whereEqualTo("bookObjectId", bookListAll.get(mViewPager.getCurrentItem()));
                        pageQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e == null) {
                                    for (int i = 0; i < list.size(); i++) {
                                        list.get(i).deleteInBackground();
                                    }
                                    bookListAll.get(mViewPager.getCurrentItem()).deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                            new LoadBooks().execute();
                                        }
                                    });
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
