package com.example.ai.babel.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.AddNewPost;
import com.example.ai.babel.ui.PageActivity;


public class BookObjectFragment extends android.support.v4.app.Fragment {


    private AVObject bookObj;
    private String bookObjID=null;

    @Override
    public void onResume() {
        super.onResume();
    }


    public BookObjectFragment(AVObject pageObj) {
        this.bookObj = pageObj;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (bookObjID == null) {
            bookObjID=bookObj.getObjectId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_book, container, false);
        ((TextView) rootView.findViewById(R.id.text_title)).setText(
                bookObj.getString("title"));
        ((TextView) rootView.findViewById(R.id.text_description)).setText(
                bookObj.getString("description"));
        rootView.findViewById(R.id.one_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("objectId", bookObjID);
                intent.setClass(getActivity(), PageActivity.class);

                startActivity(intent);
                getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            }
        });
        return rootView;
    }
}
