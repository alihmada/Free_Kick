package ao.play.freekick.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Objects;

import ao.play.freekick.Dialogs.BottomSheetDialog;
import ao.play.freekick.Fragments.Debts;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class DebtorProfile extends AppCompatActivity {
    TextView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_profile);

        hideActionBar();

        initializeComponents();
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void initializeComponents() {
        ConstraintLayout editCustomerName = findViewById(R.id.edit_customer_name);
        editCustomerName.setOnClickListener(view -> setupBottomSheet());

        String name = getIntent().getStringExtra(Common.CUSTOMER_MANE);

        if (name != null) {
            image = findViewById(R.id.image);
            image.setText(String.valueOf(name.charAt(0)));

            TextView customerName = findViewById(R.id.customer_name);
            customerName.setText(name);
        }

        String forMeValue = getIntent().getStringExtra(Common.CUSTOMER_FOR_ME);
        TextView forMe = findViewById(R.id.customer_for_me);
        forMe.setText(forMeValue);

        String forYouValue = getIntent().getStringExtra(Common.CUSTOMER_FOR_YOU);
        TextView forYou = findViewById(R.id.customer_for_you);
        forYou.setText(forYouValue);

        Button delete = findViewById(R.id.delete_account);
        delete.setOnClickListener(view -> {
            Debts.reference.removeValue();
            Debts.reference = null;
            finish();
        });
    }

    private void setupBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(R.string.debtor_name, R.string.enter_new_name, R.string.edit, Common.REGEX_NAME, text -> updateCustomerInfo(findViewById(R.id.customer_name), text));
        dialog.show(getSupportFragmentManager(), "bottom sheet");
    }

    private void updateCustomerInfo(TextView textView, String text) {
        image.setText(String.valueOf(text.charAt(0)));
        textView.setText(text);
        Debts.reference.child(Common.NAME).setValue(text);
    }
}