package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ao.play.freekick.Activities.Revenue;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.YearAndDevice;
import ao.play.freekick.R;

public class YearsAndDevicesAdapter extends RecyclerView.Adapter<YearsAndDevicesAdapter.ViewHolder> {

    List<YearAndDevice> yearAndDevices;
    ViewOnClickListener viewOnClickListener;

    public YearsAndDevicesAdapter(List<YearAndDevice> yearAndDevices, ViewOnClickListener viewOnClickListener) {
        this.yearAndDevices = yearAndDevices;
        this.viewOnClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_layout, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Revenue.isDevice) {
            String number = yearAndDevices.get(position).getNumber();
            holder.number.setText(number);
            holder.name.setText(HomeAdapter.ViewHolder.headers[Integer.parseInt(number) - 1]);

        } else {
            holder.number.setText(String.valueOf(position + 1));
            holder.name.setText(yearAndDevices.get(position).getNumber());
        }
        holder.time.setText(DateAndTime.durationToClockFormat(yearAndDevices.get(position).getDuration()));
        holder.price.setText(String.valueOf(yearAndDevices.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return yearAndDevices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number, name, time, price;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);

            if (Revenue.isDevice)
                itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(number.getText().toString()));
            else
                itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(name.getText().toString()));
        }
    }
}
