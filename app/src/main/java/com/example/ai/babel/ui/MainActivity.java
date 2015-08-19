package com.example.ai.babel.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.avos.avoscloud.AVUser;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.NavListAdapter;
import com.example.ai.babel.ui.fragment.BooksFragment;
import com.example.ai.babel.ui.fragment.TestFragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {


    private Toolbar mToolbar;
    private CircleImageView profileImage;
    private AVUser currentUser = AVUser.getCurrentUser();

    private ActionBarDrawerToggle mDrawerToggle;
    private Button logoutButton;
    private ViewPager mViewPager ;
    private Boolean userCheck = true;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ListView navList;
    private DemoCollectionPagerAdapter  mDemoCollectionPagerAdapter;
    private TextView userName;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!userCheck) {
            AVUser.logOut();
        } else {
            return;
        }
    }

    @Override
     protected void onStart() {
        super.onStart();
        new LoadBooks().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search_view);
        searchItem.setIcon(android.support.v7.appcompat.R.drawable.abc_ic_search_api_mtrl_alpha);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search_view){
             startActivity(new Intent(MainActivity.this,SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new BooksFragment();
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new TestFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    return new TestFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return "笔记本";
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return "思维碎片";

                default:
                    // The other sections of the app are dummy placeholders.
                    return "影音";
            }
        }
    }

    private void intiView() {
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
                Toast.makeText(MainActivity.this, "登出成功", Toast.LENGTH_SHORT).show();
            }
        });

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initTabsValue();
    }




    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.GRAY);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.TRANSPARENT);
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                0, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics()));
        // 选中的文字颜色

        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }






    public class LoadBooks extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        URL picUrl = null;
        Bitmap pngBM = null;

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (currentUser.getAVFile("AvatarImage") != null) {
                    picUrl = new URL(currentUser.getAVFile("AvatarImage").getUrl());
                } else {
                    picUrl = new URL("http://ac-9lv2ouk1.clouddn.com/AscMwdlOVcH3yBgT7GYbKTfeqOa52RCRQYHIQkT6.png");
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
            profileImage = (CircleImageView) findViewById(R.id.profile_image);
            profileImage.setImageBitmap(pngBM);
            TextView userCreatedAt = (TextView) findViewById(R.id.user_ctime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String t1 = format.format(AVUser.getCurrentUser().getCreatedAt());
            userCreatedAt.setText(t1 + "创建");
            userName = (TextView) findViewById(R.id.user_name);
            userName.setText(currentUser.get("writeName").toString());
        }
    }

}
