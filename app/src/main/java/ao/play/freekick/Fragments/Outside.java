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

public class Outside extends Fragment {
    private ControllerViewModel model;
    String name;

    public Outside() {
    }

    public Outside(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outside, container, false);

        setupViewModel();

        ConstraintLayout body = view.findViewById(R.id.body);
        TextView body_text = view.findViewById(R.id.body_text);
        body.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.body), (isGood, problem) -> {
                Firebase.getController(requireContext()).child(name).child("body").setValue(new Component(isGood, problem));
                body_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout touchPad = view.findViewById(R.id.pad);
        TextView touchPad_text = view.findViewById(R.id.pad_text);
        touchPad.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_touchpad), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("touchPad").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout socket = view.findViewById(R.id.socket);
        TextView socket_text = view.findViewById(R.id.socket_text);
        socket.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.socket), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("socket").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout right_analog = view.findViewById(R.id.right_analog);
        TextView right_analog_text = view.findViewById(R.id.right_analog_text);
        right_analog.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.right_analog), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("rightAnalog").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout left_analog = view.findViewById(R.id.left_analog);
        TextView left_analog_text = view.findViewById(R.id.left_analog_text);
        left_analog.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.left_analog), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("leftAnalog").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        model.getController().observe(requireActivity(), controller -> {
            body_text.setText(controller.getBody().getProblem());
            touchPad_text.setText(controller.getTouchPad().getProblem());
            socket_text.setText(controller.getSocket().getProblem());
            right_analog_text.setText(controller.getRightAnalog().getProblem());
            left_analog_text.setText(controller.getLeftAnalog().getProblem());
        });


        return view;
    }

    private void setupViewModel() {
        model = ViewModelProviders.of(this).get(ControllerViewModel.class);
        model.initialize(getContext(), name);
    }

}