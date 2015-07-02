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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.ui.widget.MyFloatingActionButton;

public class EditBook extends BaseActivity {

    private EditText bookTitle, bookDescription;
    private MyFloatingActionButton saveBtn;
    private String bookObjectId;
    private Toolbar mToolbar;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditBook.this, MainActivity.class).putExtra("userCheck", true));
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        mToolbar = getActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("编辑封面");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditBook.this, MainActivity.class).putExtra("userCheck", true));
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                finish();
            }
        });
        bookTitle = (EditText) findViewById(R.id.book_title);
        bookDescription = (EditText) findViewById(R.id.book_description);
        Intent intent = getIntent();
        bookObjectId = intent.getStringExtra("bookObjectId");
        AVQuery<AVObject> query = new AVQuery<>("Book");
        saveBtn = (MyFloatingActionButton) findViewById(R.id.btnSave);
        query.getInBackground(bookObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                if (e == null){
                    final AVObject bookObject =avObject;
                    bookTitle.setText(avObject.getString("title"));
                    bookDescription.setText(avObject.getString("description"));
                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bookObject.put("title", bookTitle.getText().toString());
                            bookObject.put("description", bookDescription.getText().toString());
                            Toast.makeText(EditBook.this, "保存成功", Toast.LENGTH_SHORT).show();
                            bookObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        Log.i("LeanCloud", "Save successfully.");
                                        startActivity(new Intent(EditBook.this, MainActivity.class).putExtra("userCheck", true));
                                        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                                        finish();
                                    } else {
                                        Log.e("LeanCloud", "Save failed.");
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Toast.makeText(EditBook.this,"查询失败",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
