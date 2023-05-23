package ao.play.freekick.Interfaces;

import com.google.android.material.textfield.TextInputEditText;

import ao.play.freekick.Classes.Device;

public interface ViewListener {
    void languageHandler(Device device, int position);

    void popTimePicker(TextInputEditText textInputEditText);

    void vibration(int effect);
}
