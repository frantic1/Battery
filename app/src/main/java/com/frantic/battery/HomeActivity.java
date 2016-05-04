package com.frantic.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView textBatteryLevel,progress_text;
    ImageView im;
    ProgressBar pb;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textBatteryLevel = (TextView) findViewById(R.id.info);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        progress_text = (TextView) findViewById(R.id.progress_circle_text);
        im = (ImageView) findViewById(R.id.image1);
        bt = (Button) findViewById(R.id.call);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        registerBatteryLevel();
    }

    private void registerBatteryLevel() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(battery_receiver,filter);
    }


    private BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isPresent = intent.getBooleanExtra("present", false);
            String technology = intent.getStringExtra("technology");
            int plugged = intent.getIntExtra("plugged", -1);
            int scale = intent.getIntExtra("scale", -1);
            int health = intent.getIntExtra("health", 0);
            int status = intent.getIntExtra("status", 0);
            int rawlevel = intent.getIntExtra("level", -1);
            int voltage = intent.getIntExtra("voltage", 0);
            int temperature = intent.getIntExtra("temperature", 0);
            int small_icon = intent.getIntExtra("icon-small",0);
            int level = 0;
            Bundle bundle = intent.getExtras();
            Log.i("BatteryLevel", bundle.toString());
            if (isPresent) {
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                String info = "Battery Level: " + level + "%\n";
                info += ("Technology: " + technology + "\n");
                info += ("Plugged: " + getPlugTypeString(plugged) + "\n");
                info += ("Health: " + getHealthString(health) + "\n");
                info += ("Status: " + getStatusString(status) + "\n");
                info += ("Voltage: " + voltage + "\n");
                info += ("Temperature: " + temperature + "\n");
                info += ("Scale: " + scale + "\n");
                info += ("Raw Level: " + rawlevel + "\n");
                info += ("Present: " + isPresent + "\n");
                im.setImageResource(small_icon);
                pb.setProgress(level);
                progress_text.setText(level+"%");
                setBatteryLevelText(info + "\n\n" + bundle.toString());
            } else {
                setBatteryLevelText("Battery not present!!!");
            }
        }
    };

    private String getPlugTypeString(int plugged) {
        String plugType = "Unknown";
        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                plugType = "WIRELESS";
                break;
        }
        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
            case BatteryManager.BATTERY_HEALTH_COLD:
                healthString = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                healthString = "Unknown";
                break;
        }
        return healthString;
    }

    private String getStatusString(int status) {
        String statusString = "Unknown";
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                statusString = "Not Charging";
                break;
        }
        return statusString;

    }

    private void setBatteryLevelText(String text) {
        textBatteryLevel.setText(text);
    }
}
