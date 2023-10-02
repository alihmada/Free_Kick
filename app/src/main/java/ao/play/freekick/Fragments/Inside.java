package ao.play.freekick.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ControllerDialog;
import ao.play.freekick.Models.Component;
import ao.play.freekick.Models.ControllerViewModel;
import ao.play.freekick.R;

public class Inside extends Fragment {

    private ControllerViewModel model;
    String name;

    public Inside() {
    }

    public Inside(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside, container, false);

        setupViewModel();

        ConstraintLayout battery = view.findViewById(R.id.battery);
        TextView battery_text = view.findViewById(R.id.battery_text);
        battery.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.battery), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("battery").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout innerCable = view.findViewById(R.id.inner);
        TextView innerCable_text = view.findViewById(R.id.inner_text);
        innerCable.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.inner_cable), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("innerCable").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout tap_cable = view.findViewById(R.id.tap);
        TextView tab_cable_text = view.findViewById(R.id.tap_text);
        tap_cable.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.tap_cable), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("tableCable").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout motherboard = view.findViewById(R.id.board);
        TextView motherboard_text = view.findViewById(R.id.board_text);
        motherboard.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.motherboard), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("motherboard").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        model.getController().observe(requireActivity(), controller -> {
            battery_text.setText(controller.getBattery().getProblem());
            innerCable_text.setText(controller.getInnerCable().getProblem());
            tab_cable_text.setText(controller.getTableCable().getProblem());
            motherboard_text.setText(controller.getMotherboard().getProblem());
        });

        return view;
    }

    private void setupViewModel() {
        model = ViewModelProviders.of(this).get(ControllerViewModel.class);
        model.initialize(getContext(), name);
    }

}