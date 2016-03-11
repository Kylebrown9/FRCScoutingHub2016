package org.ncfrcteams.frcscoutinghub2016.mms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.ncfrcteams.frcscoutinghub2016.database.Database;

/**
 * Created by Admin on 3/8/2016.
 */
public class DatabaseReceiver extends BroadcastReceiver {
    DatabaseListener databaseListener;

    public DatabaseReceiver spawn(DatabaseListener databaseListener) {
        DatabaseReceiver newReceiver = new DatabaseReceiver(databaseListener);
//        newReceiver.
        return newReceiver;
    }

    private DatabaseReceiver(DatabaseListener databaseListener) {
        this.databaseListener = databaseListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Database database = null;

        databaseListener.onDatabaseReceived(database);
    }

    public interface DatabaseListener {
        void onDatabaseReceived(Database database);
    }
}
