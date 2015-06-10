package com.example.ai.babel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.example.ai.babel.ui.fragment.AddNewPageFragment;
import com.example.ai.babel.ui.fragment.PageObjectFragment;

import java.util.List;
import java.util.Stack;


/**
 * Created by ai on 15-5-28.
 */
public class CollectionPageAdapter extends FragmentStatePagerAdapter {


    private List<AVObject> pageList;
    Stack<Fragment> stack = new Stack<Fragment>();
    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }
    public CollectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (pageList.size()!=0){
            for (int j = 0; j < pageList.size(); j++) {
                stack.push(new PageObjectFragment(pageList.get(j)));
            }
            stack.push(new AddNewPageFragment());
        }else {
            stack.push(new AddNewPageFragment());
        }
        return stack.get(i);
    }

    @Override
    public int getCount() {
        // For this contrived example, we have a 100-object collection.
        return pageList.size()+1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "第" + (position + 1)+"页";
    }

    public void setPageList(List<AVObject> pageList) {
        this.pageList = pageList;
    }


}
