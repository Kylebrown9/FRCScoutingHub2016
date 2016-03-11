package org.ncfrcteams.frcscoutinghub2016.planb;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.ncfrcteams.frcscoutinghub2016.R;

public class MainActivityB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_b);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarB);
        setSupportActionBar(toolbar);
    }

}
