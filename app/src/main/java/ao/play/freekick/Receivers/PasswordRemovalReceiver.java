package ao.play.freekick.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ao.play.freekick.Services.PasswordRemovalService;

public class PasswordRemovalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent password = new Intent(context, PasswordRemovalService.class);
        context.startService(password);
    }
}
