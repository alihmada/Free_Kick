package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ao.play.freekick.Adapters.FragmentViewPagerAdapter;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.UniqueIdGenerator;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.AskAboutDebtValue;
import ao.play.freekick.Fragments.ForHim;
import ao.play.freekick.Fragments.ForYou;
import ao.play.freekick.Models.CustomerViewModel;
import ao.play.freekick.Models.Indebtedness;
import ao.play.freekick.R;

public class DebtorData extends AppCompatActivity {
    private CustomerViewModel model;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_details);

        initializeComponents();
    }

    private void initializeComponents() {
        getWindow().setStatusBarColor(getPrimaryColor());
        hideActionBar();
        setupViewModel();
        setupViewPager();
        setupTextView();
        setupAddButton();
        onReferenceRemoved();
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private void setupViewModel() {
        id = Objects.requireNonNull(getIntent().getExtras()).getString(Common.CUSTOMER_ID);
        model = ViewModelProviders.of(this).get(CustomerViewModel.class);
        model.initialize(this, id);
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new ForYou(id), getString(R.string.for_you));
        ordersViewPagerAdapter.addFragment(new ForHim(id), getString(R.string.for_him));

        viewPager.setAdapter(ordersViewPagerAdapter);
    }

    private void setupTextView() {
        TextView letter = findViewById(R.id.letter);
        TextView name = findViewById(R.id.user_name);

        model.getProfile().observe(this, customer -> {
            String customerName = customer.getName();
            letter.setText(String.valueOf(customerName.charAt(0)));
            name.setText(customerName);
        });

        View.OnClickListener clickListener = view -> {
            Intent intent = new Intent(getApplicationContext(), DebtorProfile.class);
            intent.putExtra(Common.CUSTOMER_ID, id);
            startActivity(intent);
        };

        letter.setOnClickListener(clickListener);
        name.setOnClickListener(clickListener);
    }

    private void setupAddButton() {
        ImageButton add = findViewById(R.id.add_data);
        add.setOnClickListener(view -> {
            AskAboutDebtValue askAboutDebtValue = new AskAboutDebtValue(this::pushData);

            askAboutDebtValue.show(getSupportFragmentManager(), "debtor data dialog");
        });
    }

    private void pushData(String debtValue, boolean isForYou) {
        Query getReference = Firebase.getDebt(this).orderByChild("id").equalTo(id);

        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DatabaseReference reference = dataSnapshot.getRef();
                        reference.push().setValue(new Indebtedness(UniqueIdGenerator.generateUniqueId(), isForYou ? "+" : "-", debtValue, DateAndTime.getCurrentTime()));
                        if (isForYou)
                            updateForYou(reference, debtValue, dataSnapshot.child(Common.FOR_YOU).getValue(String.class));
                        else
                            updateForHim(reference, debtValue, dataSnapshot.child(Common.FOR_HIM).getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    private void updateForYou(DatabaseReference reference, String currentValue, String previousValue) {
        String value = String.valueOf(Double.parseDouble(currentValue) + Double.parseDouble(previousValue));
        reference.child(Common.FOR_YOU).setValue(value);
    }

    private void updateForHim(DatabaseReference reference, String currentValue, String previousValue) {
        String value = String.valueOf(Double.parseDouble(currentValue) + Double.parseDouble(previousValue));
        reference.child(Common.FOR_HIM).setValue(value);
    }

    private void onReferenceRemoved() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Query getReference = Firebase.getDebt(this).orderByChild("id").equalTo(id);
        getReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dataSnapshot.getRef().addValueEventListener(valueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}