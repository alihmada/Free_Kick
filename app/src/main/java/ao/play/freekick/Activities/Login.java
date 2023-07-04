package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageButton login = findViewById(R.id.login);
        TextInputEditText phone = findViewById(R.id.mobile_text);

        login.setOnClickListener(view -> {
            String phoneNo = String.valueOf(phone.getText());
            if (phoneNo.matches("^01[0125][0-9]{8}$")) {
                Intent intent = new Intent(this, CodeVerification.class);
                intent.putExtra(Common.PHONE_NUMBER, "+2".concat(phoneNo));
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.invalid_phone_no), Toast.LENGTH_LONG).show();
            }
        });
    }
}
