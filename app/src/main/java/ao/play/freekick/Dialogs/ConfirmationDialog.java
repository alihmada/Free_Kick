package ao.play.freekick.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ao.play.freekick.R;

public class ConfirmationDialog {

    public static void show(Context context, String message, final ConfirmationDialogListener listener) {
        // Create a custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(null);

        // Set the message
        TextView messageTextView = dialog.findViewById(R.id.confirmation_text);
        messageTextView.setText(message);

        // Set the button click listeners
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

        // Show the dialog
        dialog.show();
    }

    public interface ConfirmationDialogListener {
        void onConfirm();

        void onCancel();
    }
}
