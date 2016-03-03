package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.network.Network;
import org.ncfrcteams.frcscoutinghub2016.network.server.ConnectionListenerJob;
import org.ncfrcteams.frcscoutinghub2016.network.server.Hub;

public class HubInfoPage extends AppCompatActivity {
    public static final String HUB_NAME = "HubInfoPage.hubName";
    public static final String HUB_PASSCODE = "HubInfoPage.passcode";
    private Hub hub;
    private TextView numConnectedOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_hub_info_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String hubName = intent.getStringExtra(HUB_NAME);
        String passcode = intent.getStringExtra(HUB_PASSCODE);

        hub = Network.getHub(hubName, passcode);
        numConnectedOutput = (TextView) findViewById(R.id.numConnectedOutput);
    }

    public void updateNumConnectedOutput(View view) {
//        String a = Integer.toString(hub.getNumConnected());  //set text method requires explicit string type
        String a = Integer.toString(ConnectionListenerJob.NUMACCEPTS);
        numConnectedOutput.setText(a);
    }

    public void onDestroy() {
        super.onDestroy();
        hub.kill();
    }
}
