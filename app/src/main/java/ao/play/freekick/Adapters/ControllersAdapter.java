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

    List<Controller> controllerList;

    public ControllersAdapter(List<Controller> controllerList) {
        this.controllerList = controllerList;
    } // End of ControllersAdapter()

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.controller_row, parent, false);
        return new ViewHolder(view);
    } // End of onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText(controllerList.get(position).getId());
        holder.timeOfLastRepair.setText(controllerList.get(position).getTimeOfLastRepair());
        holder.body.setText(controllerList.get(position).getBody());
        holder.rightAnalog.setText(controllerList.get(position).getRightAnalog());
        holder.leftAnalog.setText(controllerList.get(position).getLeftAnalog());
        holder.buttons.setText(controllerList.get(position).getXo());
        holder.stock.setText(controllerList.get(position).getStock());
        holder.option.setText(controllerList.get(position).getOption());
        holder.share.setText(controllerList.get(position).getShare());
        holder.table.setText(controllerList.get(position).getTable());
        holder.r1r2.setText(controllerList.get(position).getR1r2());
        holder.l1l2.setText(controllerList.get(position).getL1l2());
        holder.socket.setText(controllerList.get(position).getSocket());
        holder.battery.setText(controllerList.get(position).getBattery());
        holder.innerCable.setText(controllerList.get(position).getInnerCable());
        holder.motherboard.setText(controllerList.get(position).getMotherboard());
    } // End of onBindViewHolder()

    @Override
    public int getItemCount() {
        return controllerList.size();
    } // End of getItemCount()

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
        } // End of ViewHolder()
    } // End of ViewHolder

} // End of ControllersAdapter
