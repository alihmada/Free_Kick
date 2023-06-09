package ao.play.freekick.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ao.play.freekick.Classes.Calculations;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Interfaces.ViewListener;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;
import ao.play.freekick.Receivers.AlarmReceiver;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    public static HashMap<Integer, Timer> integerTimerHashMap = new HashMap<>();
    List<Device> devices;
    Context context;
    ViewListener viewListener;

    public HomeAdapter(Context context, List<Device> devices, ViewListener viewListener) {
        this.context = context;
        this.devices = devices;
        this.viewListener = viewListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_controller, parent, false);
        return new ViewHolder(view, context, viewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (devices.get(position) != null) {

            viewListener.languageHandler(devices.get(position), position);

            if (devices.get(position).getHeader() != null) {
                holder.header.setText(devices.get(position).getHeader());
            } else {
                holder.header.setText(ViewHolder.HEADERS[position]);
            }
            holder.starting.setText(devices.get(position).getStartingTime());
            holder.spinner.setVisibility(devices.get(position).getSpinnerVisibility() == 0 ? View.VISIBLE : View.GONE);
            holder.spinner.setSelection(devices.get(position).getSpinnerIndex());
            holder.ending.setText(devices.get(position).getEndingTime());
            if (devices.get(position).isSolo()) {
                holder.solo.setChecked(true);
            } else {
                holder.multi.setChecked(true);
            }
            holder.payment.setChecked(devices.get(position).isPayment());
            if (devices.get(position).isRunning()) holder.calculate.performClick();
        } else {
            holder.header.setText(ViewHolder.HEADERS[position]);
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public static String[] HEADERS;
        SharedPreferences sharedPreferences;
        Context context;
        ViewListener viewListener;
        AlarmManager alarmManager;
        PendingIntent pendingIntent;
        Timer timer;
        TimerTask timerTask;
        Gson gson;
        TextView header, time, price;
        Spinner spinner;
        TextInputLayout startingLayout, endingLayout;
        TextInputEditText starting, ending;
        RadioGroup radioGroup;
        RadioButton solo, multi;
        ImageButton delete, add, save, history;
        CheckBox payment;
        Button calculate;
        Duration currentDuration, fullDuration, durationDifference;
        int clickCounterForStarting, clickCounterForEnding, clickCounterForCalculate, clickCounterForForward;
        long doubleClick;
        boolean running, isForward = false, isOpenTime;

        public ViewHolder(@NonNull View itemView, Context context, ViewListener viewListener) {
            super(itemView);

            this.context = context;

            this.viewListener = viewListener;


            itemsDeclaration(); // items declaration

            methodsDeclaration(); // methods declaration

            spinner(); // spinner handler

            onTextChange(); // starting and ending time on text change handler
        } // End of ViewHolder() constructor

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.starting_time_text) {
                clickCounterForStarting = openTimePicker(starting, clickCounterForStarting);
            } else if (v.getId() == R.id.ending_time_text) {
                clickCounterForEnding = openTimePicker(ending, clickCounterForEnding);
            } else if (v.getId() == R.id.delete) {
                delete(HEADERS[getAdapterPosition()]);
            } else if (v.getId() == R.id.add) {
                ConfirmationDialog.show(context, context.getString(R.string.are_you_sure_add), new ConfirmationDialog.ConfirmationDialogListener() {
                    @Override
                    public void onConfirm() {
                        add();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else if (v.getId() == R.id.history) {
                history();
            } else if (v.getId() == R.id.save) {
                ConfirmationDialog.show(context, context.getString(R.string.are_you_sure_save), new ConfirmationDialog.ConfirmationDialogListener() {
                    @Override
                    public void onConfirm() {
                        save();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else if (v.getId() == R.id.calculate) {
                calculate();
            } else if (v.getId() == R.id.time) {
                viewListener.vibration(0); // vibrationEffect.EFFECT_CLICK
                isForward = Calculations.isEven(clickCounterForForward);
                clickCounterForForward++;
            }
        } // End of onClick()

        @Override
        public boolean onLongClick(View v) {
            if (v.getId() == R.id.starting_time_text) {
                starting();
                return true;
            } else if (v.getId() == R.id.ending_time_text) {
                ending();
                return true;
            } else {
                return false;
            }
        } // End of onLongClick()

        private void itemsDeclaration() {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            try {
                sharedPreferences = context.getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
            } catch (Exception ignored) {
            }

            HEADERS = context.getResources().getStringArray(R.array.headers);
            gson = new Gson();

            header = itemView.findViewById(R.id.header);
            spinner = itemView.findViewById(R.id.spinner);
            startingLayout = itemView.findViewById(R.id.starting_time_layout);
            starting = itemView.findViewById(R.id.starting_time_text);
            endingLayout = itemView.findViewById(R.id.ending_time_layout);
            ending = itemView.findViewById(R.id.ending_time_text);
            radioGroup = itemView.findViewById(R.id.radio_button_group);
            solo = itemView.findViewById(R.id.solo);
            multi = itemView.findViewById(R.id.multi);
            delete = itemView.findViewById(R.id.delete);
            add = itemView.findViewById(R.id.add);
            save = itemView.findViewById(R.id.save);
            history = itemView.findViewById(R.id.history);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);
            payment = itemView.findViewById(R.id.payment);
            calculate = itemView.findViewById(R.id.calculate);
        } // End of itemsDeclaration()

        private void methodsDeclaration() {
            starting.setOnLongClickListener(this);
            ending.setOnLongClickListener(this);

            starting.setOnClickListener(this);
            ending.setOnClickListener(this);
            time.setOnClickListener(this);

            delete.setOnClickListener(this);
            add.setOnClickListener(this);
            history.setOnClickListener(this);
            save.setOnClickListener(this);
            calculate.setOnClickListener(this);

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> storeData());

            payment.setOnCheckedChangeListener((buttonView, isChecked) -> storeData());

            solo.setChecked(true);
        } // End of methodsDeclaration()

        private void spinner() {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.spinner_items, R.layout.spinner_selected_item);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        spinnerOnItemSelected();
                    } catch (Exception e) {
                        Toast.makeText(context, context.getString(R.string.error_in_inputs), Toast.LENGTH_SHORT).show();
                    }
                    storeData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } // End of spinner()

        private void spinnerOnItemSelected() {
            if (!spinner.getSelectedItem().toString().equals(context.getString(R.string.custom))) {
                if (spinner.getSelectedItem().toString().equals(context.getString(R.string.empty))) {
                    if (!String.valueOf(ending.getText()).equals("")) ending.setText("");
                } else {
                    ending.setText(DateAndTime.setACustomTime(String.valueOf(starting.getText()), (long) (Calculations.mul(Double.parseDouble(spinner.getSelectedItem().toString()), 60))));
                }
            }
        } // End of SpinnerOnItemSelected()

        private void starting() {
            viewListener.vibration(5); // viewListener.vibrationEffect.EFFECT_HEAVY_CLICK
            pushToHistory();
            starting.setText(DateAndTime.getCurrentTime());
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelection(0);
            if (payment.isChecked()) payment.setChecked(false);
            if (!solo.isChecked()) solo.setChecked(true);
        }

        private void ending() {
            try {
                if (timeMatcher(String.valueOf(ending.getText()))) {
                    endingCustomInputHandler(DateAndTime.setACustomTime(String.valueOf(starting.getText()), String.valueOf(ending.getText())));
                } else if (priceMatcher(String.valueOf(ending.getText()))) {
                    endingCustomInputHandler(DateAndTime.priceDependentTiming(String.valueOf(starting.getText()), Calculations.div(Calculations.mul(Double.parseDouble(String.valueOf(ending.getText())), 60), (solo.isChecked() ? 10 : 20))));
                }
            } catch (Exception exception) {
                if (String.valueOf(ending.getText()).equals("")) {
                    Toast.makeText(context, context.getString(R.string.ending_input_not_found), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.starting_time_not_found), Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void onTextChange() {
            starting.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (timeFormatMatcher(String.valueOf(charSequence))) {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setSelection(0);

                        storeData();

                        if (String.valueOf(ending.getText()).equals("")) {
                            calculate.setText(context.getString(R.string.start));
                        } else if (timeFormatMatcher(String.valueOf(ending.getText()))) {
                            if (DateAndTime.timeConverter(String.valueOf(charSequence)).isBefore(DateAndTime.timeConverter(String.valueOf(ending.getText()))) && LocalDateTime.now().isBefore(DateAndTime.timeConverter(String.valueOf(ending.getText())))) {
                                calculate.setText(context.getString(R.string.timer));
                            } else {
                                calculate.setText(context.getString(R.string.calculate));
                            }
                        } else {
                            calculate.setText(context.getString(R.string.calculate));
                        }
                        startingLayout.setErrorEnabled(false);
                    } else if (String.valueOf(charSequence).equals("")) {
                        calculate.setText(context.getString(R.string.calculate));
                        spinner.setVisibility(View.GONE);

                        storeData();

                        startingLayout.setErrorEnabled(false);
                    } else {
                        startingLayout.setError(context.getString(R.string.invalid_time_format));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    shutdownTimer();
                    shutdownAlarm();
                }
            });

            ending.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    endingOnTextChange(charSequence);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    shutdownTimer();
                    shutdownAlarm();
                }
            });
        }

        private boolean timeMatcher(String text) {
            return text.matches("^(0?[0-9]|1[0-9]|2[0-3]):(0?[0-9]|[1-5][0-9])$");
        }

        private boolean timeFormatMatcher(String text) {
            return text.matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d) (0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9] (AM|am|PM|pm|ص|م)$");
        }

        private boolean priceMatcher(String text) {
            return text.matches("\\d+(\\.\\d+)?");
        }

        private void endingCustomInputHandler(String time) {
            ending.setText(time);
            viewListener.vibration(5); // viewListener.vibrationEffect.EFFECT_HEAVY_CLICK
            spinner.setSelection(9);
        }

        private void endingOnTextChange(CharSequence charSequence) {
            endingInputTextChecker(charSequence);
            if (String.valueOf(charSequence).equals("")) {
                spinner.setSelection(0);
                if (timeFormatMatcher(String.valueOf(starting.getText()))) {
                    calculate.setText(context.getString(R.string.start));
                }
            } else if (charSequence.charAt(0) == ' ' && charSequence.length() == 1) {
                ending.setText(DateAndTime.getCurrentTime());
                viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK
                spinner.setSelection(9);
            }
        }

        private void endingInputTextChecker(CharSequence charSequence) {
            if (timeFormatMatcher(String.valueOf(charSequence)) || String.valueOf(ending.getText()).equals("")) {
                storeData();

                if (timeFormatMatcher(String.valueOf(charSequence))) {
                    if (LocalDateTime.now().isBefore(DateAndTime.timeConverter(String.valueOf(charSequence)))) {
                        calculate.setText(context.getString(R.string.timer));
                    } else {
                        calculate.setText(context.getString(R.string.calculate));
                    }
                }
                endingLayout.setErrorEnabled(false);
                endingLayout.setHelperTextEnabled(false);
            } else if (timeMatcher(String.valueOf(charSequence))) {
                endingLayout.setHelperText(String.format("%s %s", context.getString(R.string.time_price_match_helper), context.getString(R.string.time).toLowerCase()));
            } else if (priceMatcher(String.valueOf(charSequence))) {
                endingLayout.setHelperText(String.format("%s %s", context.getString(R.string.time_price_match_helper), context.getString(R.string.price).toLowerCase()));
            } else {
                endingLayout.setError(context.getString(R.string.invalid_time_format));
            }
        }

        private int openTimePicker(TextInputEditText inputEditText, int clickCount) {
            clickCount++;
            if (doubleClick + Common.TIME_INTERVAL > System.currentTimeMillis()) {
                if (clickCount == 2) {
                    viewListener.vibration(1); // viewListener.vibrationEffect.EFFECT_DOUBLE_CLICK
                    viewListener.popTimePicker(inputEditText);
                    clickCount = 0;
                }
            } else {
                clickCount = 1;
            }

            doubleClick = System.currentTimeMillis();
            return clickCount;
        }

        private void delete(String head) {

            clickCounterForCalculate = 0;
            isOpenTime = false;

            if (!Objects.equals(String.valueOf(starting.getText()), "")) pushToHistory();

            viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK
            shutdownTimer();
            shutdownAlarm();
            header.setText(head);
            spinner.setSelection(0);
            starting.setText("");
            ending.setText("");
            time.setText(context.getString(R.string.time));
            price.setText(context.getString(R.string.price));
            solo.setChecked(true);
            payment.setChecked(false);

        } // End of delete()

        private void add() {
            viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK

            if (!String.valueOf(starting).equals("") && !price.getText().equals(context.getString(R.string.price))) {

                clickCounterForCalculate = 0;

                pushToHistory();

                try {
                    double saved = Double.parseDouble(header.getText().toString());
                    saved += Double.parseDouble(price.getText().toString());

                    save();

                    delete(String.valueOf(saved));

                    starting.setText(DateAndTime.getCurrentTime());

                } catch (Exception e) {

                    save();

                    delete(price.getText().toString());

                    starting.setText(DateAndTime.getCurrentTime());
                }

            } else {
                Toast.makeText(context, context.getString(R.string.check_for_data), Toast.LENGTH_SHORT).show();
            }
        } // End of add()

        private void history() {
            viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK

            Device data = getDataFromHistory();

            if (data != null && data.getStartingTime() != null && !data.getStartingTime().equals("")) {
                if (data.getHeader() != null) header.setText(data.getHeader());
                else header.setText(HEADERS[getAdapterPosition()]);
                starting.setText(data.getStartingTime());
                spinner.setVisibility(data.getSpinnerVisibility() == 8 ? View.GONE : View.VISIBLE);
                spinner.setSelection(data.getSpinnerIndex());
                ending.setText(data.getEndingTime());
                if (data.isSolo()) solo.setChecked(true);
                else multi.setChecked(true);
                if (data.isPayment()) payment.setChecked(true);
                if (data.isRunning()) calculate.performClick();
            } else
                Toast.makeText(context, context.getString(R.string.empty_history), Toast.LENGTH_SHORT).show();
        } // End of history()

        private void save() {
            viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK
            if (!String.valueOf(starting.getText()).equals("") && !String.valueOf(time.getText()).equals(context.getString(R.string.time))) {
                Firebase.save(String.valueOf(getAdapterPosition() + 1), getRevenueDeviceDataInstance(), context);
            } else
                Toast.makeText(context, context.getString(R.string.save_message_when_device_empty), Toast.LENGTH_SHORT).show();

        } // End of save()

        private RevenueDeviceData getRevenueDeviceDataInstance() {
            Duration duration = DateAndTime.timeDifference(String.valueOf(starting.getText()), String.valueOf(ending.getText()).equals("") ? DateAndTime.getCurrentTime() : String.valueOf(ending.getText()));
            return new RevenueDeviceData(languageHandler(String.valueOf(starting.getText())), String.valueOf(ending.getText()).equals("") ? languageHandler(DateAndTime.getCurrentTime()) : languageHandler(String.valueOf(ending.getText())), solo.isChecked() ? "Solo" : "Multi", DateAndTime.durationToClockFormat(duration), Calculations.round(Calculations.priceCalculator(solo.isChecked(), duration)));
        } // End of getDeviceInstance() -> save open time

        private void calculate() {
            viewListener.vibration(0); // viewListener.vibrationEffect.EFFECT_CLICK

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (isOpenTime) openTime();
                    else closedTime();
                }
            };

            String startingText = String.valueOf(starting.getText());
            String endingText = String.valueOf(ending.getText());

            if (timeFormatMatcher(startingText) && timeFormatMatcher(endingText) && DateAndTime.timeConverter(startingText).isBefore(DateAndTime.timeConverter(endingText)) && LocalDateTime.now().isBefore(DateAndTime.timeConverter(endingText))) {

                scheduleAlarm(DateAndTime.timeExtractor(endingText));

                timeOutOfRange();
                fullDuration = DateAndTime.timeDifference(startingText, endingText);

                timer.scheduleAtFixedRate(timerTask, 1000, 500);
                Toast.makeText(context, context.getString(R.string.timer_running), Toast.LENGTH_SHORT).show();

                running = true;
                storeData();

                integerTimerHashMap.put(getAdapterPosition(), timer);

            } else if (timeFormatMatcher(startingText) && timeFormatMatcher(endingText) && DateAndTime.timeConverter(startingText).isBefore(DateAndTime.timeConverter(endingText)) && LocalDateTime.now().isAfter(DateAndTime.timeConverter(endingText))) {

                timeOutOfRange();

            } else if (timeFormatMatcher(startingText) && endingText.equals("")) {
                isOpenTime = true;

                if (Calculations.isEven(clickCounterForCalculate)) {
                    calculate.setText(context.getString(R.string.stop));
                    timer.scheduleAtFixedRate(timerTask, 0, 1000);
                    Toast.makeText(context, context.getString(R.string.open_time_running), Toast.LENGTH_SHORT).show();
                    running = true;
                    storeData();
                } else {
                    ConfirmationDialog.show(context, context.getString(R.string.are_you_sure_calculate), new ConfirmationDialog.ConfirmationDialogListener() {
                        @Override
                        public void onConfirm() {
                            calculate.setText(context.getString(R.string.calculate));
                            ending.setText(DateAndTime.getCurrentTime());
                            shutdownTimer();
                            Toast.makeText(context, context.getString(R.string.open_time_off), Toast.LENGTH_SHORT).show();
                            storeData();
                            Firebase.save(String.valueOf(getAdapterPosition() + 1), getRevenueDeviceDataInstance(), context);
                        }

                        @Override
                        public void onCancel() {
                            clickCounterForCalculate--;
                        }
                    });
                }
                clickCounterForCalculate++;

            } else {
                Toast.makeText(context, context.getString(R.string.check_for_data), Toast.LENGTH_SHORT).show();
            }
        }

        private void openTime() {
            currentDuration = DateAndTime.timeDifference(String.valueOf(starting.getText()));
            time.setText(DateAndTime.durationToClockFormat(currentDuration));
            price.setText(Calculations.priceCalculator(solo.isChecked(), currentDuration));
        }

        private void closedTime() {
            if (isForward) {
                currentDuration = DateAndTime.timeDifference(String.valueOf(starting.getText()));
                durationDifference = fullDuration.minus(currentDuration);

                time.setText(DateAndTime.durationToClockFormat(durationDifference));
                price.setText(Calculations.priceCalculator(solo.isChecked(), durationDifference));
            } else {
                openTime();
            }
        }

        private void timeOutOfRange() {
            isOpenTime = false;
            Duration duration = DateAndTime.timeDifference(String.valueOf(starting.getText()), String.valueOf(ending.getText()));

            time.setText(DateAndTime.durationToClockFormat(duration));
            price.setText(Calculations.priceCalculator(solo.isChecked(), duration));
        }

        private void scheduleAlarm(int[] time) {
            Duration duration = DateAndTime.timeDifference(String.valueOf(starting.getText()), String.valueOf(ending.getText()));
            String price = Calculations.priceCalculator(solo.isChecked(), duration);

            Intent intent = new Intent(context, AlarmReceiver.class);

            intent.putExtra(Common.NOTIFICATION_CONTENT, context.getString(R.string.notification_content).concat(String.format("\n%s %s", context.getString(R.string.required_to_be_paid), price)));
            intent.putExtra(Common.NOTIFICATION_TITLE, String.format("%s %s", HEADERS[getAdapterPosition()], context.getString(R.string.notification_title)));
            intent.putExtra(Common.ITEM_POSITION, getAdapterPosition());

            sharedPreferences.edit().putString(Common.REVENUE.concat(String.valueOf(getAdapterPosition())), gson.toJson(getRevenueDeviceDataInstance())).apply();

            pendingIntent = PendingIntent.getBroadcast(context, getAdapterPosition(), intent, PendingIntent.FLAG_IMMUTABLE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, time[0]);
            calendar.set(Calendar.MINUTE, time[1]);
            calendar.set(Calendar.SECOND, time[2]);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        private void shutdownAlarm() {
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent);
                pendingIntent = null;
            }
        }

        private void shutdownTimer() {
            try {
                if (isOpenTime) clickCounterForCalculate = 0;
                if (timer != null) timer.cancel();
                running = false;
                storeData();
            } catch (Exception ignored) {
            }
        }

        private void storeData() {
            sharedPreferences.edit().putString(String.valueOf(getAdapterPosition()), getJsonDevice()).apply();
        }

        private void pushToHistory() {
            sharedPreferences.edit().putString(getHistoryKey(), getJsonDevice()).apply();
        }

        private Device getDataFromHistory() {
            return gson.fromJson(sharedPreferences.getString(getHistoryKey(), ""), Device.class);
        }

        private String getHistoryKey() {
            return Common.HISTORY.concat(String.valueOf(getAdapterPosition()));
        }

        private String getJsonDevice() {
            return gson.toJson(new Device(header.getText().toString(), String.valueOf(starting.getText()), String.valueOf(ending.getText()), spinner.getVisibility(), spinner.getSelectedItemPosition(), solo.isChecked(), payment.isChecked(), running));
        }

        private String languageHandler(String text) {
            if (text.contains("م") || text.contains("ص")) {
                return text.replace("م", "PM").replace("ص", "AM");
            }
            return text;
        }

    } // End of ViewHolder class

} // End of HomeAdapter class

