package org.ncfrcteams.frcscoutinghub2016.network.checkup;

import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.stuff.Job;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 3/2/2016.
 */
public class SenderJob extends Job {
    public static final long PULSE_RATE = 1000;
    private Client client;
    private ObjectOutputStream outputStream;

    public static SenderJob spawn(Client client, ObjectOutputStream outputStream) {
        SenderJob senderJob = new SenderJob(client,outputStream);
        senderJob.start();
        return senderJob;
    }

    public SenderJob(Client client, ObjectOutputStream outputStream) {
        super(true);
        this.outputStream = outputStream;
        this.client = client;
    }

    @Override
    public void init() {}

    @Override
    public void periodic() {
        try {
            Thread.sleep(PULSE_RATE);
            outputStream.writeObject(client.getHeartBeatMessage());
        } catch (IOException e) {
            Log.d("Client-SenderJob", "failed to write HeartBeatMessage");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("Client-SenderJob","interrupted during sleep");
            e.printStackTrace();
        }
    }
}
