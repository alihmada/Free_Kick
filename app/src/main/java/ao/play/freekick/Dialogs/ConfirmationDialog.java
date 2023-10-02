package ao.play.freekick.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ao.play.freekick.R;

public class ConfirmationDialog extends DialogFragment {

    private ConfirmationDialogListener listener;
    private String message;

    public ConfirmationDialog() {
    }

    public ConfirmationDialog(String message, ConfirmationDialogListener listener) {
        this.message = message;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);

        try {
            initialize(dialog);
        } catch (Exception e){
            dismiss();
        }

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initialize(Dialog dialog) {
        TextView messageTextView = dialog.findViewById(R.id.confirmation_text);
        messageTextView.setText(message);

        Button confirmButton = dialog.findViewById(R.id.yes);
        Button cancelButton = dialog.findViewById(R.id.no);

        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm();
            }
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancel();
            }
            dialog.dismiss();
        });
    }

    public interface ConfirmationDialogListener {
        void onConfirm();

        void onCancel();
    }
}
