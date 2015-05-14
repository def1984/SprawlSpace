package com.example.ai.babel.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.example.ai.babel.R;



public class DetailActivity extends BaseActivity {

    private EditText postTitle,postContent;
    String objectId;
    String postInputTitle,postInputContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        objectId=intent.getStringExtra("objectId");
        postTitle= (EditText) findViewById(R.id.post_et_title);
        postContent= (EditText) findViewById(R.id.post_et_content);
        new DetailListView().execute();

    }



    class DetailListView extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> query = AVQuery.getQuery("Post");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
            AVObject postInput;
            try {
                postInput = query.get(objectId);
                postInputTitle=postInput.getString("title");
                postInputContent=postInput.getString("content");
            } catch (AVException e) {
                // e.getMessage()
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(Boolean result) {
            postTitle.setText(postInputTitle);
            postContent.setText(postInputContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
