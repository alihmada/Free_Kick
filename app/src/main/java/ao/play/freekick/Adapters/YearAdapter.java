package ao.play.freekick.Adapters;

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
import ao.play.freekick.Models.Year;
import ao.play.freekick.R;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private final ViewOnClickListener viewOnClickListener;
    private final List<Year> years;

    public YearAdapter(List<Year> years, ViewOnClickListener viewOnClickListener) {
        this.viewOnClickListener = viewOnClickListener;
        this.years = years;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.revenue_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Year item = years.get(position);

        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(item.getNumber());
        holder.time.setText(DateAndTime.durationToClockFormat(item.getDuration()));
        holder.price.setText(String.valueOf(item.getPrice()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number, name, time, price;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(name.getText().toString(), null));
        }
    }
}