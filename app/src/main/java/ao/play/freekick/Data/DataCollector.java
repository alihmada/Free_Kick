package ao.play.freekick.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Models.Common;

public class DataCollector {
    private static String data;
    private static Device[] devices;

    public static void collect(Context context) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getDecryptedSharedPreferences(context);

        devices = new Device[Common.DEVICES_NUMBER];

        for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
            devices[i] = gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), Device.class);
        }

        data = gson.toJson(devices);
    }

    private static SharedPreferences getDecryptedSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = context.getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }
        return sharedPreferences;
    }

    public static String getData() {
        return data;
    }

    public static Device[] getDevices() {
        return devices;
    }
}
