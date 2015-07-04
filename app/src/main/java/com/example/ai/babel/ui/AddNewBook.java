package com.example.ai.babel.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;

public class AddNewBook extends BaseActivity {

    private EditText bookTitle, bookDescription;
    private MyFloatingActionButton saveBtn;
    private Toolbar mToolbar;
    private AVUser currentUser = AVUser.getCurrentUser();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddNewBook.this, MainActivity.class).putExtra("userCheck", true));
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        bookTitle= (EditText) findViewById(R.id.book_title);
        bookDescription= (EditText) findViewById(R.id.book_description);
        saveBtn= (MyFloatingActionButton) findViewById(R.id.btnSave);
        mToolbar = getActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("编辑封面");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewBook.this, MainActivity.class).putExtra("userCheck", true));
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookTitle.getText().toString().isEmpty()|| bookDescription.getText().toString().isEmpty()) {
                    Toast.makeText(AddNewBook.this, "标题或者描述不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    final AVObject newBook = new AVObject("Book");
                    newBook.put("title", bookTitle.getText().toString());
                    newBook.put("description", bookDescription.getText().toString());
                    newBook.put("userObjectId", currentUser);
                    currentUser.put("bookIndex",0);
                    newBook.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                AVObject page = new AVObject("Page");
                                page.put("bookObjectId", newBook);
                                page.put("title", "");
                                page.put("content", "");
                                page.saveInBackground();
                                Toast.makeText(AddNewBook.this, "保存成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddNewBook.this, MainActivity.class).putExtra("userCheck", true));
                                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                                finish();
                            } else {
                                Log.e("LeanCloud", "Save failed.");
                            }
                        }
                    });
                }
            }
        });
    }
}
