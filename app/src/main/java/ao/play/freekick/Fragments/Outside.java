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

public class Outside extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outside, container, false);

        ConstraintLayout body = view.findViewById(R.id.body);
        TextView body_text = view.findViewById(R.id.body_text);
        body_text.setText(Controllers.controller.getBody().getProblem());
        body.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(getString(R.string.body), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("body")
                        .setValue(new Component(isGood, problem));
                body_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout touchPad = view.findViewById(R.id.pad);
        TextView touchPad_text = view.findViewById(R.id.pad_text);
        touchPad_text.setText(Controllers.controller.getTouchPad().getProblem());
        touchPad.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_touchpad), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("touchPad")
                        .setValue(new Component(isGood, problem));
                touchPad_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout socket = view.findViewById(R.id.socket);
        TextView socket_text = view.findViewById(R.id.socket_text);
        socket_text.setText(Controllers.controller.getSocket().getProblem());
        socket.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.socket), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("socket")
                        .setValue(new Component(isGood, problem));
                socket_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout right_analog = view.findViewById(R.id.right_analog);
        TextView right_analog_text = view.findViewById(R.id.right_analog_text);
        right_analog_text.setText(Controllers.controller.getRightAnalog().getProblem());
        right_analog.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.right_analog), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("rightAnalog")
                        .setValue(new Component(isGood, problem));
                right_analog_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout left_analog = view.findViewById(R.id.left_analog);
        TextView left_analog_text = view.findViewById(R.id.left_analog_text);
        left_analog_text.setText(Controllers.controller.getLeftAnalog().getProblem());
        left_analog.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.left_analog), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("leftAnalog")
                        .setValue(new Component(isGood, problem));
                left_analog_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        return view;
    }
}