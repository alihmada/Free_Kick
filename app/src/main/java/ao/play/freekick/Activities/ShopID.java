package ao.play.freekick.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class ShopID extends AppCompatActivity {

    private TextInputEditText shopId;
    private final ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    shopId.setText(result.getContents());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_id);

        hideActionBar();

        SharedPreferences sharedPreferences = getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        TextInputEditText userName = findViewById(R.id.user_name_input);
        shopId = findViewById(R.id.shop_id);
        ImageButton main = findViewById(R.id.getMain);
        ImageButton scanner = findViewById(R.id.qr_scanner);

        main.setOnClickListener(v -> {
            String name = String.valueOf(userName.getText());
            String id = String.valueOf(shopId.getText());

            if (name.isEmpty() || id.isEmpty()) {
                showToast(getString(R.string.error_in_inputs));
                return;
            }

            sharedPreferences.edit().putString(Common.USER_NAME, name).apply();
            sharedPreferences.edit().putString(Common.SHOP_ID, id).apply();

            try {
                Common.ROOT = EncryptionAndDecryption.decrypt(id);
            } catch (Exception e) {
                showToast(getString(R.string.error_in_inputs));
                return;
            }

            Firebase.getInstance().getReference(Common.ROOT).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        navigateToMainScreen();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if necessary
                }
            });
        });

        scanner.setOnClickListener(v -> {
            ScanOptions scanOptions = new ScanOptions()
                    .setBeepEnabled(true)
                    .setOrientationLocked(true)
                    .setCaptureActivity(Capture.class);

            scanOptionsActivityResultLauncher.launch(scanOptions);
        });
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}