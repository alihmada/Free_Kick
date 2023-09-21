package ao.play.freekick.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import ao.play.freekick.R;

public class BottomSheetDialog extends DialogFragment {
    private BottomSheetListener listener;
    private int headerId, hintId, buttonId;
    private String regex;
    private Vibrator vibrator;
    private EditText text;

    public BottomSheetDialog() {
    }

    public BottomSheetDialog(int headerId, int hintId, int buttonId, String regex, BottomSheetListener listener) {
        this.headerId = headerId;
        this.hintId = hintId;
        this.buttonId = buttonId;
        this.regex = regex;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.account_bottom_sheet);

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
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
        setupHeader(dialog);
        setupEditText(dialog);
        setupButton(dialog);
    }

    private void setupHeader(Dialog dialog) {
        TextView header = dialog.findViewById(R.id.bottom_sheet_header);
        header.setText(getString(headerId));
    }

    private void setupEditText(Dialog dialog) {
        text = dialog.findViewById(R.id.bottom_sheet_edit_text);
        text.setHint(getString(hintId));
    }

    private void setupButton(Dialog dialog) {
        Button button = dialog.findViewById(R.id.bottom_sheet_edit_btn);
        button.setText(getString(buttonId));

        button.setOnClickListener(view -> {
            String edited = String.valueOf(text.getText());
            if (!edited.isEmpty() && validateAndSetValue(edited)) {
                listener.onDataEntered(edited);
                dialog.dismiss();
            } else {
                text.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
                vibrate(0);
            }
        });
    }

    private boolean validateAndSetValue(String edited) {
        return edited.matches(regex);
    }

    public void vibrate(int effect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effect));
        }
    }

    public interface BottomSheetListener {
        void onDataEntered(String text);
    }
}
