package com.example.ai.babel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.example.ai.babel.ui.fragment.DemoObjectFragment;


import java.util.List;


/**
 * Created by ai on 15-5-28.
 */
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {


    private List<AVObject> pageList;

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
    public DemoCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DemoObjectFragment(pageList.get(i));
        return fragment;
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return pageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }

    public void setPageList(List<AVObject> pageList) {
        this.pageList = pageList;
    }


}
