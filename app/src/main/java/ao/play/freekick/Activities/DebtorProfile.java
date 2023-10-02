package ao.play.freekick.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ao.play.freekick.Classes.Common;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.BottomSheetDialog;
import ao.play.freekick.Models.CustomerViewModel;
import ao.play.freekick.R;

public class DebtorProfile extends AppCompatActivity {
    private CustomerViewModel model;
    private TextView image;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_profile);

        hideActionBar();

        setupViewModel();
        initializeComponents();
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void setupViewModel() {
        id = Objects.requireNonNull(getIntent().getStringExtra(Common.CUSTOMER_ID));
        model = ViewModelProviders.of(this).get(CustomerViewModel.class);
        model.initialize(this, id);
    }

    private void initializeComponents() {
        ConstraintLayout editCustomerName = findViewById(R.id.edit_customer_name);
        editCustomerName.setOnClickListener(view -> setupBottomSheet());

        TextView forHim = findViewById(R.id.customer_for_him);

        TextView forYou = findViewById(R.id.customer_for_you);

        model.getProfile().observe(this, customer -> {
            String name = customer.getName().trim();

            image = findViewById(R.id.image);
            image.setText(String.valueOf(name.charAt(0)));

            TextView customerName = findViewById(R.id.customer_name);
            customerName.setText(name);
            forHim.setText(customer.getForHim());
            forYou.setText(customer.getForYou());
        });

        Button delete = findViewById(R.id.delete_account);
        delete.setOnClickListener(view -> {
            Query getReference = Firebase.getDebt(this).orderByChild("id").equalTo(id);

            getReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            finish();
        });
    }

    private void setupBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(R.string.debtor_name, R.string.enter_new_name, R.string.edit, Common.REGEX_NAME, this::updateCustomerName);
        dialog.show(getSupportFragmentManager(), "bottom sheet");
    }

    private void updateCustomerName(String name) {
        Query getReference = Firebase.getDebt(this).orderByChild("id").equalTo(id);

        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().child("name").setValue(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}