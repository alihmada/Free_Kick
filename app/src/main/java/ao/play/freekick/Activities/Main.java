package ao.play.freekick.Activities;

import static ao.play.freekick.Classes.Common.TIME_INTERVAL;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Dialogs.NotificationAlert;
import ao.play.freekick.Dialogs.Password;
import ao.play.freekick.Fragments.Account;
import ao.play.freekick.Fragments.Controllers;
import ao.play.freekick.Fragments.Debts;
import ao.play.freekick.Fragments.Home;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewListener;
import ao.play.freekick.Models.User;
import ao.play.freekick.R;

public class Main extends AppCompatActivity implements ViewListener {
    private static final int NOTIFICATION_SETTINGS_REQUEST_CODE = 1;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView navigationView;
    private Controllers controllers;
    private Vibrator vibrator;
    private long backPressed;
    private Account account;
    private Bundle bundle;
    private Debts debts;
    private Gson gson;
    private Home home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNotificationPermission();
        setCustomActionBar();
        itemsDeclaration();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
        }

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(home);
                return true;
            } else if (item.getItemId() == R.id.controllers) {
                replaceFragment(controllers);
                return true;
            } else if (item.getItemId() == R.id.debt) {
                replaceFragment(debts);
                return true;
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(account);
                return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void setCustomActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
    }

    private void itemsDeclaration() {
        navigationView = findViewById(R.id.navigation);
        home = new Home();
        debts = new Debts();
        bundle = new Bundle();
        account = new Account();
        controllers = new Controllers();
        Loading.progressDialogConstructor(this);

        findViewById(R.id.tool_bar_title).setOnLongClickListener(v -> {
            if (getUserFromSharedPreferences().isAdmin()) {
                if (!sharedPreferences.getBoolean(Common.REMEMBER_ME, false)) {
                    if (Internet.isConnected(this)) {
                        openPasswordDialog();
                    }
                } else startRevenue();
            }
            return false;
        });

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        try {
            sharedPreferences = getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        gson = new Gson();
    }

    private User getUserFromSharedPreferences() {
        String userDataJson = sharedPreferences.getString(Common.USER_DATA, "");
        return gson.fromJson(userDataJson, User.class);
    }

    private void openPasswordDialog() {
        Password password = new Password((passcode, isRememberMe) -> {
            Loading.showProgressDialog();
            Firebase.getRoot(this).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (Ciphering.encrypt(passcode).equals(Objects.requireNonNull(snapshot.getValue(String.class)))) {
                            startRevenue();
                            sharedPreferences.edit().putBoolean(Common.REMEMBER_ME, isRememberMe).apply();
                        } else {
                            Toast.makeText(Main.this, "😒", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ignored) {
                    }
                    Loading.dismissProgressDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        password.show(getSupportFragmentManager(), "password_dialog");
    }

    private void startRevenue() {
        Intent revenue = new Intent(Main.this, Revenue.class);
        bundle.putString(Common.TITLE, getString(R.string.years));
        bundle.putBoolean(Common.YEAR, true);
        revenue.putExtras(bundle);
        startActivity(revenue);
    }

    private void checkNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationAlert dialog = new NotificationAlert(() -> {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivityForResult(intent, NOTIFICATION_SETTINGS_REQUEST_CODE);
            });
            dialog.show(getSupportFragmentManager(), "");
            dialog.setCancelable(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == NOTIFICATION_SETTINGS_REQUEST_CODE) {
            if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        vibration(0);
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, getString(R.string.tap_again), Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    public void languageHandler(Device device, int position) {
        if (device != null) {
            String[] headers = getResources().getStringArray(R.array.headers);

            String startingTime = device.getStartingTime();
            String endingTime = device.getEndingTime();

            if ((startingTime.contains("م") || startingTime.contains("ص")) && Locale.getDefault().getLanguage().equals("en")) {
                device.setHeader(headers[position]);
                device.setStartingTime(startingTime.replace("م", "PM").replace("ص", "AM"));
                device.setEndingTime(endingTime.replace("م", "PM").replace("ص", "AM"));
            } else if ((startingTime.contains("PM") || startingTime.contains("AM")) && Locale.getDefault().getLanguage().equals("ar")) {
                device.setHeader(headers[position]);
                device.setStartingTime(startingTime.replace("PM", "م").replace("AM", "ص"));
                device.setEndingTime(endingTime.replace("PM", "م").replace("AM", "ص"));
            }

            try {
                Double.parseDouble(device.getHeader());
            } catch (Exception e) {
                device.setHeader(null);
            }
        }
    }

    @Override
    public void popTimePicker(EditText textInputEditText) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(LocalTime.now().getHour()).setMinute(LocalTime.now().getMinute()).build();

        timePicker.show(getSupportFragmentManager(), "timePicker");

        timePicker.addOnPositiveButtonClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            textInputEditText.setText(DateAndTime.timeFormatter(timePicker.getHour(), timePicker.getMinute()));
        });
    }

    @Override
    public void vibration(int effect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effect));
        }
    }
}