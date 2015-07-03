package com.example.ai.babel.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.PageActivity;

public class AddNewPageFragment extends Fragment implements View.OnClickListener {
    private Button btnDddNewBook;

    AVQuery<AVObject> query = AVQuery.getQuery("Book");
    final AVObject newPage = new AVObject("Page");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_new_page, container, false);
        btnDddNewBook = (Button) rootView.findViewById(R.id.add_new_page);
        ImageButton btnDddNewBook2 = (ImageButton) rootView.findViewById(R.id.add_new_page_1);
        btnDddNewBook2.setOnClickListener(this);
        btnDddNewBook.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        DeleteDialog();
    }

    private void DeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定建立？");
        builder.setTitle("新建新页面");
        builder.setPositiveButton("建立", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.getInBackground(getActivity().getIntent().getStringExtra("bookObjectId"), new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        newPage.put("title", "");
                        newPage.put("content", "");
                        newPage.put("bookObjectId", avObject);
                        newPage.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    getActivity().finish();
                                    Intent intent = new Intent();
                                    intent.putExtra("bookObjectId", getActivity().getIntent().getStringExtra("bookObjectId"));
                                    intent.setClass(getActivity(), PageActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                                } else {
                                    Toast.makeText(getActivity(), "查询错误", Toast.LENGTH_SHORT).show();
                                    Log.e("LeanCloud", "Save failed.");
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                });

            }
        });
        builder.create().show();
    }


}
