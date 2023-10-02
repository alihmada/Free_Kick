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

public class Buttons extends Fragment {

    private ControllerViewModel model;
    private String name;

    public Buttons() {
    }

    public Buttons(String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        setupViewModel();

        ConstraintLayout triangle = view.findViewById(R.id.triangle);
        TextView triangle_text = view.findViewById(R.id.triangle_text);
        triangle.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_triangle), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("triangle").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout circle = view.findViewById(R.id.circle);
        TextView circle_text = view.findViewById(R.id.circle_text);
        circle.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_circle), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("circle").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout cross = view.findViewById(R.id.cross);
        TextView cross_text = view.findViewById(R.id.cross_text);
        cross.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_cross), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("cross").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout square = view.findViewById(R.id.square);

        TextView square_text = view.findViewById(R.id.square_text);
        square.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_square), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("square").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout up = view.findViewById(R.id.top_arrow);
        TextView up_text = view.findViewById(R.id.Top_arrow_text);
        up.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_dpad_up), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("up").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout right = view.findViewById(R.id.right_arrow);
        TextView right_text = view.findViewById(R.id.right_arrow_text);
        right.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_dpad_right), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("right").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout down = view.findViewById(R.id.bottom_arrow);
        TextView down_text = view.findViewById(R.id.bottom_arrow_text);
        down.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_dpad_down), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("down").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout left = view.findViewById(R.id.left_arrow);
        TextView left_text = view.findViewById(R.id.left_arrow_text);
        left.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_dpad_left), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("left").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout r1 = view.findViewById(R.id.r1_button);
        TextView r1_text = view.findViewById(R.id.r1_text);
        r1.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_r1), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("r1").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout r2 = view.findViewById(R.id.r2);
        TextView r2_text = view.findViewById(R.id.r2_text);
        r2.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_r2), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("r2").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout l1 = view.findViewById(R.id.l1_button);
        TextView l1_text = view.findViewById(R.id.l1_text);
        l1.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_l1), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("l1").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout l2 = view.findViewById(R.id.l2_button);
        TextView l2_text = view.findViewById(R.id.l2_text);
        l2.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_l2), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("l2").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout options = view.findViewById(R.id.option);
        TextView options_text = view.findViewById(R.id.option_text);
        options.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_options), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("options").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout share = view.findViewById(R.id.share);
        TextView share_text = view.findViewById(R.id.share_text);
        share.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_share), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("share").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout ps = view.findViewById(R.id.ps);
        TextView ps_text = view.findViewById(R.id.ps_text);

        ps.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(name, getString(R.string.ps4_controller_button_ps), (isGood, problem) -> Firebase.getController(requireContext()).child(name).child("ps").setValue(new Component(isGood, problem)));

            dialog.show(getParentFragmentManager(), "");
        });

        model.getController().observe(requireActivity(), controller -> {
            triangle_text.setText(controller.getTriangle().getProblem());
            circle_text.setText(controller.getCircle().getProblem());
            cross_text.setText(controller.getCross().getProblem());
            square_text.setText(controller.getSquare().getProblem());
            up_text.setText(controller.getUp().getProblem());
            right_text.setText(controller.getRight().getProblem());
            down_text.setText(controller.getDown().getProblem());
            left_text.setText(controller.getLeft().getProblem());
            r1_text.setText(controller.getR1().getProblem());
            r2_text.setText(controller.getR2().getProblem());
            l1_text.setText(controller.getL1().getProblem());
            l2_text.setText(controller.getL2().getProblem());
            options_text.setText(controller.getOptions().getProblem());
            share_text.setText(controller.getShare().getProblem());
            ps_text.setText(controller.getPs().getProblem());
        });
        return view;
    }

    private void setupViewModel() {
        model = ViewModelProviders.of(this).get(ControllerViewModel.class);
        model.initialize(getContext(), name);
    }
}