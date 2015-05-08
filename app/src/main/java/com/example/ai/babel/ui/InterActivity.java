package com.example.ai.babel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.avos.avoscloud.AVUser;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.fragment.LaunchpadFragment;
import com.example.ai.babel.ui.fragment.LoginFragment;
import com.example.ai.babel.ui.fragment.RegisterFragment;


public class InterActivity extends BaseActivity {

    private Toolbar mToolbar;
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            intiView();
        }
    }

    private void intiView(){
        mToolbar = getActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_login:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.action_registers:
                mViewPager.setCurrentItem(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
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
                    return new LaunchpadFragment();
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new RegisterFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    return new LoginFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

}


