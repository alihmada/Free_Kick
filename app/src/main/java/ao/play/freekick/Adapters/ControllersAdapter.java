package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Controller;
import ao.play.freekick.R;

public class ControllersAdapter extends RecyclerView.Adapter<ControllersAdapter.ViewHolder> {

    private final List<Controller> controllerList;

    public ControllersAdapter(List<Controller> controllerList) {
        this.controllerList = controllerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Controller controller = controllerList.get(position);

        holder.id.setText(controller.getId());
        holder.timeOfLastRepair.setText(controller.getTimeOfLastRepair());
        holder.body.setText(controller.getBody());
        holder.rightAnalog.setText(controller.getRightAnalog());
        holder.leftAnalog.setText(controller.getLeftAnalog());
        holder.buttons.setText(controller.getXo());
        holder.stock.setText(controller.getStock());
        holder.option.setText(controller.getOption());
        holder.share.setText(controller.getShare());
        holder.table.setText(controller.getTable());
        holder.r1r2.setText(controller.getR1r2());
        holder.l1l2.setText(controller.getL1l2());
        holder.socket.setText(controller.getSocket());
        holder.battery.setText(controller.getBattery());
        holder.innerCable.setText(controller.getInnerCable());
        holder.motherboard.setText(controller.getMotherboard());
    }

    @Override
    public int getItemCount() {
        return controllerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button save, edit;
        TextInputEditText id, timeOfLastRepair, body, rightAnalog, leftAnalog, buttons, stock, option, share, table, r1r2, l1l2, socket, battery, innerCable, motherboard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.controller_id);
            timeOfLastRepair = itemView.findViewById(R.id.controller_last_time);
            body = itemView.findViewById(R.id.controller_body);
            rightAnalog = itemView.findViewById(R.id.controller_right_analog);
            leftAnalog = itemView.findViewById(R.id.controller_left_analog);
            buttons = itemView.findViewById(R.id.controller_buttons);
            stock = itemView.findViewById(R.id.controller_stock);
            option = itemView.findViewById(R.id.controller_option);
            share = itemView.findViewById(R.id.controller_share);
            table = itemView.findViewById(R.id.controller_table);
            r1r2 = itemView.findViewById(R.id.controller_r1r2);
            l1l2 = itemView.findViewById(R.id.controller_l1l2);
            socket = itemView.findViewById(R.id.controller_socket);
            battery = itemView.findViewById(R.id.controller_battery);
            innerCable = itemView.findViewById(R.id.controller_innercable);
            motherboard = itemView.findViewById(R.id.controller_motherboard);
            save = itemView.findViewById(R.id.controller_save);
            edit = itemView.findViewById(R.id.controller_edit);

            TextInputEditText[] inputLayouts = {timeOfLastRepair, body, rightAnalog, leftAnalog, buttons, stock, option, share, table, r1r2, l1l2, socket, battery, innerCable, motherboard};

            timeOfLastRepair.setOnLongClickListener(v -> {
                timeOfLastRepair.setText(DateAndTime.timeFormatter(DateAndTime.getLocalTime()));
                return false;
            });

            save.setOnClickListener(v -> {
                List<String> text = new ArrayList<>();
                for (TextInputEditText inputLayout : inputLayouts) {
                    inputLayout.setClickable(false);
                    inputLayout.setEnabled(false);
                    text.add(Objects.requireNonNull(inputLayout.getText()).toString());
                }
                edit.setEnabled(true);
                save.setEnabled(false);
                Firebase.getController().child(String.format("controller%s", id.getText())).setValue(new Controller(Objects.requireNonNull(id.getText()).toString(), text.get(0), text.get(1), text.get(2), text.get(3), text.get(4), text.get(5), text.get(6), text.get(7), text.get(8), text.get(9), text.get(10), text.get(11), text.get(12), text.get(13), text.get(14)));
            });

            edit.setOnClickListener(v -> {
                save.setEnabled(true);
                edit.setEnabled(false);
                for (TextInputEditText inputLayout : inputLayouts) {
                    inputLayout.setClickable(true);
                    inputLayout.setEnabled(true);
                }
            });
        }
    }
}