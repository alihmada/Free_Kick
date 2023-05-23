package ao.play.freekick.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ao.play.freekick.R;

public class Password extends DialogFragment {

    private final PasswordDialogListener listener;
    private TextInputEditText passwordEditText;

    public Password(PasswordDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.password_dialog, null);

        passwordEditText = view.findViewById(R.id.password_text);
        Button submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(v -> {
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            listener.onPasswordEntered(password);
            dismiss();
        });

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    public interface PasswordDialogListener {
        void onPasswordEntered(String password);
    }
}