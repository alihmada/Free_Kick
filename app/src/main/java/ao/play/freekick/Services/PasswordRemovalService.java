package ao.play.freekick.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Models.Common;

public class PasswordRemovalService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences preferences = null;

        try {
            preferences = getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        assert preferences != null;

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Common.USER_PASSWORD);
        editor.apply();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    } // End of onBind()
} // End of PasswordRemovalService()
