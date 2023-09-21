package ao.play.freekick.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.User;
import ao.play.freekick.R;

public class ShopID extends AppCompatActivity {
    ImageView userImage;
    SharedPreferences sharedPreferences;
    EditText firstName;
    EditText lastName;
    Uri uri;
    String id;
    private EditText shopId;
    private final ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            shopId.setText(result.getContents());
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_id);

        hideActionBar();

        Loading.progressDialogConstructor(this);

        try {
            sharedPreferences = getSharedPreferences(EncryptionAndDecryption.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        userImage = findViewById(R.id.user_image);

        shopId = findViewById(R.id.shop_id);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);

        CardView selectImage = findViewById(R.id.select_image);
        selectImage.setOnClickListener(view -> checkPermission());

        Button main = findViewById(R.id.getMain);
        ImageButton scanner = findViewById(R.id.qr_scanner);

        main.setOnClickListener(v -> {
            Loading.showProgressDialog();

            String _1stName = String.valueOf(firstName.getText());
            String _2ndName = String.valueOf(lastName.getText());

            id = String.valueOf(shopId.getText());

            try {
                id = EncryptionAndDecryption.decrypt(id);
            } catch (Exception e) {
                showToast(getString(R.string.error_in_inputs));
                return;
            }


            if (_1stName.isEmpty() || _2ndName.isEmpty() || id.isEmpty()) {
                showToast(getString(R.string.error_in_inputs));
                Loading.dismissProgressDialog();
                return;
            }

            Query query = Firebase.getInstance().getReference(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        Common.setROOT(id);

                        Query userQuery = Firebase.getUsers(ShopID.this).orderByChild("phoneNumber").equalTo(Firebase.getPhoneNumber());

                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = null;
                                if (!snapshot.exists()) {
                                    user = new User(_1stName, _2ndName, Firebase.getPhoneNumber(), false);
                                    Firebase.getUsers(ShopID.this).push().setValue(user);
                                } else {

                                    DatabaseReference reference = null;
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        reference = dataSnapshot.getRef();
                                        user = dataSnapshot.getValue(User.class);
                                    }

                                    if (user != null) {
                                        reference.setValue(new User(_1stName, _2ndName, Firebase.getPhoneNumber(), user.isAdmin()));
                                    }
                                }
                                sharedPreferences.edit().putString(Common.USER_DATA, new Gson().toJson(user)).putString(Common.SHOP_ID, id).apply();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if (userImage.getDrawable() != null) {
                            uploadImage();
                        }

                        navigateToMainScreen();
                    } else {
                        showToast("🙂");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if necessary
                }
            });
        });

        scanner.setOnClickListener(v -> {
            ScanOptions scanOptions = new ScanOptions().setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);

            scanOptionsActivityResultLauncher.launch(scanOptions);
        });
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");

        if (uri != null) {
            StorageReference imageRef = storageRef.child(Firebase.getPhoneNumber());
            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> sharedPreferences.edit().putString(Common.IMAGE, uri.toString()).apply())).addOnFailureListener(exception -> {

            });
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            pickImagesFromGallery();
        }
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void hideActionBar() {
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, Main.class);
        Loading.dismissProgressDialog();
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            uri = data.getData();
            userImage.setImageURI(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImagesFromGallery();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}