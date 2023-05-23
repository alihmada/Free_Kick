package ao.play.freekick.Data;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;

public class Firebase {
    static double annualTally, monthlyTally, dailyTally, deviceOutcome;
    static String annualDuration, monthlyDuration, dailyDuration, deviceDuration;

    public static DatabaseReference getRoot() {
        return FirebaseDatabase.getInstance().getReference(Common.ROOT);
    }

    public static DatabaseReference getDatabase() {
        return getRoot().child(Common.DATA_BASE_NAME);
    }

    public static DatabaseReference getYear(String year) {
        return getDatabase().child(year);
    }

    public static DatabaseReference getMonth(String year, String month) {
        return getYear(year).child(month);
    }

    public static DatabaseReference getDay(String year, String month, String day) {
        return getMonth(year, month).child(day);
    }

    public static DatabaseReference getDevice(String year, String month, String day, String device) {
        return getDay(year, month, day).child(device);
    }

    public static DatabaseReference getMonth(DatabaseReference year, String month) {
        return year.child(month);
    }

    public static DatabaseReference getDay(DatabaseReference month, String day) {
        return month.child(day);
    }

    public static DatabaseReference getDevice(DatabaseReference day, String device) {
        return day.child(device);
    }

    public static DatabaseReference getController() {
        return getRoot().child(Common.CONTROLLERS);
    }

    public static void upload(Device[] data, Context context) {
        Loading.showProgressDialog();
        if (!Internet.isConnected(context)) Loading.dismissProgressDialog();

        getRoot().child(Common.DEVICE).addListenerForSingleValueEvent(new ValueEventListener() {
            final List<Device> devices = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // read data from device branch from firebase database
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    devices.add(dataSnapshot.getValue(Device.class));
                }

                // get history branch from firebase database
                DatabaseReference devicesHistory = getRoot().child(Common.DEVICES_HISTORY);

                // move the device branch data to history branch
                for (int i = 0; i < devices.size(); i++)
                    devicesHistory.child(String.valueOf(i)).setValue(devices.get(i));

                // upload data to database
                for (int i = 0; i < data.length; i++)
                    getRoot().child(Common.DEVICE).child(String.valueOf(i)).setValue(data[i]);

                Loading.dismissProgressDialog();
                Toast.makeText(context, context.getString(R.string.data_uploaded), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    } // End upload()

    public static void save(String deviceNumber, RevenueDeviceData revenueDeviceData, Context context) {

        Internet.isConnected(context);

        DatabaseReference year = getYear(DateAndTime.getYear());

        year.child(Common.NUMBER).setValue(DateAndTime.getYear()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });


        DatabaseReference month = getMonth(year, DateAndTime.getMonth());

        month.child(Common.NUMBER).setValue(DateAndTime.getMonth()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        month.child(Common.ARABIC_NAME).setValue(DateAndTime.getArabicNameOfMonth()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        month.child(Common.ENGLISH_NAME).setValue(DateAndTime.getEnglishNameOfMonth()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        DatabaseReference day = getDay(month, DateAndTime.getDay());

        day.child(Common.NUMBER).setValue(DateAndTime.getDay()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        day.child(Common.ARABIC_NAME).setValue(DateAndTime.getArabicNameOfDay()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        day.child(Common.ENGLISH_NAME).setValue(DateAndTime.getEnglishNameOfDay()).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });

        DatabaseReference device = getDevice(day, deviceNumber);

        queryGetStarting(year, month, day, device, deviceNumber, context, revenueDeviceData);
    } //End save()

    private static void onSuccess(Void unused) {

    }

    private static void queryGetStarting(DatabaseReference year, DatabaseReference month, DatabaseReference day, DatabaseReference device, String deviceNumber, Context context, RevenueDeviceData revenueDeviceData) {

        Query query = device.orderByChild("start").equalTo(revenueDeviceData.getStart());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    device.child(Common.NUMBER).setValue(deviceNumber).addOnSuccessListener(Firebase::onSuccess).addOnFailureListener(e -> {
                    });

                    device.child(Common.PRICE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressWarnings("ConstantConditions")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                deviceOutcome = Double.parseDouble(snapshot.getValue().toString()) + Double.parseDouble(revenueDeviceData.getPrice());
                            } else {
                                deviceOutcome = Double.parseDouble(revenueDeviceData.getPrice());
                            } // device if - else

                            device.child(Common.PRICE).setValue(deviceOutcome).addOnSuccessListener(unused -> day.child(Common.PRICE).addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressWarnings("ConstantConditions")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        dailyTally = Double.parseDouble(snapshot.getValue().toString()) + Double.parseDouble(revenueDeviceData.getPrice());
                                    } else {
                                        dailyTally = Double.parseDouble(revenueDeviceData.getPrice());
                                    } // dailyTally if - else

                                    day.child(Common.PRICE).setValue(dailyTally).addOnSuccessListener(unused -> month.child(Common.PRICE).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @SuppressWarnings("ConstantConditions")
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()) {
                                                monthlyTally = Double.parseDouble(snapshot.getValue().toString()) + Double.parseDouble(revenueDeviceData.getPrice());
                                            } else {
                                                monthlyTally = Double.parseDouble(revenueDeviceData.getPrice());
                                            } // monthlyTally if - else

                                            month.child(Common.PRICE).setValue(monthlyTally).addOnSuccessListener(unused -> year.child(Common.PRICE).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @SuppressWarnings("ConstantConditions")
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.exists()) {
                                                        annualTally = Double.parseDouble(snapshot.getValue().toString()) + Double.parseDouble(revenueDeviceData.getPrice());
                                                    } else {
                                                        annualTally = Double.parseDouble(revenueDeviceData.getPrice());
                                                    } // annualTally if - else

                                                    year.child(Common.PRICE).setValue(annualTally).addOnSuccessListener(unused -> {

                                                    }).addOnFailureListener(e -> {

                                                    });
                                                } // End month onDataChange()

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                } // End month onCancelled()
                                            })).addOnFailureListener(e -> {

                                            });
                                        } // End day onDataChange()

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        } // End day onCancelled()
                                    })).addOnFailureListener(e -> {

                                    });
                                } // End device onDataChange()

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                } // End device onCancelled()
                            })).addOnFailureListener(e -> {

                            });

                        } // End device onDataChange()

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }); // End device ValueEventListener()

                    device.child(Common.DURATION).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressWarnings("ConstantConditions")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                deviceDuration = DateAndTime.durationPlus(snapshot.getValue().toString(), DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString());
                            } else {
                                deviceDuration = DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString();
                            } // device if - else

                            device.child(Common.DURATION).setValue(deviceDuration).addOnSuccessListener(unused -> day.child(Common.DURATION).addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressWarnings("ConstantConditions")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()) {
                                        dailyDuration = DateAndTime.durationPlus(snapshot.getValue().toString(), DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString());
                                    } else {
                                        dailyDuration = DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString();
                                    } // dailyTally if - else

                                    day.child(Common.DURATION).setValue(dailyDuration).addOnSuccessListener(unused -> month.child(Common.DURATION).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @SuppressWarnings("ConstantConditions")
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (snapshot.exists()) {
                                                monthlyDuration = DateAndTime.durationPlus(snapshot.getValue().toString(), DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString());
                                            } else {
                                                monthlyDuration = DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString();
                                            } // dailyTally if - else

                                            month.child(Common.DURATION).setValue(monthlyDuration).addOnSuccessListener(unused -> year.child(Common.DURATION).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @SuppressWarnings("ConstantConditions")
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.exists()) {
                                                        annualDuration = DateAndTime.durationPlus(snapshot.getValue().toString(), DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString());
                                                    } else {
                                                        annualDuration = DateAndTime.timeDifference(Firebase.languageHandler(revenueDeviceData.getStart()), Firebase.languageHandler(revenueDeviceData.getEnd())).toString();
                                                    } // annualTally if - else

                                                    year.child(Common.DURATION).setValue(annualDuration).addOnSuccessListener(unused -> {

                                                    }).addOnFailureListener(e -> {

                                                    });
                                                } // End month onDataChange()

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                } // End month onCancelled()
                                            })).addOnFailureListener(e -> {

                                            });
                                        } // End day onDataChange()

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        } // End day onCancelled()
                                    })).addOnFailureListener(e -> {

                                    });
                                } // End device onDataChange()

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                } // End device onCancelled()
                            })).addOnFailureListener(e -> {

                            });

                        } // End device onDataChange()

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }); // End device ValueEventListener()

                    device.push().setValue(revenueDeviceData).addOnSuccessListener(unused -> Toast.makeText(context, context.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show());
                }
            } // End query onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String languageHandler(String text) {
        if (text.contains("م") || text.contains("ص")) {
            if (Locale.getDefault().getLanguage().equals("en")) {
                return text.replace("م", "PM").replace("ص", "AM");
            }
        } else if (text.contains("PM") || text.contains("AM")) {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                return text.replace("PM", "م").replace("AM", "ص");
            }
        }
        return text;
    }
} //End Firebase
