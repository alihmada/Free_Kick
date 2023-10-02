package ao.play.freekick.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ao.play.freekick.Classes.Notifications;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.R;

public class VibrationNotificationService extends Service {

    private Vibrator vibrator;
    private String title;
    private String content;

    @Override
    public void onCreate() {
        super.onCreate();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        title = intent.getStringExtra(Common.NOTIFICATION_TITLE);
        content = intent.getStringExtra(Common.NOTIFICATION_CONTENT);

        new Thread(() -> {
            long[] pattern = {250, 1000, 250, 1000, 250, 1000, 250, 1000, 250, 1000, 250, 1000, 250, 1000, 250, 1000};
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
        }).start();

        startForeground(-1, createNotification(title, content));
        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }

    private Notification createNotification(String title, String content) {
        return new NotificationCompat
                .Builder(this, Notifications.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSmallIcon(R.drawable.notification_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_logo))
                .setContentIntent(Notifications.pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setAutoCancel(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        new Thread(() -> {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(Notifications.id, createNotification(title, content));
            Notifications.id++;
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
