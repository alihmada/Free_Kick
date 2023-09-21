package ao.play.freekick.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ControllerDialog;
import ao.play.freekick.Models.Component;
import ao.play.freekick.R;

public class Inside extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside, container, false);

        ConstraintLayout battery = view.findViewById(R.id.battery);
        TextView battery_text = view.findViewById(R.id.battery_text);
        battery_text.setText(Controllers.controller.getBattery().getProblem());
        battery.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.battery),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("battery")
                                .setValue(new Component(isGood, problem));
                        battery_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout innerCable = view.findViewById(R.id.inner);
        TextView innerCable_text = view.findViewById(R.id.inner_text);
        innerCable_text.setText(Controllers.controller.getInnerCable().getProblem());
        innerCable.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.inner_cable),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("innerCable")
                                .setValue(new Component(isGood, problem));
                        innerCable_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout tap_cable = view.findViewById(R.id.tap);
        TextView tab_cable_text = view.findViewById(R.id.tap_text);
        tab_cable_text.setText(Controllers.controller.getTableCable().getProblem());
        tap_cable.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.tap_cable),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("tableCable")
                                .setValue(new Component(isGood, problem));
                        tab_cable_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout motherboard = view.findViewById(R.id.board);
        TextView motherboard_text = view.findViewById(R.id.board_text);
        motherboard_text.setText(Controllers.controller.getMotherboard().getProblem());
        motherboard.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.motherboard),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("motherboard")
                                .setValue(new Component(isGood, problem));
                        motherboard_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        return view;
    }
}