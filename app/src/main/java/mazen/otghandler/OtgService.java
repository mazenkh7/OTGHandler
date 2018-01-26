package mazen.otghandler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class OtgService extends IntentService {
    public static boolean isRunning;
    public OtgService() {
        super("OTG Service");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while(isRunning) {
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                String[] cmds = new String[2];
                cmds[0] = "cd /sys/class/usb_switch/switch_ctrl/manual_ctrl/";
                cmds[1] = "cat switchctrl";
                for (String s : cmds) {
                    outputStream.writeBytes(s + "\n");
                    outputStream.flush();
                }
                BufferedReader result = new BufferedReader(new InputStreamReader(su.getInputStream()));
                int x = Integer.parseInt(result.readLine());
                if (x==8)
                    outputStream.writeBytes("echo hoston > /sys/devices/platform/ff100000.hisi_usb/plugusb\n");
                else
                    outputStream.writeBytes("echo hostoff > /sys/devices/platform/ff100000.hisi_usb/plugusb\n");

                outputStream.flush();
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(3250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
