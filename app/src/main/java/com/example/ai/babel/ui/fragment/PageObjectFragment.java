package com.example.ai.babel.ui.fragment;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;

@SuppressLint("ValidFragment")
public class PageObjectFragment extends android.support.v4.app.Fragment {

    private AVObject pageObj;
    private EditText pageTitle,pageContent;

    @Override
    public void onResume() {
        super.onResume();
    }

    public PageObjectFragment() {

    }

    public PageObjectFragment(AVObject pageObj) {
        this.pageObj = pageObj;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        pageObj.put("title", pageTitle.getText());
        pageObj.put("content", pageContent.getText());
        pageObj.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                } else {
                    Log.e("LeanCloud", "Save failed.");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_page, container, false);
        pageTitle = (EditText) rootView.findViewById(R.id.page_title);
        Typeface face = Typeface.createFromAsset (getActivity().getAssets() , "fonts/minijbf.TTF" );
        pageTitle.setText(pageObj.get("title").toString());
        pageContent = (EditText) rootView.findViewById(R.id.page_content);
        pageContent.setText(pageObj.get("content").toString());
        pageTitle.setTypeface(face);
        pageContent.setTypeface(face);
        return rootView;
    }


}
