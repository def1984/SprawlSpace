package com.example.ai.babel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import com.example.ai.babel.R;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ListView lv;
    private MyFloatingActionButton fabBtn;
    private Boolean isCheck = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button logoutButton;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        logOut();
        fabBtnAm();
        mDrawerListView();
    }

    ArrayList<HashMap<String, Object>> listItemMain = new ArrayList<HashMap<String,Object>>();
    private void mDrawerListView(){
        lv = (ListView) findViewById(R.id.lv);
        AVUser currentUser = AVUser.getCurrentUser();
        AVQuery<AVObject> query = AVQuery.getQuery("Post");
        AVOSCloud.setLastModifyEnabled(true);
        query.whereEqualTo("userObjectId", currentUser);
        query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> commentList, AVException e) {
                if (e == null) {
                    for (int i = 0; i < commentList.size(); i++) {
                        HashMap<String, Object> allDrawNavTag = new HashMap<String, Object>();
                        allDrawNavTag.put("ItemImage", R.drawable.ic_tick);//加入图片
                        allDrawNavTag.put("ItemTitle",commentList.get(i).getString("content"));
                        listItemMain.add(allDrawNavTag);
                    }
                } else {
                    System.out.println("无法查询");
                }

            }
        });

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItemMain,//需要绑定的数据
                R.layout.drawer_item,//每一行的布局
//动态数组中的数据源的键对应到定义布局的View中
                new String[] {"ItemImage","ItemTitle" },
                new int[] {R.id.ItemImage,R.id.ItemTitle}
        );
        lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器
    }

    private void intiView() {

        // 在这里我们获取了主题暗色，并设置了status bar的颜色
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        int color = typedValue.data;

        // 注意setStatusBarBackgroundColor方法需要你将fitsSystemWindows设置为true才会生效
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        drawerLayout.setStatusBarBackgroundColor(color);
        mToolbar = getActionBarToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

    }
    private void showAllMinFab() {
        Animation minFabSet = AnimationUtils.loadAnimation(MainActivity.this, R.anim.min_fab_anim);
        mLinearLayout = (LinearLayout) findViewById(R.id.mini_fab_content);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
              FloatingActionButton mini= (FloatingActionButton) mLinearLayout .getChildAt(i);
              mini.setVisibility(View.VISIBLE);
              mini.startAnimation(minFabSet);
        }
    }

    private void hideAllMinFab() {
        mLinearLayout = (LinearLayout) findViewById(R.id.mini_fab_content);
        Animation minFabSetRve = AnimationUtils.loadAnimation(MainActivity.this, R.anim.min_fab_anim_rev);
        for (int i = 0; i < mLinearLayout.getChildCount(); i++) {
            FloatingActionButton mini= (FloatingActionButton) mLinearLayout .getChildAt(i);
            minFabSetRve.setFillAfter(true);
            mini.startAnimation(minFabSetRve);
        }
    }


    private void fabBtnAm() {
        fabBtn = (MyFloatingActionButton) findViewById(R.id.fab);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animationSet = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_anim);
                ImageView fabImageView= (ImageView) findViewById(R.id.img_fab);
                if (!isCheck) {
                    animationSet.setFillAfter(true);
                    showAllMinFab();
                    fabImageView.startAnimation(animationSet);
                    isCheck = true;
                } else {
                    animationSet.setInterpolator(new ReverseInterpolator());
                    fabImageView.startAnimation(animationSet);
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

    private void logOut() {
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                Intent interIntent = new Intent(MainActivity.this, InterActivity.class);
                startActivity(interIntent);
                finish();
            }
        });
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_search_into:
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SearchActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
