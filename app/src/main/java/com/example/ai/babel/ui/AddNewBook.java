package com.example.ai.babel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;

public class AddNewBook extends BaseActivity {

    private EditText postTitle,postContent;
    private Button saveBtn;
    private AVUser currentUser = AVUser.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        postContent= (EditText) findViewById(R.id.post_et_content);
        postTitle= (EditText) findViewById(R.id.post_et_title);
        saveBtn= (Button) findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject newPost = new AVObject("Book");
                newPost.put("description", postContent.getText().toString());
                newPost.put("title", postTitle.getText().toString());
                newPost.put("userObjectId", currentUser);
                newPost.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(AddNewBook.this, "保存成功", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(AddNewBook.this, MainActivity.class));
                            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                        } else {
                            Log.e("LeanCloud", "Save failed.");
                        }
                    }
                });

            }
        });
    }
}
