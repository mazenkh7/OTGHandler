package mazen.otghandler;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public Button startButton;
    public Button stopButton;
    public static TextView serviceStatus;
    public CheckBox setOnBootCheckBox;
    public static SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        serviceStatus = (TextView) findViewById(R.id.fv);
        setOnBootCheckBox = (CheckBox) findViewById(R.id.checkBox);
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pref = getSharedPreferences("Preferences",MODE_PRIVATE);

        if(pref.getBoolean("StartOnBoot",false)){
            setOnBootCheckBox.setChecked(true);
        }
        setOnBootCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor prefEditor = pref.edit();
                if(setOnBootCheckBox.isChecked())
                    prefEditor.putBoolean("StartOnBoot",true);
                else
                    prefEditor.putBoolean("StartOnBoot",false);
                prefEditor.apply();

            }
        });
        final Intent service = new Intent(this,OtgService.class);
        serviceStatus.setTextColor(Color.RED);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!OtgService.isRunning){
                    OtgService.isRunning = true;
                    startService(service);
                    serviceStatus.setText("Running");
                    serviceStatus.setTextColor(Color.GREEN);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OtgService.isRunning){
                    OtgService.isRunning = false;
                    stopService(service);
                    serviceStatus.setText("Stopped");
                    serviceStatus.setTextColor(Color.RED);
//                    manager.cancel(1);
                }
            }
        });
        if(OtgService.isRunning){
            serviceStatus.setTextColor(Color.GREEN);
            serviceStatus.setText("Running");
        }
    }
}
