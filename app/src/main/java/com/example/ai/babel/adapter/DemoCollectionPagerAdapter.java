package com.example.ai.babel.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.example.ai.babel.ui.fragment.DemoObjectFragment;


/**
 * Created by ai on 15-5-28.
 */
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    AVQuery<AVObject> query = AVQuery.getQuery("Page");
    private AVUser currentUser = AVUser.getCurrentUser();
    private int PageCount;

    public DemoCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        for (int j = 0; j < PageCount ; j++) {

        }
        query.whereEqualTo("userObjectId", currentUser);
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return PageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

    public void setPageCount(int pageCount) {
        PageCount = pageCount;
    }
}
