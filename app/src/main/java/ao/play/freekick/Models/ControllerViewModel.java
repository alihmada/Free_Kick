package ao.play.freekick.Models;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ControllerViewModel extends ViewModel {
    private MutableLiveData<Controller> mutableLiveData;

    public void initialize(Context context, String name) {
        if (mutableLiveData != null) {
            return;
        }
        Repository repository = Repository.getInstance();
        mutableLiveData = repository.getController(context, name);
    }

    public LiveData<Controller> getController() {
        return mutableLiveData;
    }
}