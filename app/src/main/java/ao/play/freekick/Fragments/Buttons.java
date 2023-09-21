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

public class Buttons extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        ConstraintLayout triangle = view.findViewById(R.id.triangle);
        TextView triangle_text = view.findViewById(R.id.triangle_text);
        triangle_text.setText(Controllers.controller.getTriangle().getProblem());
        triangle.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(getString(R.string.ps4_controller_button_triangle), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("triangle")
                        .setValue(new Component(isGood, problem));
                triangle_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout circle = view.findViewById(R.id.circle);
        TextView circle_text = view.findViewById(R.id.circle_text);
        circle_text.setText(Controllers.controller.getCircle().getProblem());
        circle.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(getString(R.string.ps4_controller_button_circle), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("circle")
                        .setValue(new Component(isGood, problem));
                circle_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout cross = view.findViewById(R.id.cross);
        TextView cross_text = view.findViewById(R.id.cross_text);
        cross_text.setText(Controllers.controller.getCross().getProblem());
        cross.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(getString(R.string.ps4_controller_button_cross), (isGood, problem) -> {
                Firebase.getController(requireContext())
                        .child(Controllers.controller.getName())
                        .child("cross")
                        .setValue(new Component(isGood, problem));
                cross_text.setText(problem);
            });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout square = view.findViewById(R.id.square);
        TextView square_text = view.findViewById(R.id.square_text);
        square_text.setText(Controllers.controller.getSquare().getProblem());
        square.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_square),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("square")
                                .setValue(new Component(isGood, problem));
                        square_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout up = view.findViewById(R.id.top_arrow);
        TextView up_text = view.findViewById(R.id.Top_arrow_text);
        up_text.setText(Controllers.controller.getUp().getProblem());
        up.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_dpad_up),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("up")
                                .setValue(new Component(isGood, problem));
                        up_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout right = view.findViewById(R.id.right_arrow);
        TextView right_text = view.findViewById(R.id.right_arrow_text);
        right_text.setText(Controllers.controller.getRight().getProblem());
        right.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_dpad_right),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("right")
                                .setValue(new Component(isGood, problem));
                        right_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout down = view.findViewById(R.id.bottom_arrow);
        TextView down_text = view.findViewById(R.id.bottom_arrow_text);
        down_text.setText(Controllers.controller.getDown().getProblem());
        down.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_dpad_down),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("down")
                                .setValue(new Component(isGood, problem));
                        down_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout left = view.findViewById(R.id.left_arrow);
        TextView left_text = view.findViewById(R.id.left_arrow_text);
        left_text.setText(Controllers.controller.getLeft().getProblem());
        left.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_dpad_left),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("left")
                                .setValue(new Component(isGood, problem));
                        left_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout r1 = view.findViewById(R.id.r1_button);
        TextView r1_text = view.findViewById(R.id.r1_text);
        r1_text.setText(Controllers.controller.getR1().getProblem());
        r1.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_r1),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("r1")
                                .setValue(new Component(isGood, problem));
                        r1_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout r2 = view.findViewById(R.id.r2);
        TextView r2_text = view.findViewById(R.id.r2_text);
        r2_text.setText(Controllers.controller.getR2().getProblem());
        r2.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_r2),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("r2")
                                .setValue(new Component(isGood, problem));
                        r2_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout l1 = view.findViewById(R.id.l1_button);
        TextView l1_text = view.findViewById(R.id.l1_text);
        l1_text.setText(Controllers.controller.getL1().getProblem());
        l1.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_l1),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("l1")
                                .setValue(new Component(isGood, problem));
                        l1_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout l2 = view.findViewById(R.id.l2_button);
        TextView l2_text = view.findViewById(R.id.l2_text);
        l2_text.setText(Controllers.controller.getL2().getProblem());
        l2.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_l2),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("l2")
                                .setValue(new Component(isGood, problem));
                        l2_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout options = view.findViewById(R.id.option);
        TextView options_text = view.findViewById(R.id.option_text);
        options_text.setText(Controllers.controller.getOptions().getProblem());
        options.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_options),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("options")
                                .setValue(new Component(isGood, problem));
                        options_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout share = view.findViewById(R.id.share);
        TextView share_text = view.findViewById(R.id.share_text);
        share_text.setText(Controllers.controller.getShare().getProblem());
        share.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_share),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("share")
                                .setValue(new Component(isGood, problem));
                        share_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        ConstraintLayout ps = view.findViewById(R.id.ps);
        TextView ps_text = view.findViewById(R.id.ps_text);
        ps_text.setText(Controllers.controller.getPs().getProblem());
        ps.setOnClickListener(view1 -> {
            ControllerDialog dialog = new ControllerDialog(
                    getString(R.string.ps4_controller_button_ps),
                    (isGood, problem) -> {
                        Firebase.getController(requireContext())
                                .child(Controllers.controller.getName())
                                .child("ps")
                                .setValue(new Component(isGood, problem));
                        ps_text.setText(problem);
                    });

            dialog.show(getParentFragmentManager(), "");
        });

        return view;
    }
}