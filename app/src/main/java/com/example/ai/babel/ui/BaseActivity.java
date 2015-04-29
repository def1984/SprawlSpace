package com.example.ai.babel.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;
import com.example.ai.babel.R;


public class BaseActivity extends ActionBarActivity {
    // Primary toolbar and drawer toggle
    private Toolbar mActionBarToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVOSCloud.initialize(this, "9lv2ouk1313iornwpwlliesuouu0lioblfnbdahmseyvfls2", "mzry71qrf377cvljbb62eggmbv5j3aeqdss7j7ipmt11whsw");
        AVAnalytics.trackAppOpened(getIntent());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }


    protected Toolbar getActionBarToolbar() {
        if (mActionBarToolbar == null) {
            mActionBarToolbar = (Toolbar) findViewById(R.id.main_toolbar);
            if (mActionBarToolbar != null) {
                setSupportActionBar(mActionBarToolbar);
            }
        }
        return mActionBarToolbar;
    }
}
