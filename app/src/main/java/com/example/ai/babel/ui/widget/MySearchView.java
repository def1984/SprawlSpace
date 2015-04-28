package com.example.ai.babel.ui.widget;

/**
 * Created by ai on 15-4-28.
 */

import android.content.Context;

import android.graphics.drawable.Drawable;

import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.example.ai.babel.R;

public class MySearchView  extends SearchView{

    private ImageView mSearchButton;
    public MySearchView(Context context) {
        super(context);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context,
                attrs, android.support.v7.appcompat.R.styleable.SearchView, defStyleAttr, 0);
        // Keep the TintManager in case we need it later


        final LayoutInflater inflater = LayoutInflater.from(context);
        final int layoutResId = a.getResourceId(
                android.support.v7.appcompat.R.styleable.SearchView_layout, R.layout.abc_search_view);
        inflater.inflate(layoutResId, this, true);
        mSearchButton= (ImageView) findViewById(R.id.search_button);
        mSearchButton.setImageDrawable(a.getDrawable(R.drawable.ic_action_search));
    }

    @Override
    public boolean isIconfiedByDefault() {
        return super.isIconfiedByDefault();

    }
}
