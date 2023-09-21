package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Objects;

import ao.play.freekick.Adapters.FragmentViewPagerAdapter;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.UniqueIdGenerator;
import ao.play.freekick.Dialogs.AskAboutDebtValue;
import ao.play.freekick.Fragments.Debts;
import ao.play.freekick.Fragments.ForHim;
import ao.play.freekick.Fragments.ForYou;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.CustomerDetails;
import ao.play.freekick.R;

public class DebtorData extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_details);

        initializeComponents();
    }

    private void initializeComponents() {
        getWindow().setStatusBarColor(getPrimaryColor());
        hideActionBar();
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

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new ForYou(), getString(R.string.for_you));
        ordersViewPagerAdapter.addFragment(new ForHim(), getString(R.string.for_him));

        viewPager.setAdapter(ordersViewPagerAdapter);
    }

    private void setupTextView() {
        String customerName = Debts.customer.getName();

        // Find both TextViews
        TextView letter = findViewById(R.id.letter);
        TextView name = findViewById(R.id.user_name);

        // Set the first character of the customer name as text for 'letter'
        letter.setText(String.valueOf(customerName.charAt(0)));
        name.setText(customerName);

        // Create a common click listener for both 'letter' and 'name'
        View.OnClickListener clickListener = view -> {
            Intent intent = new Intent(getApplicationContext(), DebtorProfile.class);
            intent.putExtra(Common.CUSTOMER_MANE, customerName);
            intent.putExtra(Common.CUSTOMER_FOR_YOU, Debts.customer.getForYou());
            intent.putExtra(Common.CUSTOMER_FOR_ME, Debts.customer.getForHim());
            startActivity(intent);
        };

        // Assign the common click listener to both 'letter' and 'name'
        letter.setOnClickListener(clickListener);
        name.setOnClickListener(clickListener);
    }

    private void setupAddButton() {
        ImageButton add = findViewById(R.id.add_data);
        add.setOnClickListener(view -> {
            AskAboutDebtValue askAboutDebtValue = new AskAboutDebtValue((debtValue, isForYou) -> {
                String operator = isForYou ? "+" : "-";
                Debts.reference.push().setValue(new CustomerDetails(UniqueIdGenerator.generateUniqueId(), operator, debtValue, DateAndTime.getCurrentTime()));

                if (isForYou) updateForYou(debtValue);
                else updateForHim(debtValue);
            });

            askAboutDebtValue.show(getSupportFragmentManager(), "debtor data dialog");
        });
    }

    private void updateForYou(String currentValue) {
        double value = Double.parseDouble(currentValue) + Double.parseDouble(Debts.customer.getForHim());
        Debts.customer.setForYou(String.valueOf(value));
        Debts.reference.child(Common.FOR_YOU).setValue(String.valueOf(value));
    }

    private void updateForHim(String currentValue) {
        double value = Double.parseDouble(currentValue) + Double.parseDouble(Debts.customer.getForYou());
        Debts.customer.setForHim(String.valueOf(value));
        Debts.reference.child(Common.FOR_HIM).setValue(String.valueOf(value));
    }

    private void onReferenceRemoved() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // This method is called when a new child is added to the reference
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // This method is called when an existing child is changed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (Debts.reference == null) {
                    DebtorData.this.finish();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // This method is called when a child changes position in the list
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // This method is called if there is an error in the operation
            }
        };

        // Add the ChildEventListener to your DatabaseReference
        Debts.reference.addChildEventListener(childEventListener);
    }
}