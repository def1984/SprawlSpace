package com.example.ai.babel.ui;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.ai.babel.R;
import com.example.ai.babel.adapter.CollectionBookAdapter;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
    private CircleImageView resultView;
    private String filename;
    private String dirPath;
    private File outputImage;
    private EditText userName;
    private AVUser currentUser = AVUser.getCurrentUser();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        new resultViewEx().execute();
        Toolbar mToolbar = getActionBarToolbar();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("个人设置");
        userName = (EditText) findViewById(R.id.user_name);
        userName.setText(currentUser.get("writeName").toString());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Babel";
            File path1 = new File(dirPath);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
            }
        } else {
            return;
        }
    }


    class resultViewEx extends AsyncTask<Void, Integer, Boolean> {
        URL picUrl = null;
        Bitmap pngBM = null;
        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            AVQuery<AVObject> query = AVQuery.getQuery("Book");
            query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ONLY);
            query.whereEqualTo("userObjectId", currentUser);
            query.orderByDescending("createdAt");

            try {
                if (currentUser.getAVFile("AvatarImage") != null) {
                    picUrl = new URL(currentUser.getAVFile("AvatarImage").getUrl());
                } else {
                    picUrl = new URL("http://ac-9lv2ouk1.clouddn.com/rDWKrB4GKVhOsrhg5fqh2cdYN5bVa4FWnBG2IXkL");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                pngBM = BitmapFactory.decodeStream(picUrl.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("当前下载进度：" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            resultView = (CircleImageView) findViewById(R.id.profile_image);
            resultView.setImageBitmap(pngBM);
            resultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crop.pickImage(ProfileActivity.this);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.show();
            try {
                if (outputImage != null) {
                    if (currentUser.getAVFile("AvatarImage") != null) {
                        currentUser.getAVFile("AvatarImage").deleteInBackground();
                        AVFile file = AVFile.withAbsoluteLocalPath("AvatarImage", String.valueOf(outputImage));
                        currentUser.put("AvatarImage", file);
                        currentUser.put("writeName", userName.getText().toString());
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Toast.makeText(ProfileActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    } else {
                        AVFile file = AVFile.withAbsoluteLocalPath("AvatarImage", String.valueOf(outputImage));
                        currentUser.put("AvatarImage", file);
                        currentUser.put("writeName", userName.getText().toString());
                        currentUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Toast.makeText(ProfileActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        });
                    }
                } else {
                    currentUser.put("writeName", userName.getText().toString());
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            Toast.makeText(ProfileActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    });
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        filename = format.format(date);
        outputImage = new File(dirPath, filename + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将File对象转换为Uri并启动照相程序
        Uri destination = Uri.fromFile(outputImage);
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
