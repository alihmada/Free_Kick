package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ao.play.freekick.Adapters.DeviceDetailsAdapter;
import ao.play.freekick.Adapters.HomeAdapter;
import ao.play.freekick.Adapters.MonthsAndDaysAdapter;
import ao.play.freekick.Adapters.YearsAndDevicesAdapter;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Dialogs.Password;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.MonthAndDay;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.Models.YearAndDevice;
import ao.play.freekick.R;

public class Revenue extends AppCompatActivity implements ViewOnClickListener {
    public static boolean isDevice;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        itemsDeclaration();

        setCustomActionBar(getIntent().getStringExtra(Common.TITLE));

        if (getIntent().getBooleanExtra(Common.YEAR, false)) {
            isDevice = false;
            Firebase.getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
                final List<YearAndDevice> years = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            years.add(dataSnapshot.getValue(YearAndDevice.class));
                        }
                    } catch (Exception ignored) {
                    }

                    if (!years.isEmpty()) {
                        YearsAndDevicesAdapter adapter = new YearsAndDevicesAdapter(years, Revenue.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (getIntent().getBooleanExtra(Common.MONTH, false)) {
            Firebase.getYear(getIntent().getStringExtra(Common.YEAR_VALUE)).addListenerForSingleValueEvent(new ValueEventListener() {
                final List<MonthAndDay> months = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            months.add(dataSnapshot.getValue(MonthAndDay.class));
                        }
                    } catch (Exception ignored) {
                    }

                    if (!months.isEmpty()) {
                        MonthsAndDaysAdapter adapter = new MonthsAndDaysAdapter(months, Revenue.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (getIntent().getBooleanExtra(Common.DAY, false)) {
            Firebase.getMonth(getIntent().getStringExtra(Common.YEAR_VALUE), getIntent().getStringExtra(Common.MONTH_VALUE)).addListenerForSingleValueEvent(new ValueEventListener() {
                final List<MonthAndDay> days = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            days.add(dataSnapshot.getValue(MonthAndDay.class));
                        }
                    } catch (Exception ignored) {
                    }

                    if (!days.isEmpty()) {
                        MonthsAndDaysAdapter adapter = new MonthsAndDaysAdapter(days, Revenue.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (getIntent().getBooleanExtra(Common.DEVICE, false)) {
            isDevice = true;
            Firebase.getDay(getIntent().getStringExtra(Common.YEAR_VALUE), getIntent().getStringExtra(Common.MONTH_VALUE), getIntent().getStringExtra(Common.DAY_VALUE)).addListenerForSingleValueEvent(new ValueEventListener() {
                final List<YearAndDevice> devices = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            devices.add(dataSnapshot.getValue(YearAndDevice.class));
                        }
                    } catch (Exception ignored) {
                    }

                    if (!devices.isEmpty()) {
                        YearsAndDevicesAdapter adapter = new YearsAndDevicesAdapter(devices, Revenue.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else if (getIntent().getBooleanExtra(Common.DEVICE_DETAILS, false)) {
            String[] date = new String[]{getIntent().getStringExtra(Common.YEAR_VALUE), getIntent().getStringExtra(Common.MONTH_VALUE), getIntent().getStringExtra(Common.DAY_VALUE), getIntent().getStringExtra(Common.DEVICE_NUMBER)};
            Firebase.getDevice(getIntent().getStringExtra(Common.YEAR_VALUE), getIntent().getStringExtra(Common.MONTH_VALUE), getIntent().getStringExtra(Common.DAY_VALUE), getIntent().getStringExtra(Common.DEVICE_NUMBER)).addListenerForSingleValueEvent(new ValueEventListener() {
                final List<RevenueDeviceData> devices = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            devices.add(dataSnapshot.getValue(RevenueDeviceData.class));
                        }
                    } catch (Exception ignored) {
                    }

                    if (!devices.isEmpty()) {
                        DeviceDetailsAdapter adapter = new DeviceDetailsAdapter(devices, Revenue.this, date);
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
    }

    private void itemsDeclaration() {
        recyclerView = findViewById(R.id.revenue_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCustomActionBar(String title) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);

        TextView textView = findViewById(R.id.tool_bar_title);
        textView.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.revenue_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_password) {
            Password password = new Password(password1 -> ConfirmationDialog.show(this, getString(R.string.password_confirmation).concat(" \"").concat(password1).concat("\" ").concat(getString(R.string.password_confirmation_1)), new ConfirmationDialog.ConfirmationDialogListener() {
                @Override
                public void onConfirm() {
                    try {
                        if (Internet.isConnected(Revenue.this)) {
                            getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                                    .edit()
                                    .putString(Common.USER_PASSWORD, password1)
                                    .apply();

                            Firebase.getRoot().child("password").setValue(EncryptionAndDecryption.encrypt(password1));
                        }
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onCancel() {

                }
            }));

            password.show(getSupportFragmentManager(), "password_dialog");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(String position) {
        Intent intent = new Intent(this, Revenue.class);
        if (getIntent().getBooleanExtra(Common.YEAR, false)) {
            intent.putExtra(Common.YEAR_VALUE, position);
            intent.putExtra(Common.TITLE, getString(R.string.months));
            intent.putExtra(Common.MONTH, true);

            startActivity(intent);
        } else if (getIntent().getBooleanExtra(Common.MONTH, false)) {
            intent.putExtra(Common.YEAR_VALUE, getIntent().getStringExtra(Common.YEAR_VALUE));
            intent.putExtra(Common.MONTH_VALUE, position);
            intent.putExtra(Common.DAY, true);
            intent.putExtra(Common.TITLE, getString(R.string.days));

            startActivity(intent);
        } else if (getIntent().getBooleanExtra(Common.DAY, false)) {
            intent.putExtra(Common.YEAR_VALUE, getIntent().getStringExtra(Common.YEAR_VALUE));
            intent.putExtra(Common.MONTH_VALUE, getIntent().getStringExtra(Common.MONTH_VALUE));
            intent.putExtra(Common.DAY_VALUE, position);
            intent.putExtra(Common.DEVICE, true);
            intent.putExtra(Common.TITLE, getString(R.string.devices));

            startActivity(intent);
        } else if (getIntent().getBooleanExtra(Common.DEVICE, false)) {
            intent.putExtra(Common.YEAR_VALUE, getIntent().getStringExtra(Common.YEAR_VALUE));
            intent.putExtra(Common.MONTH_VALUE, getIntent().getStringExtra(Common.MONTH_VALUE));
            intent.putExtra(Common.DAY_VALUE, getIntent().getStringExtra(Common.DAY_VALUE));
            intent.putExtra(Common.DEVICE_NUMBER, position);
            intent.putExtra(Common.DEVICE_DETAILS, true);
            intent.putExtra(Common.TITLE, HomeAdapter.ViewHolder.HEADERS[Integer.parseInt(position) - 1]);

            startActivity(intent);
        }
    }

    @Override
    public void languageHandler(RevenueDeviceData revenueDeviceData) {
        if (revenueDeviceData != null) {
            if (revenueDeviceData.getStart().contains("PM") || revenueDeviceData.getStart().contains("AM")) {
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    revenueDeviceData.setStart(revenueDeviceData.getStart().replace("PM", "م").replace("AM", "ص"));
                    revenueDeviceData.setEnd(revenueDeviceData.getEnd().replace("PM", "م").replace("AM", "ص"));
                    revenueDeviceData.setState(revenueDeviceData.getState().replace("Solo", getString(R.string.radioButtonIndividual)).replace("Multi", getString(R.string.radioButtonMultiplayer)));
                }
            }
        }
    }
}
