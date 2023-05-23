package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.RevenueDeviceData;
import ao.play.freekick.R;

public class DeviceDetailsAdapter extends RecyclerView.Adapter<DeviceDetailsAdapter.ViewHolder> {
    List<RevenueDeviceData> deviceData;
    ViewOnClickListener onClickListener;

    public DeviceDetailsAdapter(List<RevenueDeviceData> deviceData, ViewOnClickListener viewOnClickListener) {
        this.deviceData = deviceData;
        this.onClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_detail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        onClickListener.languageHandler(deviceData.get(position));

        holder.time_num.setText(String.valueOf(position + 1));
        holder.start.setText(deviceData.get(position).getStart());
        holder.end.setText(deviceData.get(position).getEnd());
        holder.state.setText(deviceData.get(position).getState());
        holder.time.setText(deviceData.get(position).getTime());
        holder.price.setText(deviceData.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return deviceData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView time_num, start, end, state, time, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time_num = itemView.findViewById(R.id.time_no);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            state = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.user_time);
            price = itemView.findViewById(R.id.price);
        }
    }
}
