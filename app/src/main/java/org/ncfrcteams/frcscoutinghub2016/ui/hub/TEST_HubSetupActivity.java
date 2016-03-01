package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.ncfrcteams.frcscoutinghub2016.R;

public class TEST_HubSetupActivity extends AppCompatActivity {
    private TextView hubName;
    private TextView hubPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_hub_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hubName = (TextView) findViewById(R.id.hubName);
        hubPasscode = (TextView) findViewById(R.id.hubPasscode);
    }

    public void startHubInfoPage(View view) {
        Intent intent = new Intent(this, HubInfoPage.class);
        intent.putExtra(HubInfoPage.HUB_NAME,hubName.getText().toString());
        intent.putExtra(HubInfoPage.HUB_PASSCODE,hubPasscode.getText().toString());
        startActivity(intent);
    }
}
