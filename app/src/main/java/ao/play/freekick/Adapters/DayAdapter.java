package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import ao.play.freekick.Classes.Animation;
import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Day;
import ao.play.freekick.R;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private final ViewOnClickListener viewOnClickListener;
    private final List<Day> days;

    public DayAdapter(List<Day> days, ViewOnClickListener viewOnClickListener) {
        this.days = days;
        this.viewOnClickListener = viewOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.revenue_row, parent, false);
        return new ViewHolder(view, viewOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Day item = days.get(position);

        holder.number.setText(item.getNumber());
        holder.name.setText(getNameOfDay(item));
        holder.time.setText(DateAndTime.durationToClockFormat(item.getDuration()));
        holder.price.setText(String.valueOf(item.getPrice()));

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    private String getNameOfDay(Day item) {
        if (Locale.getDefault().getLanguage().equals("en")) {
            return item.getEnglish_name();
        } else {
            return item.getArabic_name();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView number, name, time, price;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(number.getText().toString(), null));
        }
    }
}
