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
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Device;
import ao.play.freekick.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private final ViewOnClickListener viewOnClickListener;
    private final List<Device> devices;
    private final String[] headers;

    public DeviceAdapter(Context context, List<Device> devices, ViewOnClickListener viewOnClickListener) {
        this.headers = context.getResources().getStringArray(R.array.headers);
        this.viewOnClickListener = viewOnClickListener;
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.revenue_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device item = devices.get(position);

        String number = item.getNumber();
        holder.name.setText(headers[Integer.parseInt(number) - 1]);
        holder.number.setText(number);
        holder.time.setText(DateAndTime.durationToClockFormat(item.getDuration()));
        holder.price.setText(String.valueOf(item.getPrice()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number, name, time, price;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(number.getText().toString(), name.getText().toString()));
        }
    }
}
