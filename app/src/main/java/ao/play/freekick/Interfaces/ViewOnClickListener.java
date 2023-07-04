package ao.play.freekick.Interfaces;

import ao.play.freekick.Models.RevenueDeviceData;

public interface
ViewOnClickListener {
    void onClickListener(String position);

    default void languageHandler(RevenueDeviceData revenueDeviceData) {

    }
}