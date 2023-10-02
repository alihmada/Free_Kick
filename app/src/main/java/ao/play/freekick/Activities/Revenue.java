package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ao.play.freekick.Adapters.DayAdapter;
import ao.play.freekick.Adapters.DeviceAdapter;
import ao.play.freekick.Adapters.MonthAdapter;
import ao.play.freekick.Adapters.TemporalAdapter;
import ao.play.freekick.Adapters.YearAdapter;
import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Classes.FirstItemMarginDecoration;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Dialogs.Password;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Day;
import ao.play.freekick.Models.Device;
import ao.play.freekick.Models.Month;
import ao.play.freekick.Models.Temporal;
import ao.play.freekick.Models.Year;
import ao.play.freekick.R;

public class Revenue extends AppCompatActivity implements ViewOnClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;
    private List<Year> years;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        bundle = new Bundle();
        alert = findViewById(R.id.alert);
        alert.setVisibility(View.GONE);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.revenue_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));

        setCustomActionBar(getIntent().getStringExtra(Common.TITLE));
        setupSwipeRefreshLayout();
        processIntentExtras();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            processIntentExtras();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void processIntentExtras() {
        if (getIntent() != null) {
            if (setupHaveInternet()) {
                if (Objects.requireNonNull(getIntent().getExtras()).getBoolean(Common.YEAR)) {
                    loadYearsData();
                } else if (getIntent().getBooleanExtra(Common.MONTH, false)) {
                    loadMonthsData();
                } else if (getIntent().getBooleanExtra(Common.DAY, false)) {
                    loadDaysData();
                } else if (getIntent().getBooleanExtra(Common.DEVICE, false)) {
                    loadDevicesData();
                } else if (getIntent().getBooleanExtra(Common.DEVICE_DETAILS, false)) {
                    loadDeviceDetailsData();
                }
            }
        }
    }

    private void loadYearsData() {
        Firebase.getDatabase(this).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                years = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        years.add(dataSnapshot.getValue(Year.class));
                    } catch (Exception ignored) {
                    }
                }
                if (!years.isEmpty()) {
                    Collections.reverse(years);
                    recyclerView.setAdapter(new YearAdapter(years, Revenue.this));
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadMonthsData() {
        String year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR_VALUE);

        Firebase.getYear(this, year).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Month> months = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        months.add(dataSnapshot.getValue(Month.class));
                    } catch (Exception ignored) {
                    }
                }
                if (!months.isEmpty()) {
                    Collections.reverse(months);
                    recyclerView.setAdapter(new MonthAdapter(months, Revenue.this));
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadDaysData() {
        String year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR_VALUE);
        String month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH_VALUE);

        Firebase.getMonth(this, year, month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Day> days = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        days.add(dataSnapshot.getValue(Day.class));
                    } catch (Exception ignored) {
                    }
                }
                if (!days.isEmpty()) {
                    Collections.reverse(days);
                    recyclerView.setAdapter(new DayAdapter(days, Revenue.this));
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadDevicesData() {
        String year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR_VALUE);
        String month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH_VALUE);
        String device = Objects.requireNonNull(getIntent().getExtras().getString(Common.DAY_VALUE));

        Firebase.getDay(this, year, month, device).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Device> devices = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        devices.add(dataSnapshot.getValue(Device.class));
                    } catch (Exception ignored) {
                    }
                }
                if (!devices.isEmpty()) {
                    recyclerView.setAdapter(new DeviceAdapter(Revenue.this, devices, Revenue.this));
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadDeviceDetailsData() {
        String year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR_VALUE);
        String month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH_VALUE);
        String device = Objects.requireNonNull(getIntent().getExtras().getString(Common.DAY_VALUE));
        String device_number = Objects.requireNonNull(getIntent().getExtras().getString(Common.DEVICE_NUMBER));

        Firebase.getDevice(this, year, month, device, device_number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Temporal> devices = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        devices.add(dataSnapshot.getValue(Temporal.class));
                    } catch (Exception ignored) {
                    }
                }
                if (!devices.isEmpty()) {
                    setupRevenueDeviceDataRecycleView(devices, new String[]{year, month, device, device_number});
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setCustomActionBar(String title) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
        TextView textView = findViewById(R.id.tool_bar_title);
        textView.setText(title);
    }

    private boolean setupHaveInternet() {
        boolean isConnected = Internet.isConnectedWithoutMessage(this);
        if (isConnected) {
            if (Internet.isNetworkLimited(this)) {
                setupWifi(getString(R.string.internet_limited));
            } else {
                alert.setVisibility(View.GONE);
            }
        } else {
            setupWifi(getString(R.string.no_internet));
        }
        return isConnected;
    }

    private void setupWifi(String message) {
        alert.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.wifi_off));
        textView.setText(message);
    }

    private void setupRevenueDeviceDataRecycleView(List<Temporal> revenueDeviceData, String[] data) {
        recyclerView.setAdapter(new TemporalAdapter(Revenue.this, revenueDeviceData, Revenue.this, data));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.revenue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_password) {
            showPasswordDialog();
            return true;
        } else if (item.getItemId() == R.id.remember_me_remover) {
            removeRememberMe();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPasswordDialog() {

        Password password = new Password((password1, isRememberMe) -> {
            ConfirmationDialog dialog = new ConfirmationDialog(getString(R.string.password_confirmation).concat(" \"").concat(password1).concat("\" ").concat(getString(R.string.password_confirmation_1)), new ConfirmationDialog.ConfirmationDialogListener() {
                @Override
                public void onConfirm() {
                    try {
                        if (Internet.isConnected(Revenue.this)) {
                            getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE).edit().putBoolean(Common.REMEMBER_ME, isRememberMe).apply();

                            Firebase.getRoot(Revenue.this).child("password").setValue(Ciphering.encrypt(password1));
                        }
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onCancel() {
                }
            });
            dialog.show(getSupportFragmentManager(), "");
        });
        password.show(getSupportFragmentManager(), "password_dialog");
    }

    private void removeRememberMe() {
        try {
            getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE).edit().remove(Common.REMEMBER_ME).apply();

            Toast.makeText(this, getString(R.string.done), Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClickListener(String position, String device_name) {
        Intent intent = new Intent(this, Revenue.class);
        if (Objects.requireNonNull(getIntent().getExtras()).getBoolean(Common.YEAR)) {
            bundle.putString(Common.YEAR_VALUE, position);
            bundle.putString(Common.TITLE, getString(R.string.months));
            bundle.putBoolean(Common.MONTH, true);
            intent.putExtras(bundle);
        } else if (getIntent().getExtras().getBoolean(Common.MONTH)) {
            bundle.putString(Common.YEAR_VALUE, getIntent().getExtras().getString(Common.YEAR_VALUE));
            bundle.putString(Common.MONTH_VALUE, position);
            bundle.putString(Common.TITLE, getString(R.string.days));
            bundle.putBoolean(Common.DAY, true);
            intent.putExtras(bundle);
        } else if (getIntent().getExtras().getBoolean(Common.DAY)) {
            bundle.putString(Common.YEAR_VALUE, getIntent().getExtras().getString(Common.YEAR_VALUE));
            bundle.putString(Common.MONTH_VALUE, getIntent().getExtras().getString(Common.MONTH_VALUE));
            bundle.putString(Common.DAY_VALUE, position);
            bundle.putString(Common.TITLE, getString(R.string.devices));
            bundle.putBoolean(Common.DEVICE, true);
            intent.putExtras(bundle);
        } else if (getIntent().getExtras().getBoolean(Common.DEVICE)) {
            bundle.putString(Common.YEAR_VALUE, getIntent().getExtras().getString(Common.YEAR_VALUE));
            bundle.putString(Common.MONTH_VALUE, getIntent().getExtras().getString(Common.MONTH_VALUE));
            bundle.putString(Common.DAY_VALUE, getIntent().getExtras().getString(Common.DAY_VALUE));
            bundle.putString(Common.DEVICE_NUMBER, position);
            bundle.putBoolean(Common.DEVICE_DETAILS, true);
            bundle.putString(Common.TITLE, device_name);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void languageHandler(Temporal temporal) {
        if (temporal != null) {
            String start = temporal.getStart();
            String end = temporal.getEnd();
            String state = temporal.getState();

            if (start.contains("PM") || start.contains("AM")) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    temporal.setStart(start.replace("PM", "م").replace("AM", "ص"));
                    temporal.setEnd(end.replace("PM", "م").replace("AM", "ص"));
                    temporal.setState(state.replace("Solo", getString(R.string.radioButtonIndividual)).replace("Multi", getString(R.string.radioButtonMultiplayer)));
                }
            }
        }
    }
}