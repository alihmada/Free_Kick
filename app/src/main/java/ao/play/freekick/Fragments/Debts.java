package ao.play.freekick.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ao.play.freekick.Activities.DebtDetails;
import ao.play.freekick.Adapters.DebtAdapter;
import ao.play.freekick.Classes.UniqueIdGenerator;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Value;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Customer;
import ao.play.freekick.Models.CustomerDetails;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;

public class Debts extends Fragment implements ViewOnClickListener {
    public static List<CustomerDetails> customerDetails;
    public static DatabaseReference ref;
    public static Customer customer;
    private RecyclerView recyclerView;
    private DebtAdapter debtAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        customerDetails = new ArrayList<>();

        recyclerView = view.findViewById(R.id.debt_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        EditText search = view.findViewById(R.id.search_view);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    debtAdapter.getFilter().filter(s);
                } catch (Exception ignored) {
                }
            }
        });

        FloatingActionButton add = view.findViewById(R.id.add_customer);
        add.setOnClickListener(v -> openCustomerDialog());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setRecyclerView();
    }

    private void openCustomerDialog() {
        Value customerName = new Value(name -> {
            Query query = Firebase.getDebt().orderByChild("name").equalTo(name);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        Customer customer = new Customer(UniqueIdGenerator.generateUniqueId(), name, "0", "0");
                        Firebase.getDebt().push().setValue(customer);

                        setRecyclerView();
                    } else {
                        Toast.makeText(requireContext(), requireContext().getString(R.string.user_exist), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        customerName.show(getChildFragmentManager(), "customer_dialog");
    }

    private void setRecyclerView() {
        Firebase.getDebt().addListenerForSingleValueEvent(new ValueEventListener() {
            final List<Customer> customerList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        customerList.add(customer);
                    }
                }

                if (!customerList.isEmpty()) {
                    debtAdapter = new DebtAdapter(customerList, Debts.this);
                    recyclerView.setAdapter(debtAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    public void onClickListener(String id) {
        Query query = Firebase.getDebt().orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    customer = snapshot.getValue(Customer.class);
                    customerDetails.clear();
                    ref = snapshot.getRef();
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                try {
                                    CustomerDetails customer = dataSnapshot.getValue(CustomerDetails.class);
                                    if (customer != null) {
                                        customerDetails.add(customer);
                                    }
                                } catch (Exception ignored) {
                                }
                            }
                            Intent intent = new Intent(getActivity(), DebtDetails.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    @Override
    public void languageHandler(RevenueDeviceData revenueDeviceData) {
        // Handle language changes
    }
}