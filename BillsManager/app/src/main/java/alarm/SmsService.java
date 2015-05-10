package alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.sd.billsmanager.MainActivity;
import com.sd.billsmanager.R;

/**
 * Created by randeep on 5/9/15.
 */
public class SmsService extends Service {

    private SmsReceiver smsReceiver;
    private IntentFilter intentFilter;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsReceiver = new SmsReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);

    }

    private class SmsReceiver extends BroadcastReceiver {

        final SmsManager smsManager = SmsManager.getDefault();

        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();
            String phoneNumber = "";
            String message = "";
            try {
                if (bundle != null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");
                    Log.e("OBject", pdusObj.length + "..............");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        phoneNumber = sms.getDisplayOriginatingAddress();
                        message = sms.getDisplayMessageBody();
                        if(message.contains("bill") || message.contains("debit"))
                        {
                            sendNotification("Would you like to add this as a bill");
                            break;
                        }
                        Log.e("RECEIVED", phoneNumber + "...................." + message);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    private void sendNotification(String msg) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.payulogo)
                        .setContentTitle(getString(R.string.doodle_alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
