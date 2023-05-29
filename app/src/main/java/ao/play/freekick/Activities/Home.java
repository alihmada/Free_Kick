package ao.play.freekick.Activities;

import static ao.play.freekick.Models.Common.TIME_INTERVAL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;

import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.DataCollector;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Dialogs.Password;
import ao.play.freekick.Dialogs.Qr;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewListener;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;
import ao.play.freekick.Services.PasswordRemovalService;

public class Home extends AppCompatActivity implements ViewListener {

    SharedPreferences sharedPreferences;
    Gson gson;
    RecyclerView recyclerView;
    Vibrator vibrator;
    FingerprintManager fingerprintManager;
    long BackPressed;
    ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String code = null;
            try {
                code = EncryptionAndDecryption.decrypt(result.getContents());
            } catch (Exception ignored) {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }

            if (code != null) {
                if (code.matches("controller\\d+")) {

                    Intent intent = new Intent(Home.this, Controllers.class);
                    intent.putExtra(Common.CODE, code);

                    startActivity(intent);

                } else {
                    List<Device> devices = Arrays.asList(gson.fromJson(code, Device[].class));

                    for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                        languageHandler(devices.get(i), i);
                    }

                    HomeAdapter adapter = new HomeAdapter(this, devices, this);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
    }); // End of registerForActivityResult()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setCustomActionBar();

        itemsDeclaration();

        DataCollector.collect(this);
        List<Device> devices = Arrays.asList(DataCollector.getDevices());

        HomeAdapter adapter = new HomeAdapter(this, devices, this);
        recyclerView.setAdapter(adapter);
    } // End of onCreate()

    @Override
    public void onBackPressed() {
        vibration(0);
        if (BackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, getString(R.string.tap_again), Toast.LENGTH_SHORT).show();
        }
        BackPressed = System.currentTimeMillis();
    }  // End of onBackPressed()

    private void setCustomActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
    } // End of setCustomActionBar()

    private void itemsDeclaration() {

        Loading.progressDialogConstructor(this);

        fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);

        findViewById(R.id.tool_bar_title).setOnLongClickListener(v -> {
            if (sharedPreferences.getString(Common.USER_PASSWORD, "").equals("")) {
                Password password = new Password(passcode -> {
                    try {
                        if (passcode.equals(EncryptionAndDecryption.decrypt(Common.PASSWORD))) {

                            Intent revenue = new Intent(Home.this, Revenue.class);
                            revenue.putExtra(Common.TITLE, getString(R.string.years));
                            revenue.putExtra(Common.YEAR, true);

                            startActivity(revenue);

                            if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Common.USER_PASSWORD, passcode);
                                editor.apply();
                            }

                            Intent intent = new Intent(this, PasswordRemovalService.class);
                            startService(intent);

                        } else {
                            Toast.makeText(this, "😒", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ignored) {
                    }
                });

                password.show(getSupportFragmentManager(), "password_dialog");
            } else {

                // Check if the device has a fingerprint sensor
                if (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    // Create a prompt to ask the user to set up a fingerprint
                    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle(getString(R.string.use_your_fingerprint)).setDescription(getString(R.string.free_kick_verify)).setNegativeButtonText(getString(R.string.cancel)).build();

                    // Create a callback to handle the result of the authentication prompt
                    BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);

                            Intent revenue = new Intent(Home.this, Revenue.class);
                            revenue.putExtra(Common.TITLE, getString(R.string.years));
                            revenue.putExtra(Common.YEAR, true);

                            startActivity(revenue);
                        }
                    };

                    // Create a BiometricPrompt to show the authentication prompt
                    BiometricPrompt biometricPrompt = new BiometricPrompt(this, Executors.newSingleThreadExecutor(), authenticationCallback);

                    // Show the authentication prompt
                    biometricPrompt.authenticate(promptInfo);
                }

            }
            return false;
        });

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        try {
            sharedPreferences = getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        gson = new Gson();

        recyclerView = findViewById(R.id.home_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    } // End of itemsDeclaration()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    } // End of onCreateOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.upload) {
            Device[] devices = new Device[Common.DEVICES_NUMBER];

            for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                if (gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), Device.class) != null)
                    devices[i] = gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), Device.class);
                else devices[i] = new Device(null, "", "", 8, 0, true, false, false);
            }

            Firebase.upload(devices, this);

            return true;
        } else if (item.getItemId() == R.id.download) {
            download();
            return true;
        } else if (item.getItemId() == R.id.qr) {

            DataCollector.collect(this);

            BitMatrix bitMatrix = null;
            try {
                bitMatrix = new QRCodeWriter().encode(EncryptionAndDecryption.encrypt(DataCollector.getData()), BarcodeFormat.QR_CODE, 780, 780);
            } catch (Exception ignored) {

            }
            int width = Objects.requireNonNull(bitMatrix).getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            Qr.showQrDialog(this, bitmap);

            return true;
        } else if (item.getItemId() == R.id.scanner) {

            ScanOptions scanOptions = new ScanOptions();
            scanOptions.setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);
            scanOptionsActivityResultLauncher.launch(scanOptions);

            return true;
        } else if (item.getItemId() == R.id.delAll) {
            List<Device> devices = new ArrayList<>();

            for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                devices.add(null);

                sharedPreferences.edit().putString(String.format("h%s", i), sharedPreferences.getString(String.valueOf(i), "")).apply();
                sharedPreferences.edit().putString(String.valueOf(i), null).apply();
            }

            HomeAdapter adapter = new HomeAdapter(this, devices, this);
            recyclerView.setAdapter(adapter);

            return true;
        } else if (item.getItemId() == R.id.exit) {
            finish();
            return true;
        } else return super.onOptionsItemSelected(item);
        /*else if (item.getItemId() == R.id.online_history) {

            return true;
        } else if (item.getItemId() == R.id.first_online_history) {

            return true;
        } else if (item.getItemId() == R.id.second_online_history) {

            return true;
        } else if (item.getItemId() == R.id.third_online_history) {

            return true;
        } else if (item.getItemId() == R.id.fourth_online_history) {

            return true;
        } */
    } // End of onOptionsItemSelected()

    private void download() {
        Loading.showProgressDialog();
        if (!Internet.isConnected(this)) Loading.dismissProgressDialog();

        Firebase.getRoot().child(Common.DEVICE).addListenerForSingleValueEvent(new ValueEventListener() {
            final List<Device> deviceList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    deviceList.add(dataSnapshot.getValue(Device.class));
                }

                if (deviceList.size() != 0) {
                    HomeAdapter adapter = new HomeAdapter(Home.this, deviceList, Home.this);
                    recyclerView.setAdapter(adapter);
                    Loading.dismissProgressDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } // End of download()

    @Override
    public void languageHandler(Device device, int position) {
        if (device != null) {
            if (device.getStartingTime().contains("م") || device.getStartingTime().contains("ص")) {
                if (Locale.getDefault().getLanguage().equals("en")) {
                    device.setHeader(HomeAdapter.ViewHolder.headers[position]);
                    device.setStartingTime(device.getStartingTime().replace("م", "PM").replace("ص", "AM"));
                    device.setEndingTime(device.getEndingTime().replace("م", "PM").replace("ص", "AM"));
                }
            } else if (device.getStartingTime().contains("PM") || device.getStartingTime().contains("AM")) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    device.setHeader(HomeAdapter.ViewHolder.headers[position]);
                    device.setStartingTime(device.getStartingTime().replace("PM", "م").replace("AM", "ص"));
                    device.setEndingTime(device.getEndingTime().replace("PM", "م").replace("AM", "ص"));
                }
            }

            try {
                Double.parseDouble(device.getHeader());
            } catch (Exception e) {
                device.setHeader(null);
            }
        }
    } // End of languageHandler()

    @Override
    public void popTimePicker(TextInputEditText textInputEditText) {
        MaterialTimePicker timePicker;
        timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(LocalTime.now().getHour()).setMinute(LocalTime.now().getMinute()).build();

        timePicker.show(getSupportFragmentManager(), "timePicker");

        timePicker.addOnPositiveButtonClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            textInputEditText.setText(DateAndTime.timeFormatter(timePicker.getHour(), timePicker.getMinute()));
        });
    } // End of popTimePicker()

    @Override
    public void vibration(int effect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effect));
        }
    } // End of vibration()

}