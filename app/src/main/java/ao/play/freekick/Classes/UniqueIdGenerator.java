package ao.play.freekick.Classes;

import java.util.UUID;

public class UniqueIdGenerator {
    public static String generateUniqueId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
