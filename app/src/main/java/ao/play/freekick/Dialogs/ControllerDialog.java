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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Fragments.Controllers;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class ControllerDialog extends DialogFragment {
    ConstraintLayout good, bad;
    CheckBox goodCheckbox, badCheckbox;
    private ControllerListener listener;
    private EditText problem;
    private Vibrator vibrator;
    private String headerText;
    private boolean isChecked;

    public ControllerDialog() {
    }

    public ControllerDialog(String headerText, ControllerListener listener) {
        this.headerText = headerText;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.controller_dialog);

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
        setupGoodOption(dialog);
        setupBadOption(dialog);
        setupEditText(dialog);
        setupButton(dialog);
    }

    private void setupHeader(Dialog dialog) {
        TextView header = dialog.findViewById(R.id.component_header);
        header.setText(headerText);
    }

    private void setupGoodOption(Dialog dialog) {
        good = dialog.findViewById(R.id.good);
        goodCheckbox = dialog.findViewById(R.id.good_checkbox);

        goodCheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) problem.setVisibility(View.GONE);
        });

        good.setOnClickListener(v -> setOptionSelected(good, goodCheckbox, bad, badCheckbox));
    }

    private void setupBadOption(Dialog dialog) {
        bad = dialog.findViewById(R.id.bad);
        badCheckbox = dialog.findViewById(R.id.bad_checkbox);

        badCheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) problem.setVisibility(View.VISIBLE);
        });

        bad.setOnClickListener(v -> setOptionSelected(bad, badCheckbox, good, goodCheckbox));
    }

    private void setupEditText(Dialog dialog) {
        problem = dialog.findViewById(R.id.component_problem);
    }

    private void setupButton(Dialog dialog) {
        Button button = dialog.findViewById(R.id.save_component_status);

        button.setOnClickListener(view -> {
            if (isChecked) {
                boolean isGood = goodCheckbox.isChecked();
                if (isGood) {
                    listener.onDataEntered(true, "");
                } else {
                    String problem = this.problem.getText().toString();
                    if (validateAndSetValue(problem)) {
                        listener.onDataEntered(false, problem);
                    } else {
                        this.problem.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
                        vibrate(0);
                        return;
                    }
                }
                Firebase.getController(requireContext()).child(Controllers.controller.getName()).child("timeOfLastRepair").setValue(DateAndTime.getCurrentTime());
                dialog.dismiss();
            } else {
                handleInputErrors();
                vibrate(0);
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

    private boolean validateAndSetValue(String edited) {
        return edited.matches(Common.REGEX_ERROR);
    }

    private void handleInputErrors() {
        good.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
        bad.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error));
    }

    public void vibrate(int effect) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effect));
        }
    }

    public interface ControllerListener {
        void onDataEntered(boolean isGood, String problem);
    }
}
