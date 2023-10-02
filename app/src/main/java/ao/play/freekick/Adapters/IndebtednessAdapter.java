package ao.play.freekick.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import ao.play.freekick.Classes.Animation;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Models.Indebtedness;
import ao.play.freekick.R;

public class IndebtednessAdapter extends RecyclerView.Adapter<IndebtednessAdapter.ViewHolder> {
    String id;
    Context context;
    FragmentManager manager;
    DatabaseReference reference;
    List<Indebtedness> indebtednessList;

    public IndebtednessAdapter(String id, Context context, FragmentManager manager, List<Indebtedness> indebtednessList) {
        this.id = id;
        this.context = context;
        this.manager = manager;
        this.indebtednessList = indebtednessList;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_customer_details_row, parent, false);
        return new IndebtednessAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.letter.setText(indebtednessList.get(position).getLetter());
        holder.value.setText(indebtednessList.get(position).getValue());
        holder.time.setText(indebtednessList.get(position).getTime());

        Animation.startAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return indebtednessList.size();
    }

    public void removeItem(int position) {

        String id = indebtednessList.get(position).getId();

        Query query = Firebase.getDebt(context).orderByChild("id").equalTo(this.id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        reference = dataSnapshot.getRef();
                        Query query1 = reference.orderByChild("id").equalTo(id);

                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        dataSnapshot1.getRef().removeValue();

                                        if (indebtednessList.get(position).getLetter().equals("-")) {
                                            updateForHim(indebtednessList.get(position).getValue());
                                        } else {
                                            updateForYou(indebtednessList.get(position).getValue());
                                        }
                                    }

                                    indebtednessList.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateForHim(String value) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        String newValue = String.valueOf(Double.parseDouble(Objects.requireNonNull(snapshot.child(Common.FOR_HIM).getValue(String.class))) - Double.parseDouble(value));
                        snapshot.getRef().child(Common.FOR_HIM).setValue(newValue);
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateForYou(String value) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String newValue = String.valueOf(Double.parseDouble(Objects.requireNonNull(snapshot.child(Common.FOR_YOU).getValue(String.class))) - Double.parseDouble(value));
                    snapshot.getRef().child(Common.FOR_YOU).setValue(newValue);
                }
            }

        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });

}

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView letter, value, time;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        letter = itemView.findViewById(R.id.debt_details_letter);
        value = itemView.findViewById(R.id.debt_details_value);
        time = itemView.findViewById(R.id.debt_details_time);

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
                    dialog.show(manager, "");
                }
                return false;
            });
            return false;
        });
    }
}
}
