package ao.play.freekick.Classes;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ao.play.freekick.Models.Customer;

public class DiffCallback extends DiffUtil.Callback {
    private final List<Customer> oldList;
    private final List<Customer> newList;

    public DiffCallback(List<Customer> oldList, List<Customer> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Customer oldCustomer = oldList.get(oldItemPosition);
        Customer newCustomer = newList.get(newItemPosition);
        // Compare unique identifiers of the items (e.g., customer IDs)
        return oldCustomer.getName().equals(newCustomer.getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Customer oldCustomer = oldList.get(oldItemPosition);
        Customer newCustomer = newList.get(newItemPosition);
        // Compare the content of the items
        return oldCustomer.equals(newCustomer);
    }
}
