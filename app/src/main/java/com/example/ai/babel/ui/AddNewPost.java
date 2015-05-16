package com.example.ai.babel.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.ai.babel.R;

public class AddNewPost extends BaseActivity {

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
                AVObject newPost = new AVObject("Post");
                newPost.put("content", postContent.getText().toString());
                newPost.put("title", postTitle.getText().toString());
                newPost.put("userObjectId", currentUser);
                newPost.saveInBackground();
                Toast.makeText(AddNewPost.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
