package mazen.otghandler;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import static mazen.otghandler.R.drawable.ic_usb_black_24dp;

@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingsService
        extends TileService {

    private static final String SERVICE_STATUS_FLAG = "serviceStatus";
    private static final String PREFERENCES_KEY = "com.google.android_quick_settings";

    @Override
    public void onTileAdded() {
        Log.d("QS", "Tile added");
    }

    @Override
    public void onStartListening() {
        Log.d("QS", "Start listening");
    }

    @Override
    public void onClick() {
        updateTile();
    }

    @Override
    public void onStopListening() {
        Log.d("QS", "Stop Listening");
    }

    @Override
    public void onTileRemoved() {
        Log.d("QS", "Tile removed");
    }

    private void updateTile() {

        Tile tile = this.getQsTile();
        boolean isActive = getServiceStatus();

        Icon newIcon;
        String newLabel;
        int newState;

        if (isActive) {
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                String[] cmds = new String[3];
                cmds[0] = "cd /sys/class/usb_switch/switch_ctrl/manual_ctrl/";
                cmds[1] = "cat switchctrl";
                cmds[2] = "echo hoston > /sys/devices/platform/ff100000.hisi_usb/plugusb\n";
                for (String s : cmds) {
                    outputStream.writeBytes(s + "\n");
                    outputStream.flush();
                }
                outputStream.writeBytes("exit\n");
                outputStream.flush();
            }catch (Exception e){}
            newLabel = "Host On";

            newIcon = Icon.createWithResource(getApplicationContext(),
                    ic_usb_black_24dp);

            newState = Tile.STATE_ACTIVE;

        } else {
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                String[] cmds = new String[3];
                cmds[0] = "cd /sys/class/usb_switch/switch_ctrl/manual_ctrl/";
                cmds[1] = "cat switchctrl";
                cmds[2] = "echo hostoff > /sys/devices/platform/ff100000.hisi_usb/plugusb\n";
                for (String s : cmds) {
                    outputStream.writeBytes(s + "\n");
                    outputStream.flush();
                }
                outputStream.writeBytes("exit\n");
                outputStream.flush();
            }catch (Exception e){}
            newLabel = "Host Off";

            newIcon =
                    Icon.createWithResource(getApplicationContext(),
                            ic_usb_black_24dp);

            newState = Tile.STATE_INACTIVE;
        }

        // Change the UI of the tile.
        tile.setLabel(newLabel);
        tile.setIcon(newIcon);
        tile.setState(newState);

        // Need to call updateTile for the tile to pick up changes.
        tile.updateTile();
    }

    // Access storage to see how many times the tile
    // has been tapped.
    private boolean getServiceStatus() {

        SharedPreferences prefs =
                getApplicationContext()
                        .getSharedPreferences(PREFERENCES_KEY,
                                MODE_PRIVATE);

        boolean isActive = prefs.getBoolean(SERVICE_STATUS_FLAG, false);
        isActive = !isActive;

        prefs.edit().putBoolean(SERVICE_STATUS_FLAG, isActive).apply();

        return isActive;
    }
}
