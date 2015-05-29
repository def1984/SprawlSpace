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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;

public class AddNewPost extends BaseActivity {

    private EditText postTitle, postContent;
    private Button saveBtn;
    private String pgObjectId;
    private AVObject pageObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        postContent = (EditText) findViewById(R.id.post_et_content);
        postTitle = (EditText) findViewById(R.id.post_et_title);
        Intent intent = getIntent();
        pgObjectId = intent.getStringExtra("objectId");

        AVQuery<AVObject> query = new AVQuery<AVObject>("Page");

        query.getInBackground(pgObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                pageObject =avObject;
            }
        });

        saveBtn = (Button) findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject newPost = new AVObject("Post");
                newPost.put("content", postContent.getText().toString());
                newPost.put("title", postTitle.getText().toString());
                newPost.put("pgObjectId", pageObject);
                newPost.saveInBackground();
                Toast.makeText(AddNewPost.this, "保存成功", Toast.LENGTH_SHORT).show();
                pageObject.put("updateNow", "updateNow");
                pageObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Log.i("LeanCloud", "Save successfully.");
                        } else {
                            Log.e("LeanCloud", "Save failed.");
                        }
                    }
                });
                finish();
                startActivity(new Intent(AddNewPost.this, MainActivity.class));
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
            }
        });
    }
}
