package com.example.ai.babel.ui;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.CollectionBookAdapter;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {

    private Toolbar mToolbar;
    private MyFloatingActionButton fabBtn;
    private Boolean isCheck = false;
    private ActionBarDrawerToggle mDrawerToggle;
    private Button logoutButton;
    private LinearLayout mLinearLayout;
    private CircleImageView profileImage;
    private CollectionBookAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;
    private AVUser currentUser = AVUser.getCurrentUser();
    private ArrayList<String> pgObIdList = new ArrayList<String>();

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new DownloadPages().execute();
    }

    class DownloadPages extends AsyncTask<Void, Integer, Boolean> {
        ProgressDialog progressDialog =new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        private List<AVObject> bookListAll;
        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> query = AVQuery.getQuery("Book");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
            query.whereEqualTo("userObjectId", currentUser);
            query.orderByDescending("updatedAt");
            try {
                bookListAll=query.find();
                for (int i = 0; i < bookListAll.size(); i++) {
                    pgObIdList.add(bookListAll.get(i).getObjectId());
                }
            } catch (AVException e) {
                Toast.makeText(MainActivity.this, "连接超时:错误代码:"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
            mDemoCollectionPagerAdapter = new CollectionBookAdapter(getSupportFragmentManager());
            mDemoCollectionPagerAdapter.setPageList(bookListAll);
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        logOut();


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
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.add_new_page:
                startActivity(new Intent(this, AddNewBook.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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



    private void logOut() {
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                Intent interIntent = new Intent(MainActivity.this, InitActivity.class);
                startActivity(interIntent);
                finish();
            }
        });
    }


}
