package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.ConfirmationDialog;
import ao.play.freekick.Fragments.Debts;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.Customer;
import ao.play.freekick.Models.CustomerDetails;
import ao.play.freekick.R;

public class DebtDetailsAdapter extends RecyclerView.Adapter<DebtDetailsAdapter.ViewHolder> {
    List<CustomerDetails> customerDetails;

    public DebtDetailsAdapter(List<CustomerDetails> customerDetails) {
        this.customerDetails = customerDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_customer_details, parent, false);
        return new DebtDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.letter.setText(customerDetails.get(position).getLetter());
        holder.value.setText(customerDetails.get(position).getValue());
        holder.time.setText(customerDetails.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return customerDetails.size();
    }

    public void removeItem(int position) {

        Query query = Debts.ref.orderByChild("id").equalTo(customerDetails.get(position).getId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.getRef().removeValue();

                    if (customerDetails.get(position).getLetter().equals("-")) {
                        updateForYou(customerDetails.get(position).getValue(), Debts.customer.getId());
                    } else {
                        updateForMe(customerDetails.get(position).getValue(), Debts.customer.getId());
                    }
                }

                customerDetails.remove(position);
                notifyItemRemoved(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateForMe(String value, String id) {
        Query query = Firebase.getDebt().orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    assert customer != null;
                    double newValue = Double.parseDouble(customer.getForMe()) - Double.parseDouble(value);
                    Debts.ref.child(Common.FOR_ME).setValue(String.valueOf(newValue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateForYou(String value, String id) {
        Query query = Firebase.getDebt().orderByChild("id").equalTo(id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    assert customer != null;
                    double newValue = Double.parseDouble(customer.getForYou()) - Double.parseDouble(value);
                    Debts.ref.child(Common.FOR_YOU).setValue(String.valueOf(newValue));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        ConfirmationDialog.show(itemView.getContext(), itemView.getContext().getString(R.string.are_you_sure_delete), new ConfirmationDialog.ConfirmationDialogListener() {
                            @Override
                            public void onConfirm() {
                                removeItem(getAdapterPosition());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                    return false;
                });
                return false;
            });
        }
    }
}
