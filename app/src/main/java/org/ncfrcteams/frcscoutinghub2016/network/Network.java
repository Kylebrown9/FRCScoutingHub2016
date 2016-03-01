package org.ncfrcteams.frcscoutinghub2016.network;

import org.ncfrcteams.frcscoutinghub2016.network.server.Hub;
import org.ncfrcteams.frcscoutinghub2016.network.server.Server;

import java.util.UUID;

/**
 * Created by Admin on 2/26/2016.
 */
public class Network {
    public static final UUID SCOUTING_HUB_UUID = UUID.fromString("4e73e0f0-dcd5-11e5-a837-0800200c9a66");   //a random UUID generated from a website
    public static final String NAME = "org.ncfrcteams.frcscoutinghub2016";

    public static Hub getHub(String name, String passcode) {
        return Server.spawn(name, passcode);
    }


}
