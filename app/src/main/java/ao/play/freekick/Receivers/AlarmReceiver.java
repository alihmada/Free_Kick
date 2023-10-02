package ao.play.freekick.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Classes.Calculations;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Models.Temporal;
import ao.play.freekick.Services.VibrationNotificationService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            try {
                FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                Gson gson = new Gson();

                startVibrationNotificationService(context, intent);

                int position = intent.getIntExtra(Common.ITEM_POSITION, -1);
                removeCallbacks(position);

                SharedPreferences sharedPreferences = getDecryptedSharedPreferences(context);
                if (sharedPreferences != null) {
                    String revenueKey = getSharedPreferenceKey(position);

                    String revenue = sharedPreferences.getString(revenueKey, "");
                    Temporal temporal = parseRevenueData(revenue, crashlytics, gson);

                    if (temporal != null) {
                        saveRevenueData(String.valueOf(Calculations.sum(position, 1)), temporal, context);
                    }

                    transferDeviceDataToHistory(sharedPreferences, gson, String.valueOf(position));
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }).start();
    }

    private SharedPreferences getDecryptedSharedPreferences(Context context) {
        try {
            return context.getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return null;
    }

    private void startVibrationNotificationService(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, VibrationNotificationService.class);
        serviceIntent.putExtra(Common.NOTIFICATION_TITLE, intent.getStringExtra(Common.NOTIFICATION_TITLE));
        serviceIntent.putExtra(Common.NOTIFICATION_CONTENT, intent.getStringExtra(Common.NOTIFICATION_CONTENT));
        context.startForegroundService(serviceIntent);
    }

    private void removeCallbacks(int position) {
        try {
            Handler handler = HomeAdapter.integerHandlerHashMap.get(position);
            Runnable runnable = HomeAdapter.integerRunnableHashMap.get(position);
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private String getSharedPreferenceKey(int position) {
        return Common.REVENUE.concat(String.valueOf(position));
    }

    private Temporal parseRevenueData(String revenue, FirebaseCrashlytics crashlytics, Gson gson) {
        Temporal temporal = null;
        try {
            temporal = gson.fromJson(revenue, Temporal.class);
        } catch (JsonSyntaxException e) {
            crashlytics.recordException(e);
        }
        return temporal;
    }

    private void saveRevenueData(String deviceNumber, Temporal temporal, Context context) {
        Firebase.save(deviceNumber, temporal, context);
    }

    private void transferDeviceDataToHistory(SharedPreferences sharedPreferences, Gson gson, String sharedPrefsKey) {
        String deviceJson = sharedPreferences.getString(sharedPrefsKey, "");
        Device device = parseDeviceData(deviceJson, gson, FirebaseCrashlytics.getInstance());

        if (device != null) {
            device.setRunning(false);

            String preferenceHistoryKey = getSharedPreferenceHistoryKey(sharedPrefsKey);
            sharedPreferences.edit().putString(preferenceHistoryKey, gson.toJson(device)).apply();
        }
        sharedPreferences.edit().remove(sharedPrefsKey).apply();
    }

    private Device parseDeviceData(String deviceJson, Gson gson, FirebaseCrashlytics crashlytics) {
        Device device = null;
        try {
            device = gson.fromJson(deviceJson, Device.class);
        } catch (JsonSyntaxException e) {
            crashlytics.recordException(e);
        }
        return device;
    }

    private String getSharedPreferenceHistoryKey(String position) {
        return Common.HISTORY.concat(position);
    }
}
