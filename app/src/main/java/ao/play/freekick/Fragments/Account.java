package ao.play.freekick.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import ao.play.freekick.Activities.Login;
import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Classes.QRConstructor;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.BottomSheetDialog;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Dialogs.Qr;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Models.User;
import ao.play.freekick.R;

public class Account extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private SharedPreferences sharedPreferences;
    private TextView firstName;
    private TextView lastName;
    private TextView phoneNumber;
    private ImageView userImage;

    private Uri uri;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        setupUserInfo();
        return view;
    }

    private void init(View view) {
        try {
            sharedPreferences = requireContext().getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), Context.MODE_PRIVATE);
        } catch (Exception ignored) {
        }
        firstName = view.findViewById(R.id.account_user_first);
        lastName = view.findViewById(R.id.account_user_last);
        phoneNumber = view.findViewById(R.id.account_user_phone);
        userImage = view.findViewById(R.id.account_user_image);
        user = new Gson().fromJson(sharedPreferences.getString(Common.USER_DATA, ""), User.class);

        CardView selectImage = view.findViewById(R.id.account_select_image);
        selectImage.setOnClickListener(view1 -> checkPermission());

        ConstraintLayout editFirstName = view.findViewById(R.id.edit_first_name);
        editFirstName.setOnClickListener(view12 -> setupBottomSheet(firstName, R.string.first_name));

        ConstraintLayout editLastName = view.findViewById(R.id.edit_last_name);
        editLastName.setOnClickListener(view13 -> setupBottomSheet(lastName, R.string.last_name));
    }

    private void setupBottomSheet(TextView textView, int header) {
        BottomSheetDialog dialog = new BottomSheetDialog(header, R.string.enter_new_name, R.string.edit, Common.REGEX_NAME, text -> updateUserInfo(textView, text));
        dialog.show(getParentFragmentManager(), "bottom sheet");
    }

    private void updateUserInfo(TextView textView, String edited) {
        DatabaseReference userReference = Firebase.getUsers(requireContext()).orderByChild("phoneNumber").equalTo(Firebase.getPhoneNumber()).getRef();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User downloadedUser = null;
                    DatabaseReference reference = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        reference = dataSnapshot.getRef();
                        downloadedUser = dataSnapshot.getValue(User.class);
                    }
                    if (downloadedUser != null) {
                        if (textView.getId() == R.id.account_user_first) {
                            user = new User(edited, downloadedUser.getLastName(), downloadedUser.getPhoneNumber(), downloadedUser.isAdmin());
                        } else {
                            user = new User(downloadedUser.getFirstName(), edited, downloadedUser.getPhoneNumber(), downloadedUser.isAdmin());
                        }
                        reference.setValue(user);
                    }

                    sharedPreferences.edit().putString(Common.USER_DATA, new Gson().toJson(user)).apply();
                    textView.setText(edited);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void setupUserInfo() {
        getUserImage();
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(formatPhoneNumber(user.getPhoneNumber()));
    }

    private void getUserImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");

        StorageReference imageRef = storageRef.child(Firebase.getPhoneNumber());
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(userImage)).addOnFailureListener(exception -> {
        });

        if (isAdded())
            userImage.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.profile, requireContext().getTheme()));
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images");

        if (uri != null) {
            StorageReference imageRef = storageRef.child(Firebase.getPhoneNumber());
            UploadTask uploadTask = imageRef.putFile(uri);

            uploadTask.addOnSuccessListener(taskSnapshot -> {

            }).addOnFailureListener(exception -> {

            });
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 2);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        } else {
            pickImagesFromGallery();
        }
    }

    private void pickImagesFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    private String formatPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        if (digitsOnly.startsWith("1")) {
            digitsOnly = digitsOnly.substring(1);
        }

        StringBuilder formattedNumber = new StringBuilder("+");
        for (int i = 0; i < digitsOnly.length(); i++) {
            if (i == 2 || i == 5 || i == 8) {
                formattedNumber.append(" ");
            }
            formattedNumber.append(digitsOnly.charAt(i));
        }

        return formattedNumber.toString();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (user.isAdmin()) {
            inflater.inflate(R.menu.account_menu, menu);
        } else {
            inflater.inflate(R.menu.not_admin, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.shop_id) {
            Qr qr = new Qr(QRConstructor.createQR(Common.getROOT()));
            qr.show(getParentFragmentManager(), "");
            return true;
        } else if (item.getItemId() == R.id.logout) {
            ConfirmationDialog dialog = new ConfirmationDialog(getString(R.string.are_you_sure_logout), new ConfirmationDialog.ConfirmationDialogListener() {
                @Override
                public void onConfirm() {
                    sharedPreferences.edit().remove(Common.USER_DATA).remove(Common.SHOP_ID).apply();
                    navigateToLogin();
                }

                @Override
                public void onCancel() {

                }
            });
            dialog.show(getParentFragmentManager(), "");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && data != null && data.getData() != null) {
            uri = data.getData();
            userImage.setImageURI(data.getData());
            uploadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImagesFromGallery();
            }
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }
}