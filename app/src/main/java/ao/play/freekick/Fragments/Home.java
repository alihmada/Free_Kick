package ao.play.freekick.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Classes.FirstItemMarginDecoration;
import ao.play.freekick.Classes.QRConstructor;
import ao.play.freekick.Data.DataCollector;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Dialogs.Qr;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewListener;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.R;

public class Home extends Fragment implements ViewListener {
    private ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private Vibrator vibrator;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            sharedPreferences = requireActivity().getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        gson = new Gson();

        recyclerView = view.findViewById(R.id.home_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));

        DataCollector.collect(requireContext());
        List<Device> devices = Arrays.asList(DataCollector.getDevices());

        setupScanOptionsActivityResultLauncher();
        setupRecyclerView(devices);
        setupVibrator();

        return view;
    }

    private void setupScanOptionsActivityResultLauncher() {
        scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
            try {
                if (result.getContents() != null) {
                    String code = null;
                    try {
                        code = Ciphering.decrypt(result.getContents());
                    } catch (Exception ignored) {
                        Toast.makeText(requireContext(), result.getContents(), Toast.LENGTH_LONG).show();
                    }

                    if (code != null) {
                        List<Device> devices = Arrays.asList(gson.fromJson(code, Device[].class));

                        for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                            languageHandler(devices.get(i), i);
                        }

                        setupRecyclerView(devices);
                    }
                }
            } catch (Exception ignored) {
            }
        }); // End of registerForActivityResult()
    }

    private void setupRecyclerView(List<Device> devices) {
        HomeAdapter adapter = new HomeAdapter(requireContext(), getParentFragmentManager(), devices, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupVibrator() {
        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.upload) {
            Device[] devices = new Device[Common.DEVICES_NUMBER];

            for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                if (gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), Device.class) != null)
                    devices[i] = gson.fromJson(sharedPreferences.getString(String.valueOf(i), ""), Device.class);
                else devices[i] = new Device(null, "", "", 8, 0, true, false, false);
            }

            Firebase.upload(devices, requireContext());

            return true;
        } else if (item.getItemId() == R.id.download) {
            download();
            return true;
        } else if (item.getItemId() == R.id.qr) {
            DataCollector.collect(requireContext());
            Qr qr = new Qr(QRConstructor.createQR(DataCollector.getData()));
            qr.show(getParentFragmentManager(), "");
            return true;
        } else if (item.getItemId() == R.id.scanner) {
            try {
                setupScanner();
            } catch (IllegalStateException e) {
                setupScanOptionsActivityResultLauncher();
                setupScanner();
            }
            return true;
        } else if (item.getItemId() == R.id.delAll) {
            List<Device> devices = new ArrayList<>();

            for (int i = 0; i < Common.DEVICES_NUMBER; i++) {
                devices.add(null);

                sharedPreferences.edit().putString(String.format("h%s", i), sharedPreferences.getString(String.valueOf(i), "")).apply();
                sharedPreferences.edit().putString(String.valueOf(i), null).apply();
            }

            setupRecyclerView(devices);

            return true;
        } else if (item.getItemId() == R.id.exit) {
            requireActivity().finish();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    private void setupScanner() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);
        scanOptionsActivityResultLauncher.launch(scanOptions);
    }

    private void download() {
        Loading.showProgressDialog();
        if (!Internet.isConnected(requireContext())) Loading.dismissProgressDialog();

        Firebase.getRoot(requireContext()).child(Common.DEVICE).addListenerForSingleValueEvent(new ValueEventListener() {
            final List<Device> deviceList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    deviceList.add(dataSnapshot.getValue(Device.class));
                }

                if (deviceList.size() != 0) {
                    setupRecyclerView(deviceList);
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
            String[] headers = getResources().getStringArray(R.array.headers);
            if (device.getStartingTime().contains("م") || device.getStartingTime().contains("ص")) {
                if (Locale.getDefault().getLanguage().equals("en")) {
                    device.setHeader(headers[position]);
                    device.setStartingTime(device.getStartingTime().replace("م", "PM").replace("ص", "AM"));
                    device.setEndingTime(device.getEndingTime().replace("م", "PM").replace("ص", "AM"));
                }
            } else if (device.getStartingTime().contains("PM") || device.getStartingTime().contains("AM")) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    device.setHeader(headers[position]);
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
    }

    @Override
    public void popTimePicker(EditText textInputEditText) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H).setHour(LocalTime.now().getHour()).setMinute(LocalTime.now().getMinute()).build();

        timePicker.show(getChildFragmentManager(), "timePicker");

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