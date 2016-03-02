package org.ncfrcteams.frcscoutinghub2016.ui.hubselection;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.network.dialogue.connect.Login;
import org.ncfrcteams.frcscoutinghub2016.network.query.PasscodeDialog;
import org.ncfrcteams.frcscoutinghub2016.network.query.HostDetails;
import org.ncfrcteams.frcscoutinghub2016.network.query.QueryTask;

import java.util.ArrayList;
import java.util.Set;

public class HubListActivity extends AppCompatActivity implements HubListSelectFragment.OnHostSelectListener, QueryTask.HostDetailSetListener, PasscodeDialog.PasscodeSelectionListener {
    private boolean querying = false;
    private boolean active;
    private BluetoothDevice targetHost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        active = true;

        startListLoadingFragment();
        startQuery();
    }

    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

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
    public void onHostDetailsReady(Set<HostDetails> hostDetailsSet) {
        querying = false;
        startListSelectFragment(new ArrayList<HostDetails>(hostDetailsSet));
    }

    public void startListSelectFragment(ArrayList<HostDetails> hostDetailsList) {
        ArrayList<String> hostNameList = new ArrayList<String>();

        for(HostDetails hostDetails : hostDetailsList) {
            hostNameList.add(hostDetails.getName());
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, HubListSelectFragment.newInstance(hostNameList));
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    public void onHostSelect(HostDetails hostDetails) {
        targetHost = hostDetails.getDevice();
        PasscodeDialog passcodeDialog = new PasscodeDialog(this,this,hostDetails.getName());
        passcodeDialog.show();
    }

    @Override
    public void onPasscodeSelect(String passcode) {
        if(targetHost == null)
            return;

        Login login = new Login(targetHost,passcode);

    }
}
