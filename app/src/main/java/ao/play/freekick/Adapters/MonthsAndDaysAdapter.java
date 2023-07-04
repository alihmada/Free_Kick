package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import ao.play.freekick.Classes.DateAndTime;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.MonthAndDay;
import ao.play.freekick.R;

public class MonthsAndDaysAdapter extends RecyclerView.Adapter<MonthsAndDaysAdapter.ViewHolder> {

    private final List<MonthAndDay> monthAndDays;
    private final ViewOnClickListener viewOnClickListener;

    public MonthsAndDaysAdapter(List<MonthAndDay> monthAndDays, ViewOnClickListener viewOnClickListener) {
        this.monthAndDays = monthAndDays;
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
        MonthAndDay item = monthAndDays.get(position);
        holder.number.setText(item.getNumber());
        holder.name.setText(getMonthDisplayName(item));
        holder.time.setText(DateAndTime.durationToClockFormat(item.getDuration()));
        holder.price.setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return monthAndDays.size();
    }

    private String getMonthDisplayName(MonthAndDay item) {
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

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(number.getText().toString()));
        }
    }
}