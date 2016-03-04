package org.ncfrcteams.frcscoutinghub2016.ui.hubselection;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.network.connect.Login;
import org.ncfrcteams.frcscoutinghub2016.network.query.PasscodeDialog;
import org.ncfrcteams.frcscoutinghub2016.network.query.HubDetails;
import org.ncfrcteams.frcscoutinghub2016.network.query.QueryTask;

import java.util.ArrayList;
import java.util.Set;

public class HubListActivity extends AppCompatActivity implements HubListSelectFragment.OnHostSelectListener, QueryTask.HubDetailSetListener, PasscodeDialog.PasscodeSelectionListener {
    private boolean querying = false;
    private boolean isLoading = true;
    private boolean active;
    private BluetoothDevice targetHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.
        setSupportActionBar(toolbar);

        active = true;

        startListLoadingFragment();
        startQuery();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hub_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if(!isLoading) {
                    startListLoadingFragment();
                    startQuery();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }


    //**********************************************************************************************
    public void startListLoadingFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, ListLoadingFragment.newInstance());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void startQuery() {
        if(querying)
            return;

        new QueryTask().execute(this);
        querying = true;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void onHubDetailsReady(Set<HubDetails> hubDetailsSet) {
        querying = false;
        startListSelectFragment(new ArrayList<HubDetails>(hubDetailsSet));
    }

    public void startListSelectFragment(ArrayList<HubDetails> hubDetailsList) {
        ArrayList<String> hostNameList = new ArrayList<String>();

        for(HubDetails hubDetails : hubDetailsList) {
            hostNameList.add(hubDetails.getName());
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, HubListSelectFragment.newInstance(hostNameList));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        isLoading = false;
    }

    public void onHostSelect(HubDetails hubDetails) {
        targetHost = hubDetails.getDevice();
        PasscodeDialog passcodeDialog = new PasscodeDialog(this,this, hubDetails.getName());
        passcodeDialog.show();
    }

    @Override
    public void onPasscodeSelect(String passcode) {
        if(targetHost == null)
            return;

        Login login = new Login(targetHost,passcode);

    }
}
