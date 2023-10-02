package ao.play.freekick.Models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ao.play.freekick.Data.Firebase;

public class Repository {
    private static Repository instance;

    public static Repository getInstance() {
        if (instance == null)
            instance = new Repository();
        return instance;
    }

    public MutableLiveData<Customer> getCustomer(Context  context, String id) {
        MutableLiveData<Customer> mutableLiveData = new MutableLiveData<>();
        Query getReference = Firebase.getDebt(context).orderByChild("id").equalTo(id);

        getReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        mutableLiveData.setValue(customer);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });

        return mutableLiveData;
    }

    public MutableLiveData<Controller> getController(Context  context, String name) {
        MutableLiveData<Controller> mutableLiveData = new MutableLiveData<>();
        Firebase.getController(context).child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Controller controller = snapshot.getValue(Controller.class);
                    mutableLiveData.setValue(controller);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mutableLiveData;
    }
}
