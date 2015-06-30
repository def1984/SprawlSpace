package com.example.ai.babel.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.NavListAdapter;
import com.example.ai.babel.service.LoadBooks;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;

import java.util.List;

public class MainActivity extends BaseActivity {

    public static View contentView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button logoutButton, BtnBgc;
    private MyFloatingActionButton fabBtn;
    private Boolean isCheck = false;
    private Boolean userCheck = true ;
    private LinearLayout mLinearLayout;
    private ListView navList;

    public static  View getContentView() {
        return contentView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( !userCheck ){
            AVUser.logOut();
        }else {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new LoadBooks(this).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        contentView= layoutInflater.inflate(R.layout.activity_main, null);
        setContentView(contentView);
        intiView();
        fabBtnAm();
        navList = (ListView) findViewById(R.id.nav_list);
        navList.setAdapter(new NavListAdapter().getNavSimpleAdapter(this));//为ListView绑定适配器
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent about = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(about);
                        break;
                }
            }
        });
        userCheck = getIntent().hasExtra("userCheck");
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

                break;
            case R.id.delete_this_book:

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
                AVUser.logOut();
                Intent interIntent = new Intent(MainActivity.this, InitActivity.class);
                startActivity(interIntent);
                userCheck = false;
                finish();
                Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_SHORT).show();
            }
        });
        TextView userName= (TextView) findViewById(R.id.user_name);
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
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

    private void addNewBook() {
        ImageButton addNewBook = (ImageButton) findViewById(R.id.add_new_book);
        addNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddNewBook.class));
                MainActivity.this.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            }
        });
    }


}
