package ao.play.freekick.Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomerViewModel extends ViewModel {
    private MutableLiveData<Customer> mutableLiveData;

    public void initialize(Context context, String id) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getCustomer(context, id);
    }

    public LiveData<Customer> getProfile() {
        return mutableLiveData;
    }
}
