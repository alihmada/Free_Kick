package ao.play.freekick.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ao.play.freekick.R;

public class Password extends DialogFragment {

    ConstraintLayout parent;
    boolean isFocus, isError;
    private PasswordDialogListener listener;
    private EditText passwordEditText;
    private CheckBox rememberMe;

    public Password() {
    }

    public Password(PasswordDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_dialog);

        initialize(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initialize(Dialog dialog) {
        parent = dialog.findViewById(R.id.parent);
        rememberMe = dialog.findViewById(R.id.remember_me);

        setupEditText(dialog);
        setupShowPassword(dialog);
        setupButton(dialog);
    }

    private void setupEditText(Dialog dialog) {
        passwordEditText = dialog.findViewById(R.id.password_text);

        passwordEditText.setOnFocusChangeListener((view, isFocus) -> {
            this.isFocus = isFocus;
            if (isFocus) {
                if (!isError) changeBackground(R.drawable.blue_stroke_with_2dp_width);
                else changeBackground(R.drawable.red_stroke_with_2dp_width);
            } else {
                changeBackground(R.drawable.input_filed);
            }
        });
    }

    private void setupShowPassword(Dialog dialog) {
        CheckBox showPassword = dialog.findViewById(R.id.show_password);

        showPassword.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            try {
                int selectionStart = passwordEditText.getSelectionStart();
                int selectionEnd = passwordEditText.getSelectionEnd();

                if (isChecked) passwordEditText.setTransformationMethod(null);
                else passwordEditText.setTransformationMethod(new PasswordTransformationMethod());

                passwordEditText.setSelection(selectionStart, selectionEnd);
            } catch (Exception ignored) {
            }
        });
    }

    private void setupButton(Dialog dialog) {
        Button submit = dialog.findViewById(R.id.submit_button);
        submit.setOnClickListener(view -> {
            String password = String.valueOf(passwordEditText.getText());
            if (!Objects.equals(password, "")) {
                listener.onPasswordEntered(password, rememberMe.isChecked());
                dismiss();
            } else {
                isError = true;
                if (isFocus) changeBackground(R.drawable.red_stroke_with_2dp_width);
                else changeBackground(R.drawable.red_stroke_with_1dp_width);
            }
        });
    }

    private void changeBackground(int stroke) {
        parent.setBackground(AppCompatResources.getDrawable(requireContext(), stroke));
    }

    public interface PasswordDialogListener {
        void onPasswordEntered(String password, boolean isRememberMe);
    }
}