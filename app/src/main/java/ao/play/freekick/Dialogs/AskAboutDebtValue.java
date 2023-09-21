package ao.play.freekick.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.debt_details_dialog, null);

        main(view);

        builder.setView(view);

        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    private void main(View view) {
        initializeViews(view);
        setupForHimOption(view);
        setupForYouOption(view);
        setupSaveButton(view);
    }

    private void initializeViews(View view) {
        debtValue = view.findViewById(R.id.value_of_debt);
    }

    private void setupForYouOption(View view) {
        forYouParent = view.findViewById(R.id.for_you_parent);
        forYou = view.findViewById(R.id.for_you_checkbox);

        forYouParent.setOnClickListener(v -> setOptionSelected(forYouParent, forYou, forHimParent, forHim));
    }

    private void setupForHimOption(View view) {
        forHimParent = view.findViewById(R.id.for_him_parent);
        forHim = view.findViewById(R.id.for_him_checkbox);

        forHimParent.setOnClickListener(v -> setOptionSelected(forHimParent, forHim, forYouParent, forYou));
    }

    private void setupSaveButton(View view) {
        Button save = view.findViewById(R.id.save);

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
        selectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.green_stroke));

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
