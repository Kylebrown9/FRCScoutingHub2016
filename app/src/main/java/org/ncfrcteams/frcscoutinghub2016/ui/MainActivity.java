package org.ncfrcteams.frcscoutinghub2016.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.ui.qr.MultiQRFragment;

public class MainActivity extends AppCompatActivity implements PasscodeDialog.PasscodeSelectionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] titles = {"1/6","2/6","3/6","4/6","5/6","6/6"};
        String[] content = {"content1","content2","content3","content4","content5","content6",};

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentArea, MultiQRFragment.newInstance(content,titles));
        fragmentTransaction.commit();

//        PasscodeDialog passcodeDialog = new PasscodeDialog(this,this,"");
//        passcodeDialog.show();
    }

    @Override
    public void onPasscodeSelect(String passcode) {
        Toast.makeText(this,passcode,Toast.LENGTH_LONG).show();
    }
}