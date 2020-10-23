package com.bunker.bunkermensaje;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

public class SMSListener extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsBroadcastReceiver";
    String msg, phoneNo = "";
    ArrayList<String> numerosArray = new ArrayList<String>(); //Este array se va a llenar con todos los números que la persona agregue
    @Override
    public void onReceive(Context context, Intent intent) {

        AndroidSQLiteOpenHelper admin = new AndroidSQLiteOpenHelper(context,"mensajes",null,1); // Base de datos, donde guardo los números a los que deseo mandar el mensaje
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor fila = db.rawQuery("select numeroTel from numeros ", null); // Busco todos los numeros guardados en la base de datos
        for(fila.moveToFirst();!fila.isAfterLast();fila.moveToNext()){
            numerosArray.add(fila.getString(0)); //Agrego cada número al array de números.
        }

        Log.i(TAG, "Intent Received: " +intent.getAction());
        if(intent.getAction()==SMS_RECEIVED){ // Si la acción que se produjo fue recibir un sms
            Bundle dataBundle = intent.getExtras();
            if(dataBundle!=null){
                Object[] mypdu = (Object[]) dataBundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];
                for (int i = 0; i<mypdu.length;i++){

                    String format = dataBundle.getString("format");
                    message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);

                    phoneNo = message[i].getOriginatingAddress(); //Guarda el número de teléfono
                    msg=message[i].getMessageBody(); //Guarda el mensaje
                }
                if(phoneNo.equals("")){ //Si el numero es este, realizo el reenvio del cuerpo del mensaje
                    enviarSMS(msg);
                }
            }
        }
    }

    public void enviarSMS(String msg){

            for (String num: numerosArray) { // Mando el mismo mensaje a cada número del array
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, msg, null, null);
        }

    }
}
