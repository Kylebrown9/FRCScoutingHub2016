package org.ncfrcteams.frcscoutinghub2016.communication.sms_server;

import android.telephony.SmsManager;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public class SendSms {
    public static void send(String number, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
    }
}
