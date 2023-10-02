package ao.play.freekick.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ao.play.freekick.Classes.Animation;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Temporal;
import ao.play.freekick.R;

public class TemporalAdapter extends RecyclerView.Adapter<TemporalAdapter.ViewHolder> {
    private final Context context;
    private final List<Temporal> deviceData;
    private final ViewOnClickListener onClickListener;
    private final String[] date;

    public TemporalAdapter(Context context, List<Temporal> deviceData, ViewOnClickListener viewOnClickListener, String[] date) {
        this.context = context;
        this.deviceData = deviceData;
        this.onClickListener = viewOnClickListener;
        this.date = date;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_detail_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Temporal item = deviceData.get(position);

        onClickListener.languageHandler(item);

        holder.timeNum.setText(String.valueOf(position + 1));
        holder.start.setText(item.getStart());
        holder.end.setText(item.getEnd());
        holder.state.setText(item.getState());
        holder.time.setText(item.getTime());
        holder.price.setText(item.getPrice());

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return deviceData.size();
    }

    public void removeItem(int position) {
        DatabaseReference year = Firebase.getYear(context, date[0]);
        DatabaseReference month = Firebase.getMonth(year, date[1]);
        DatabaseReference day = Firebase.getDay(month, date[2]);
        DatabaseReference device = Firebase.getDevice(day, date[3]);

        Query query = device.orderByChild("start").equalTo(deviceData.get(position).getStart());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            final DatabaseReference year = Firebase.getYear(context, date[0]);
            final DatabaseReference month = Firebase.getMonth(year, date[1]);
            final DatabaseReference day = Firebase.getDay(month, date[2]);
            final DatabaseReference device = Firebase.getDevice(day, date[3]);
            Temporal temporal;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    temporal = dataSnapshot.getValue(Temporal.class);

                    dataSnapshot.getRef().removeValue();

                    Firebase.deletePrice(device.child(Common.PRICE), temporal.getPrice());
                    Firebase.deleteDuration(device.child(Common.DURATION), temporal.getTime());

                    Firebase.deletePrice(day.child(Common.PRICE), temporal.getPrice());
                    Firebase.deleteDuration(day.child(Common.DURATION), temporal.getTime());

                    Firebase.deletePrice(month.child(Common.PRICE), temporal.getPrice());
                    Firebase.deleteDuration(month.child(Common.DURATION), temporal.getTime());

                    Firebase.deletePrice(year.child(Common.PRICE), temporal.getPrice());
                    Firebase.deleteDuration(year.child(Common.DURATION), temporal.getTime());
                }

                deviceData.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeNum, start, end, state, time, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeNum = itemView.findViewById(R.id.time_no);
            start = itemView.findViewById(R.id.start);
            end = itemView.findViewById(R.id.end);
            state = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.user_time);
            price = itemView.findViewById(R.id.price);

            itemView.setOnLongClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_delete_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.delete_debt) {
                        ConfirmationDialog dialog = new ConfirmationDialog(itemView.getContext().getString(R.string.are_you_sure_delete), new ConfirmationDialog.ConfirmationDialogListener() {
                            @Override
                            public void onConfirm() {
                                removeItem(getAdapterPosition());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        dialog.show(dialog.getChildFragmentManager(), "");
                    }
                    return false;
                });
                return false;
            });

        }
    }
}