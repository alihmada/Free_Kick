package ao.play.freekick.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import ao.play.freekick.Models.Common;

public class PasswordRemovalService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a Handler to post a Runnable to run the password removal code every week
        Handler handler = new Handler();
        Runnable runnable = () -> {
            // Code to remove the user's password from shared preferences
            SharedPreferences preferences = getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(Common.USER_PASSWORD);
            editor.apply();
        };

        // Post the Runnable to run for the first time in a week
        handler.postDelayed(runnable, TimeUnit.DAYS.toMillis(7));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}