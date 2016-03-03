package org.ncfrcteams.frcscoutinghub2016.network.checkup;

import org.ncfrcteams.frcscoutinghub2016.network.Job;

import java.io.ObjectInputStream;

/**
 * Created by Admin on 3/2/2016.
 */
public class ReceiverJob extends Job {
    private Client client;
    private ObjectInputStream inputStream;

    public static ReceiverJob spawn(Client client, ObjectInputStream inputStream) {
        ReceiverJob receiverJob = new ReceiverJob(client, inputStream);
        receiverJob.start();
        return receiverJob;
    }

    private ReceiverJob(Client client, ObjectInputStream inputStream) {
        super(true);
        this.client = client;
        this.inputStream = inputStream;
    }

    public void init() {}

    public void periodic() {

    }
}
