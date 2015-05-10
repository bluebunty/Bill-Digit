package alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by randeep on 5/9/15.
 */
public class SmsReceiver extends BroadcastReceiver {

    final SmsManager smsManager = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, SmsService.class);
        final Bundle bundle = intent.getExtras();
        String phoneNumber = "";
        String message = "";
        try {
            if (bundle != null){
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.e("OBject", pdusObj.length + "..............");
                for (int i=0; i < pdusObj.length; i++){
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    phoneNumber = sms.getDisplayOriginatingAddress();
                    message = sms.getDisplayMessageBody();


                }

                intentService.putExtra("NUMBER",phoneNumber);
                intentService.putExtra("MESSAGE",message);
                context.startService(intentService);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
