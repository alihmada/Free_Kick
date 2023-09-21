package ao.play.freekick.Interfaces;

import ao.play.freekick.Models.RevenueDeviceData;

public interface
ViewOnClickListener {
    void onClickListener(String id);

    void openProfile(String id);

    void languageHandler(RevenueDeviceData revenueDeviceData);
}