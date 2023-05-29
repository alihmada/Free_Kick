package ao.play.freekick.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Objects;

import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.Services.VibrationNotificationService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        new Thread(() -> {
            Gson gson = new Gson();

            Intent serviceIntent = new Intent(context, VibrationNotificationService.class);
            serviceIntent.putExtra(Common.NOTIFICATION_TITLE, intent.getStringExtra(Common.NOTIFICATION_TITLE));
            serviceIntent.putExtra(Common.NOTIFICATION_CONTENT, intent.getStringExtra(Common.NOTIFICATION_CONTENT));

            context.startForegroundService(serviceIntent);

            try {
                Objects.requireNonNull(HomeAdapter.executorServiceHashMap.get(intent.getIntExtra(Common.THREAD_POSITION, -1))).shutdown();
            } catch (Exception ignored) {
            }

            SharedPreferences sharedPreferences = null;

            try {
                sharedPreferences = context.getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
            } catch (Exception ignored) {
            }

            assert sharedPreferences != null;

            String revenue = sharedPreferences.getString(String.format("d%s", intent.getIntExtra(Common.THREAD_POSITION, -1)), "");

            RevenueDeviceData revenueDeviceData = gson.fromJson(revenue, RevenueDeviceData.class);

            Firebase.save(intent.getStringExtra(Common.DEVICE_NUMBER), revenueDeviceData, context);

            sharedPreferences.edit().remove(Common.RANDOM_KEY).apply();

            String key = intent.getStringExtra(Common.SHARED_PREFERENCES_KEY);

            Device device = gson.fromJson(sharedPreferences.getString(key, ""), Device.class);
            device.setRunning(false);

            sharedPreferences.edit().putString(String.format("h%s", key), gson.toJson(device)).remove(key).apply();
        }).start();

    } // End of onReceive()

} // End of AlarmReceiver()