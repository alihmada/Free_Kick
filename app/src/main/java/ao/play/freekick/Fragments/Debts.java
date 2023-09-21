package ao.play.freekick.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ao.play.freekick.Activities.DebtorData;
import ao.play.freekick.Activities.DebtorProfile;
import ao.play.freekick.Adapters.DebtAdapter;
import ao.play.freekick.Classes.UniqueIdGenerator;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.BottomSheetDialog;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.Customer;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;

public class Debts extends Fragment implements ViewOnClickListener {
    public static DatabaseReference reference;
    public static Customer customer;
    boolean isReversed = false;
    ProgressBar progressBar;
    ConstraintLayout alert;
    ImageView imageView;
    TextView textView;
    EditText search;
    private List<Customer> customerList;
    private List<Customer> reversedList;
    private RecyclerView recyclerView;
    private DebtAdapter debtAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        customerList = new ArrayList<>();

        alert = view.findViewById(R.id.alert);

        imageView = view.findViewById(R.id.alert_image);
        textView = view.findViewById(R.id.alert_text);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.debt_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        setupView();

        search = view.findViewById(R.id.search_view);
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

        return view;
    }

    private void setupView() {
        if (Internet.isConnectedWithoutMessage(requireContext())) {
            if (Internet.isNetworkLimited(requireContext())) {
                setupWifi(getString(R.string.internet_limited));
            } else {
                alert.setVisibility(View.GONE);
                setRecyclerView();
            }
        } else if (customerList.size() == 0) {
            setupWifi(getString(R.string.no_internet));
        }
    }

    private void setupWifi(String message) {
        alert.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.wifi_off));
        textView.setText(message);
    }

    private void setRecyclerView() {
        Firebase.getDebt(requireContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerList.clear();
                progressBar.setVisibility(View.VISIBLE);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        customerList.add(customer);
                    }
                }

                if (!customerList.isEmpty()) {
                    reversedList = new ArrayList<>(customerList);
                    if (isReversed) {
                        changeSorting();
                    } else {
                        recyclerView.setAdapter(new DebtAdapter(customerList, Debts.this));
                    }
                    search.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    alert.setVisibility(View.GONE);
                } else {
                    recyclerView.setAdapter(new DebtAdapter(customerList, Debts.this));
                    alert.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    search.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void changeSorting() {
        if (reversedList != null) {
            Collections.reverse(reversedList);

            isReversed = !customerList.get(0).getId().equals(reversedList.get(0).getId());

            debtAdapter = new DebtAdapter(reversedList, Debts.this);
            recyclerView.setAdapter(debtAdapter);
        }
    }

    private void setupBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(R.string.debtor_name, R.string.customer_name, R.string.save, Common.REGEX_NAME, this::addNewCustomerInfo);
        dialog.show(getParentFragmentManager(), "bottom sheet");
    }

    private void addNewCustomerInfo(String name) {
        Query query = Firebase.getDebt(requireContext()).orderByChild("name").equalTo(name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Customer customer = new Customer(UniqueIdGenerator.generateUniqueId(), name, "0", "0");
                    Firebase.getDebt(requireContext()).push().setValue(customer);
                } else {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.user_exist), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dept_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_new) {
            setupBottomSheet();
            return true;
        } else if (item.getItemId() == R.id.sort) {
            changeSorting();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickListener(String id) {
        Query getReference = Firebase.getDebt(requireContext()).orderByChild("id").equalTo(id);

        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    customer = snapshot.getValue(Customer.class);
                    reference = snapshot.getRef();

                    Intent intent = new Intent(getActivity(), DebtorData.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    @Override
    public void openProfile(String id) {
        Query getReference = Firebase.getDebt(requireContext()).orderByChild("id").equalTo(id);

        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    customer = snapshot.getValue(Customer.class);
                    reference = snapshot.getRef();

                    Intent intent = new Intent(getActivity(), DebtorProfile.class);
                    intent.putExtra(Common.CUSTOMER_MANE, customer.getName());
                    intent.putExtra(Common.CUSTOMER_FOR_YOU, customer.getForYou());
                    intent.putExtra(Common.CUSTOMER_FOR_ME, customer.getForHim());
                    startActivity(intent);
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