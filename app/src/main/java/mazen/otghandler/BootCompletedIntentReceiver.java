package mazen.otghandler;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder ogn = new NotificationCompat.Builder(context);
        SharedPreferences pref = context.getSharedPreferences("Preferences", MODE_PRIVATE);
        boolean start = pref.getBoolean("StartOnBoot",false);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (start) {
                Intent service = new Intent(context, OtgService.class);
                context.startService(service);
                OtgService.isRunning = true;
//                ogn.setContentTitle("HAHAHAHAHAHA")
//                        .setContentText("HAHAHAHAHAHA "+start)
//                        .setSmallIcon(R.drawable.ic_usb_black_24dp)
//                        .setOngoing(true);
//                manager.notify(1,ogn.build());
            }
        }
    }
}
