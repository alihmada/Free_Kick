package ao.play.freekick.Interfaces;

import ao.play.freekick.Models.Temporal;

public interface ViewOnClickListener {
    void onClickListener(String id, String name);

    default void languageHandler(Temporal temporal){

    }

    default void openProfile(String id){

    }
}