package ao.play.freekick.Classes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import ao.play.freekick.Activities.Home;

public class Notifications extends Application {
    public static final String CHANNEL_ID = "free_kick";
    public static PendingIntent pendingIntent;
    public static int id = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotificationChannels();
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Home.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void CreateNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, "Free Kick", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("change notification sound");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
