package ao.play.freekick.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ao.play.freekick.Classes.DiffCallback;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Customer;
import ao.play.freekick.R;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> implements Filterable {

    static List<Customer> filteredCustomers;
    List<Customer> customerList;
    ViewOnClickListener onClickListener;

    public DebtAdapter(List<Customer> customerList, ViewOnClickListener onClickListener) {
        this.customerList = customerList;
        filteredCustomers = new ArrayList<>(customerList);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_customer, parent, false);
        return new DebtAdapter.ViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = filteredCustomers.get(position).getName();

        holder.letter.setText(String.valueOf(name.charAt(0)));
        holder.name.setText(name);
        holder.forMe.setText(filteredCustomers.get(position).getForMe());
        holder.forYou.setText(filteredCustomers.get(position).getForYou());
    }

    @Override
    public int getItemCount() {
        return filteredCustomers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                List<Customer> filteredList = new ArrayList<>();

                for (Customer customer : customerList) {
                    if (customer.getName().toLowerCase().contains(query)) {
                        filteredList.add(customer);
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Customer> filteredList = (List<Customer>) results.values;

                // Calculate the differences between the previous and new filtered lists
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredCustomers, filteredList));

                // Update the filtered customers list
                filteredCustomers.clear();
                filteredCustomers.addAll(filteredList);

                // Dispatch the specific change events to the adapter
                diffResult.dispatchUpdatesTo(DebtAdapter.this);
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView letter, name, forMe, forYou;

        public ViewHolder(@NonNull View itemView, ViewOnClickListener viewOnClickListener) {
            super(itemView);

            letter = itemView.findViewById(R.id.letter);
            name = itemView.findViewById(R.id.debt_name);
            forMe = itemView.findViewById(R.id.for_me);
            forYou = itemView.findViewById(R.id.for_you);

            itemView.setOnClickListener(v -> viewOnClickListener.onClickListener(filteredCustomers.get(getAdapterPosition()).getId()));
        }
    }
}
