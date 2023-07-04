package ao.play.freekick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class CodeVerification extends AppCompatActivity {
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    TextInputEditText code;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageButton otp = findViewById(R.id.otp);

        code = findViewById(R.id.code);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("ar");

        String phone = getIntent().getStringExtra(Common.PHONE_NUMBER);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();

                if (code != null) {
                    codeVerification(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(CodeVerification.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                CodeVerification.this.verificationId = verificationId;
                CodeVerification.this.token = token;
            }
        };

        if (!phone.equals("")) {
            sendVerificationCode(phone);
        }

        otp.setOnClickListener(v -> {
            String inputCode = String.valueOf(code.getText());
            if (!inputCode.equals(""))
                codeVerification(inputCode);
        });
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                try {
                    getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(Common.USER, String.valueOf(Firebase.getCurrentUser()))
                            .apply();
                } catch (Exception ignored) {
                }

                Intent intent = new Intent(getBaseContext(), ShopID.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void codeVerification(String code) {
        signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, code));
    }

}