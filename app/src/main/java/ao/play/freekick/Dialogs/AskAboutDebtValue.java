package ao.play.freekick.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ao.play.freekick.R;

public class AskAboutDebtValue extends DialogFragment {

    ConstraintLayout forHimParent, forYouParent;
    CheckBox forHim, forYou;
    EditText debtValue;
    boolean isChecked;
    private AskAboutValueListener valueListener;

    public AskAboutDebtValue() {
    }

    public AskAboutDebtValue(AskAboutValueListener valueListener) {
        this.valueListener = valueListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.debt_details_dialog);

        main(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void main(Dialog dialog) {
        initializeViews(dialog);
        setupForHimOption(dialog);
        setupForYouOption(dialog);
        setupSaveButton(dialog);
    }

    private void initializeViews(Dialog dialog) {
        debtValue = dialog.findViewById(R.id.value_of_debt);
    }

    private void setupForYouOption(Dialog dialog) {
        forYouParent = dialog.findViewById(R.id.for_you_parent);
        forYou = dialog.findViewById(R.id.for_you_checkbox);

        forYouParent.setOnClickListener(v -> setOptionSelected(forYouParent, forYou, forHimParent, forHim));
    }

    private void setupForHimOption(Dialog dialog) {
        forHimParent = dialog.findViewById(R.id.for_him_parent);
        forHim = dialog.findViewById(R.id.for_him_checkbox);

        forHimParent.setOnClickListener(v -> setOptionSelected(forHimParent, forHim, forYouParent, forYou));
    }

    private void setupSaveButton(Dialog dialog) {
        Button save = dialog.findViewById(R.id.save);

        save.setOnClickListener(view1 -> {
            String debtValue = String.valueOf(this.debtValue.getText());
            if (isValidInput(debtValue)) {
                valueListener.onDataEntered(debtValue, forYou.isChecked());
                this.debtValue.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
                dismiss();
            } else {
                handleInputErrors();
            }
        });
    }

    private void setOptionSelected(ConstraintLayout selectedLayout, CheckBox selectedCheckBox, ConstraintLayout deselectedLayout, CheckBox deselectedCheckBox) {
        selectedCheckBox.setChecked(true);
        selectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.blue_stroke_with_2dp_width));

        deselectedCheckBox.setChecked(false);
        deselectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.gray_stroke));

        isChecked = true;
    }

    private boolean isValidInput(String debtValue) {
        return debtValue.matches("[0-9]+") && isChecked;
    }

    private void handleInputErrors() {
        if (!debtValue.getText().toString().matches("[0-9]+")) {
            debtValue.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
        } else {
            debtValue.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
        }

        if (!isChecked) {
            forHimParent.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
            forYouParent.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
        }

        Toast.makeText(requireContext(), requireContext().getString(R.string.check_for_data), Toast.LENGTH_SHORT).show();
    }

    public interface AskAboutValueListener {
        void onDataEntered(String debtValue, boolean isForYou);
    }
}
