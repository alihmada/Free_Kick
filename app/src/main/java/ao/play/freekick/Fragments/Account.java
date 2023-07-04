package ao.play.freekick.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ao.play.freekick.Activities.Login;
import ao.play.freekick.Activities.Main;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class Account extends Fragment {
    private SharedPreferences sharedPreferences;
    private TextView userName;
    private TextView phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        sharedPreferences = requireContext().getSharedPreferences(Common.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        userName = view.findViewById(R.id.user_name);
        phoneNumber = view.findViewById(R.id.phone);

        setupUserInfo();
        setupQRCodeButton(view);
        setupLogoutButton(view);

        return view;
    }

    private void setupUserInfo() {
        String storedUserName = sharedPreferences.getString(Common.USER_NAME, "");
        userName.setText(storedUserName);
        phoneNumber.setText(Firebase.getPhoneNumber());
    }

    private void setupQRCodeButton(View view) {
        FloatingActionButton qr = view.findViewById(R.id.qr_code);
        qr.setOnClickListener(v -> Main.createQR(requireContext(), Common.ROOT));
    }

    private void setupLogoutButton(View view) {
        FloatingActionButton logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            sharedPreferences.edit().remove(Common.USER).apply();
            navigateToLogin();
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish();
    }
}