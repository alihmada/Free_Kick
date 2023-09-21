package ao.play.freekick.Interfaces;

import android.widget.EditText;

import ao.play.freekick.Classes.Device;

public interface ViewListener {
    void languageHandler(Device device, int position);

    void popTimePicker(EditText textInputEditText);

    void vibration(int effect);
}
