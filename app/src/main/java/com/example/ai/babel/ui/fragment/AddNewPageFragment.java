package com.example.ai.babel.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.PageActivity;

public class AddNewPageFragment extends Fragment {
    private Button btnDddNewBook;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_add_new_book, container, false);
        btnDddNewBook= (Button) rootView.findViewById(R.id.btn_add_new_book);
        btnDddNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject newPage = new AVObject("Page");
                newPage.put("bookObjectId", getActivity().getIntent().getStringExtra("objectId"));
                newPage.put("updateNow", "updateNow");
                newPage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            DeleteDialog();
                        } else {
                            Log.e("LeanCloud", "Save failed.");
                        }
                    }
                });

            }
        });
        return rootView;
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定建立？");
        builder.setTitle("新建新页面");
        builder.setPositiveButton("建立", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                    startActivity(new Intent(getActivity(), PageActivity.class));
                    }
                }, 1);
            }
        });
        builder.create().show();
    }
}
