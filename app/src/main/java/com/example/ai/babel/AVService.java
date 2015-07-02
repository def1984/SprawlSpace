package com.example.ai.babel;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;


/**
 * Created by lzw on 14-9-11.
 */
public class AVService extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,
                Config.APP_ID, Config.APP_KEY);
    }

    public static void signUp(String password, String email, String writeName ,SignUpCallback signUpCallback) {
        AVUser user = new AVUser();
        user.setPassword(password);
        user.setEmail(email);
        user.setUsername(email);
        user.put("writeName",writeName);
        user.signUpInBackground(signUpCallback);
    }

}