package mazen.otghandler;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences("Preferences", MODE_PRIVATE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (pref.getBoolean("StartOnBoot",false)) {
                Intent service = new Intent(context, OtgService.class);
                context.startService(service);
                OtgService.isRunning = true;
            }
        }
    }
}
