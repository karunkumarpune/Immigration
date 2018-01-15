package com.immigration.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;


public class SmsReceiver extends BroadcastReceiver {
    private String send_otp;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Log.d("intent", "broadcast " + intent);
        }
        final Bundle bundle = intent.getExtras();
        Log.d("bundle", "bundle " + bundle);
        try {
            if (bundle != null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    Log.d("senderNum", "senderNum " + senderNum);
                    String message = currentMessage.getDisplayMessageBody();
                    Log.d("message", "message : " + message);

                    String otp = message.replaceAll("[^0-9]", "");
                    if (otp.length()>3) {
                        send_otp=otp;
                    }
                    Log.d("receiver otp", "otp " + send_otp);
                    /*SignIn signIn = new SignIn();
                    signIn.recivedSms(send_otp);*/
                    try {
                       // if (message.contains("Shop4Hella")){
                            Log.d("opt received ",send_otp+" ");
                            intent.putExtra("otp",send_otp);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                      //  }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
