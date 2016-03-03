package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.ncfrcteams.frcscoutinghub2016.R;

public class TEST_HubSetupActivity extends AppCompatActivity {
    private EditText hubNameField;
    private EditText hubPasscodeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_hub_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hubNameField = (EditText) findViewById(R.id.hubName);
        hubPasscodeField = (EditText) findViewById(R.id.hubPasscode);

        setTitle("Hub Setup");
    }

    public void startHubInfoPage(View view) {
        Intent intent = new Intent(this, HubInfoPage.class);
        String hubName = hubNameField.getText().toString();
        String hubPasscode = hubPasscodeField.getText().toString();
        Log.d("HubSetup", "hubNameField " + hubName);
        Log.d("HubSetup", "hubPasscode " + hubName);
        intent.putExtra(HubInfoPage.HUB_NAME, hubName);
        intent.putExtra(HubInfoPage.HUB_PASSCODE, hubPasscode);
        startActivity(intent);
    }
}
