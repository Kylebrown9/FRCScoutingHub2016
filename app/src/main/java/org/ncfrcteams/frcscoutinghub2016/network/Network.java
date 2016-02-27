package org.ncfrcteams.frcscoutinghub2016.network;

import org.ncfrcteams.frcscoutinghub2016.network.query.HostDetails;
import org.ncfrcteams.frcscoutinghub2016.network.query.Querier;

import java.util.List;
import java.util.UUID;

/**
 * Created by Admin on 2/26/2016.
 */
public class Network {
    public static final UUID SCOUTING_HUB_UUID = UUID.fromString("4e73e0f0-dcd5-11e5-a837-0800200c9a66");   //a random UUID generated from a website
    public static final String NAME = "org.ncfrcteams.frcscoutinghub2016";
    public static enum State {IDLE,SERVER,CLIENT}
    private State state = State.IDLE;
}
