package ao.play.freekick.Classes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import ao.play.freekick.Activities.Main;

public class Notifications extends Application {
    public static final String CHANNEL_ID = "free_kick";
    public static PendingIntent pendingIntent;
    public static int id = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotificationChannels();
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Main.class), PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void CreateNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID, "Free Kick", NotificationManager.IMPORTANCE_HIGH);
        channel1.setDescription("change notification sound");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
