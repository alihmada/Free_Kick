package ao.play.freekick.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Data.DataCollector;
import ao.play.freekick.Interfaces.ViewListener;
import ao.play.freekick.R;

public class Home extends Fragment implements ViewListener {
    public static RecyclerView recyclerView;
    private Vibrator vibrator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.home_recycler);

        DataCollector.collect(requireContext());
        List<Device> devices = Arrays.asList(DataCollector.getDevices());

        setupRecyclerView(devices);
        setupVibrator();

        return view;
    }

    private void setupRecyclerView(List<Device> devices) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        HomeAdapter adapter = new HomeAdapter(requireContext(), devices, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupVibrator() {
        vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void languageHandler(Device device, int position) {
        if (device != null) {
            if (device.getStartingTime().contains("م") || device.getStartingTime().contains("ص")) {
                if (Locale.getDefault().getLanguage().equals("en")) {
                    device.setHeader(HomeAdapter.ViewHolder.HEADERS[position]);
                    device.setStartingTime(device.getStartingTime().replace("م", "PM").replace("ص", "AM"));
                    device.setEndingTime(device.getEndingTime().replace("م", "PM").replace("ص", "AM"));
                }
            } else if (device.getStartingTime().contains("PM") || device.getStartingTime().contains("AM")) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    device.setHeader(HomeAdapter.ViewHolder.HEADERS[position]);
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
    public void popTimePicker(TextInputEditText textInputEditText) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(LocalTime.now().getHour())
                .setMinute(LocalTime.now().getMinute())
                .build();

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