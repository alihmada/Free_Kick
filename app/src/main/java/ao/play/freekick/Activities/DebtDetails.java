package ao.play.freekick.Activities;

import static ao.play.freekick.Fragments.Debts.customerDetails;

import android.os.Bundle;
import android.widget.Button;
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
import java.util.Objects;

import ao.play.freekick.Adapters.DebtDetailsAdapter;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Classes.UniqueIdGenerator;
import ao.play.freekick.Dialogs.Value;
import ao.play.freekick.Fragments.Debts;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.CustomerDetails;
import ao.play.freekick.R;

public class DebtDetails extends AppCompatActivity {

    DebtDetailsAdapter debtDetailsAdapter;
    List<CustomerDetails> customerDetailsList;
    private TextView name;
    private TextView forMe;
    private TextView forYou;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_details);

        customerDetailsList = new ArrayList<>();

        setCustomActionBar();

        recyclerView = findViewById(R.id.debt_details_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        debtDetailsAdapter = new DebtDetailsAdapter(customerDetails);
        recyclerView.setAdapter(debtDetailsAdapter);

        name = findViewById(R.id.dept_details_name);
        forMe = findViewById(R.id.dept_details_for_me);
        forYou = findViewById(R.id.dept_details_for_you);

        updateCustomerDetails();

        Button btnForMe = findViewById(R.id.for_me);
        btnForMe.setOnClickListener(v -> openValueDialog(true));

        Button btnForYou = findViewById(R.id.for_you);
        btnForYou.setOnClickListener(v -> openValueDialog(false));
    }

    private void setCustomActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
    }

    private void updateCustomerDetails() {
        name.setText(Debts.customer.getName());
        forMe.setText(Debts.customer.getForMe());
        forYou.setText(Debts.customer.getForYou());
    }

    private void openValueDialog(boolean forMe) {
        Value value = new Value(value1 -> {
            String operator = forMe ? "+" : "-";
            CustomerDetails customerDetails = new CustomerDetails(UniqueIdGenerator.generateUniqueId(),
                    operator,
                    value1,
                    DateAndTime.getCurrentTime());
            Debts.ref.push().setValue(customerDetails);

            if (forMe) {
                updateForMe(value1);
            } else {
                updateForYou(value1);
            }

            Debts.ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    customerDetailsList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            CustomerDetails customer = dataSnapshot.getValue(CustomerDetails.class);
                            if (customer != null) {
                                customerDetailsList.add(customer);
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    recyclerView.setAdapter(new DebtDetailsAdapter(customerDetailsList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        value.show(getSupportFragmentManager(), "value_dialog");
    }

    private void updateForMe(String currentValue) {
        double value = Double.parseDouble(currentValue) + Double.parseDouble(Debts.customer.getForMe());
        Debts.ref.child(Common.FOR_ME).setValue(String.valueOf(value));
        forMe.setText(String.valueOf(value));
    }

    private void updateForYou(String currentValue) {
        double value = Double.parseDouble(currentValue) + Double.parseDouble(Debts.customer.getForYou());
        Debts.ref.child(Common.FOR_YOU).setValue(String.valueOf(value));
        forYou.setText(String.valueOf(value));
    }
}