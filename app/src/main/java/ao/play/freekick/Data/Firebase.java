package ao.play.freekick.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.Device;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;

public class Firebase {
    private static SharedPreferences sharedPreferences;

    public static FirebaseDatabase getInstance() {
        return FirebaseDatabase.getInstance();
    } // End getInstance()

    public static DatabaseReference getRoot(Context context) {
        rootHaveValue(context);
        return getInstance().getReference(Common.getROOT());
    } // End getRoot()

    public static DatabaseReference getDatabase(Context context) {
        return getRoot(context).child(Common.DATABASE_NAME);
    } // End getDatabase()

    public static DatabaseReference getUsers(Context context) {
        return getRoot(context).child(Common.FIREBASE_USERS);
    } // End getDatabase()

    public static DatabaseReference getDevices(Context context) {
        return getRoot(context).child(Common.DEVICE);
    } // End getDatabase()

    public static DatabaseReference getYear(Context context, String year) {
        return getDatabase(context).child(year);
    } // End getYears()

    public static DatabaseReference getMonth(Context context, String year, String month) {
        return getYear(context, year).child(month);
    } // End getMonth()

    public static DatabaseReference getMonth(DatabaseReference year, String month) {
        return year.child(month);
    } // End getMonth()

    public static DatabaseReference getDay(Context context, String year, String month, String day) {
        return getMonth(context, year, month).child(day);
    } // End getDay()

    public static DatabaseReference getDay(DatabaseReference month, String day) {
        return month.child(day);
    } // End getDay()

    public static DatabaseReference getDevice(Context context, String year, String month, String day, String device) {
        return getDay(context, year, month, day).child(device);
    } // End getDevice()

    public static DatabaseReference getDevice(DatabaseReference day, String device) {
        return day.child(device);
    } // End getDevice()

    public static DatabaseReference getController(Context context) {
        return getRoot(context).child(Common.CONTROLLERS);
    } // End getController()

    public static DatabaseReference getDebt(Context context) {
        return getRoot(context).child(Common.DEBT);
    } // End of getDebt()

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    } // End of getFirebaseAuth()

    public static FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    } // End of getCurrentUser()

    public static String getPhoneNumber() {
        return getCurrentUser().getPhoneNumber();
    } // End of getPhoneNumber()

    public static void rootHaveValue(Context context) {
        try {
            sharedPreferences = context.getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        if (sharedPreferences != null) {
            String root = sharedPreferences.getString(Common.SHOP_ID, "");
            if (!Objects.equals(Common.getROOT(), root)) {
                Common.setROOT(root);
            }
        }
    }

    public static void upload(Device[] data, Context context) {
        Loading.showProgressDialog();
        if (!Internet.isConnected(context)) Loading.dismissProgressDialog();

        getDevices(context).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // upload data to database
                for (int i = 0; i < data.length; i++)
                    getDevices(context).child(String.valueOf(i)).setValue(data[i]);

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

        if (DateAndTime.isSpanningMultipleDays(revenueDeviceData.getStart(), revenueDeviceData.getEnd())) {

            String[] date = DateAndTime.getYesterday();

            DatabaseReference year = getYear(context, date[2]);

            DatabaseReference month = getMonth(year, date[1]);

            DatabaseReference day = getDay(month, date[0]);

            DatabaseReference device = getDevice(day, deviceNumber);

            Query query = device.orderByChild("start").equalTo(revenueDeviceData.getStart());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        RevenueDeviceData deviceData;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            deviceData = dataSnapshot.getValue(RevenueDeviceData.class);
                            dataSnapshot.getRef().removeValue();

                            assert deviceData != null;
                            deletePrice(device.child(Common.PRICE), deviceData.getPrice());
                            deleteDuration(device.child(Common.DURATION), deviceData.getTime());

                            deletePrice(day.child(Common.PRICE), deviceData.getPrice());
                            deleteDuration(day.child(Common.DURATION), deviceData.getTime());

                            deletePrice(month.child(Common.PRICE), deviceData.getPrice());
                            deleteDuration(month.child(Common.DURATION), deviceData.getTime());

                            deletePrice(year.child(Common.PRICE), deviceData.getPrice());
                            deleteDuration(year.child(Common.DURATION), deviceData.getTime());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        DatabaseReference year = getYear(context, DateAndTime.getYear());

        year.child(Common.NUMBER).setValue(DateAndTime.getYear());

        DatabaseReference month = getMonth(year, DateAndTime.getMonth());

        month.child(Common.NUMBER).setValue(DateAndTime.getMonth());

        month.child(Common.ARABIC_NAME).setValue(DateAndTime.getArabicNameOfMonth());

        month.child(Common.ENGLISH_NAME).setValue(DateAndTime.getEnglishNameOfMonth());

        DatabaseReference day = getDay(month, DateAndTime.getDay());

        day.child(Common.NUMBER).setValue(DateAndTime.getDay());

        day.child(Common.ARABIC_NAME).setValue(DateAndTime.getArabicNameOfDay());

        day.child(Common.ENGLISH_NAME).setValue(DateAndTime.getEnglishNameOfDay());

        DatabaseReference device = getDevice(day, deviceNumber);

        queryGetStarting(year, month, day, device, deviceNumber, revenueDeviceData, context);
//        } else {
//            try (Database database = new Database(context)) {
//                database.pushItem(DateAndTime.getYear(), DateAndTime.getMonth(), DateAndTime.getDay(), deviceNumber, gson.toJson(revenueDeviceData));
//            } catch (Exception ignored) {
//            }
        //}
    } //End save()

    private static void queryGetStarting(DatabaseReference year, DatabaseReference month, DatabaseReference day, DatabaseReference device, String deviceNumber, RevenueDeviceData revenueDeviceData, Context context) {

        Query query = device.orderByChild("start").equalTo(revenueDeviceData.getStart());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    RevenueDeviceData deviceData;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        deviceData = dataSnapshot.getValue(RevenueDeviceData.class);

                        dataSnapshot.getRef().removeValue();

                        assert deviceData != null;
                        updatePriceInDelete(device.child(Common.PRICE), revenueDeviceData, deviceData.getPrice());
                        updateDurationInDelete(device.child(Common.DURATION), deviceData, revenueDeviceData);

                        // Update day values
                        updatePriceInDelete(day.child(Common.PRICE), revenueDeviceData, deviceData.getPrice());
                        updateDurationInDelete(day.child(Common.DURATION), deviceData, revenueDeviceData);

                        // Update month values
                        updatePriceInDelete(month.child(Common.PRICE), revenueDeviceData, deviceData.getPrice());
                        updateDurationInDelete(month.child(Common.DURATION), deviceData, revenueDeviceData);

                        // Update year values
                        updatePriceInDelete(year.child(Common.PRICE), revenueDeviceData, deviceData.getPrice());
                        updateDurationInDelete(year.child(Common.DURATION), deviceData, revenueDeviceData);
                    }
                } else {

                    // Update device values
                    device.child(Common.NUMBER).setValue(deviceNumber);
                    updatePrice(device.child(Common.PRICE), revenueDeviceData.getPrice());
                    updateDuration(device.child(Common.DURATION), revenueDeviceData);

                    // Update day values
                    updatePrice(day.child(Common.PRICE), revenueDeviceData.getPrice());
                    updateDuration(day.child(Common.DURATION), revenueDeviceData);

                    // Update month values
                    updatePrice(month.child(Common.PRICE), revenueDeviceData.getPrice());
                    updateDuration(month.child(Common.DURATION), revenueDeviceData);

                    // Update year values
                    updatePrice(year.child(Common.PRICE), revenueDeviceData.getPrice());
                    updateDuration(year.child(Common.DURATION), revenueDeviceData);
                }

                // Push revenueDeviceData
                device.push().setValue(revenueDeviceData).addOnSuccessListener(unused -> Toast.makeText(context, context.getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, context.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show());
            } // End query onDataChange()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } //End queryGetStarting()

    private static void updatePrice(DatabaseReference ref, String price) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double currentValue = 0.0;
                if (snapshot.exists()) {
                    currentValue = Double.parseDouble(String.valueOf(snapshot.getValue()));
                }
                double updatedValue = currentValue + Double.parseDouble(price);
                ref.setValue(updatedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    } // End updatePrice()

    private static void updatePriceInDelete(DatabaseReference ref, RevenueDeviceData revenueDeviceData, String price) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double updatedValue = Double.parseDouble(String.valueOf(snapshot.getValue())) - Double.parseDouble(price);
                ref.setValue(updatedValue).addOnSuccessListener(unused -> updatePrice(ref, revenueDeviceData.getPrice()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    } // End updatePriceInDelete()

    private static void updateDuration(DatabaseReference ref, RevenueDeviceData revenueDeviceData) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentValue = "PT0M";
                if (snapshot.exists()) {
                    currentValue = String.valueOf(snapshot.getValue());
                }
                String updatedValue = DateAndTime.durationPlus(currentValue, DateAndTime.timeDifference(languageHandler(revenueDeviceData.getStart()), languageHandler(revenueDeviceData.getEnd())).toString());
                ref.setValue(updatedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    } // End updateDuration()

    private static void updateDurationInDelete(DatabaseReference ref, RevenueDeviceData previousRevenueDeviceData, RevenueDeviceData currentRevenueDeviceData) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String updatedValue = DateAndTime.durationMinus(String.valueOf(snapshot.getValue()), DateAndTime.timeDifference(languageHandler(previousRevenueDeviceData.getStart()), languageHandler(previousRevenueDeviceData.getEnd())).toString());
                ref.setValue(updatedValue).addOnSuccessListener(unused -> updateDuration(ref, currentRevenueDeviceData));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    } // End updateDurationInDelete()

    public static void deletePrice(DatabaseReference ref, String price) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double updatedValue = Double.parseDouble(String.valueOf(snapshot.getValue())) - Double.parseDouble(price);
                ref.setValue(updatedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }// End deletePrice()

    public static void deleteDuration(DatabaseReference ref, String time) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String updatedValue = DateAndTime.durationMinus(String.valueOf(snapshot.getValue()), DateAndTime.convertToDuration(time));
                ref.setValue(updatedValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }// End deleteDuration()

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
    } // End languageHandler()
} //End Firebase