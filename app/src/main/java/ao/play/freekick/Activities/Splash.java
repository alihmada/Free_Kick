package ao.play.freekick.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hideActionBar();
        setFullScreen();
        redirectToAppropriateScreenWithDelay();
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void redirectToAppropriateScreenWithDelay() {
        new Handler().postDelayed(this::redirectToAppropriateScreen, SPLASH_DELAY_MS);
    }

    private void redirectToAppropriateScreen() {
        SharedPreferences sharedPreferences = getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String user = sharedPreferences.getString(Common.USER, "");

        if (!user.isEmpty()) {
            redirectToMainScreen();
            setFirebaseKeepSynced();
        } else {
            redirectToLoginScreen();
        }
    }

    private void redirectToMainScreen() {
        getRoot();

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void getRoot() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            String encryptedShopId = sharedPreferences.getString(Common.SHOP_ID, "");
            Common.ROOT = EncryptionAndDecryption.decrypt(encryptedShopId);
        } catch (Exception ignored) {
            // Handle decryption exception if necessary
        }
    }

    private void setFirebaseKeepSynced() {
        Firebase.getRoot().keepSynced(true);
    }
}
