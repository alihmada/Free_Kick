package ao.play.freekick.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.play.freekick.Classes.Animation;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Controller;
import ao.play.freekick.R;

public class ControllerAdapter extends RecyclerView.Adapter<ControllerAdapter.ViewHolder> {

    final List<Controller> controllerList;
    private final ViewOnClickListener onClickListener;
    private final Context context;

    public ControllerAdapter(Context context, List<Controller> controllerList, ViewOnClickListener viewOnClickListener) {
        this.context = context;
        this.controllerList = controllerList;
        this.onClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_controller_row, parent, false);
        return new ViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = controllerList.get(position).getId();
        holder.id.setText(id);
        holder.name.setText(String.format("%s %s", context.getString(R.string.controller_no), id));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return controllerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, name;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            id = itemView.findViewById(R.id.letter);
            name = itemView.findViewById(R.id.debt_name);

            itemView.setOnClickListener(view -> viewOnClickListener.onClickListener(controllerList.get(getAdapterPosition()).getName(), null));
        }
    }
}