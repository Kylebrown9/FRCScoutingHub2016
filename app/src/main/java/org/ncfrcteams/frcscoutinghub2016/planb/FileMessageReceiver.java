package org.ncfrcteams.frcscoutinghub2016.planb;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.net.URI;

/**
 * Created by Kyle Brown on 3/9/2016.
 */
public class FileMessageReceiver {
    public static final String HEADER = "ASD";

    private Context context;
    private String tag;
    private FileMessageReceiver fileMessageReceiver;

    private DownloadManager dMgr;
    private BroadcastReceiver fileReceiver;
    private BroadcastReceiver notificationClickedReceiver;

    public FileMessageReceiver(Context context, String tag, FileMessageReceiver fileMessageReceiver) {
        this.context = context;
        this.tag = tag;
        this.fileMessageReceiver = fileMessageReceiver;

        dMgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        fileReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                onReceiveFile(context,i);
            }
        };

        notificationClickedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {}
        };

        context.registerReceiver(fileReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        context.registerReceiver(notificationClickedReceiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
    }

    public void onReceiveFile(Context context, Intent i) {
        long fileID = i.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
        if(validDownload(fileID)) {
            Uri fileURI = dMgr.getUriForDownloadedFile(fileID);
            File fileItself = new File(fileURI.getPath());
            String[] fileNameParts = fileItself.getName().split("_");
            if(fileNameParts.length != 3) {
                return;
            }
            dMgr.remove(fileID);
        }
    }

    private boolean validDownload(long downloadId) {
        Log.d("Downloaded File Check","Checking download status for id: " + downloadId);

        Cursor c= dMgr.query(new DownloadManager.Query().setFilterById(downloadId));

        if(c.moveToFirst()){
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

            if(status == DownloadManager.STATUS_SUCCESSFUL){
                return true; //Download is valid, celebrate
            }else{
                int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
                Log.d("Downloaded File Check", "Download not correct, status [" + status + "] reason [" + reason + "]");
                return false;
            }
        }
        return false;
    }

    public void stop() {
        try {
            context.unregisterReceiver(fileReceiver);
            context.unregisterReceiver(notificationClickedReceiver);
        } catch (IllegalArgumentException e) {
            Log.d("MainActivity", "Failed to close BroadcastReceiver");
        }
    }

    public interface FileMessageListener {
        void onMessageReady(String[] message);
    }
}
