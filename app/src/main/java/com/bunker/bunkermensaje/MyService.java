package com.bunker.bunkermensaje;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    SMSListener smsListener = new SMSListener(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            registerReceiver(smsListener, new IntentFilter(SMS_RECEIVED));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


}
