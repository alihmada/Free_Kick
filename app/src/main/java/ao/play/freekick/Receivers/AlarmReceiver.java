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
            SharedPreferences sharedPreferences = getDecryptedSharedPreferences(context);

            Intent serviceIntent = new Intent(context, VibrationNotificationService.class);
            serviceIntent.putExtra(Common.NOTIFICATION_TITLE, intent.getStringExtra(Common.NOTIFICATION_TITLE));
            serviceIntent.putExtra(Common.NOTIFICATION_CONTENT, intent.getStringExtra(Common.NOTIFICATION_CONTENT));
            context.startForegroundService(serviceIntent);

            int position = intent.getIntExtra(Common.THREAD_POSITION, -1);
            removeCallbacks(position);

            String revenue = sharedPreferences.getString(getSharedPreferenceKey(position), "");
            RevenueDeviceData revenueDeviceData = gson.fromJson(revenue, RevenueDeviceData.class);
            Firebase.save(intent.getStringExtra(Common.DEVICE_NUMBER), revenueDeviceData, context);

            sharedPreferences.edit().remove(Common.RANDOM_KEY).apply();
            Device device = gson.fromJson(sharedPreferences.getString(getSharedPreferenceKey(intent.getStringExtra(Common.SHARED_PREFERENCES_KEY)), ""), Device.class);
            device.setRunning(false);
            sharedPreferences.edit().putString(getNewSharedPreferenceKey(intent.getStringExtra(Common.SHARED_PREFERENCES_KEY)), gson.toJson(device)).remove(intent.getStringExtra(Common.SHARED_PREFERENCES_KEY)).apply();
        }).start();
    }

    private SharedPreferences getDecryptedSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = context.getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }
        return sharedPreferences;
    }

    private void removeCallbacks(int position) {
        try {
            Objects.requireNonNull(HomeAdapter.integerHandlerHashMap.get(position))
                    .removeCallbacks(HomeAdapter.integerRunnableHashMap.get(position));
        } catch (Exception ignored) {
        }
    }

    private String getSharedPreferenceKey(int position) {
        return String.format("d%s", position);
    }

    private String getSharedPreferenceKey(String position) {
        return String.format("d%s", position);
    }

    private String getNewSharedPreferenceKey(String oldKey) {
        return String.format("h%s", oldKey);
    }
}
