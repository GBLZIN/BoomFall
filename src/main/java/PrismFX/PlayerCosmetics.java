package PrismFX;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class PlayerCosmetics {

    private static final HashMap<UUID, String> fallCosmetics = new HashMap<>();
    private static final HashMap<UUID, String> projectileCosmetics = new HashMap<>();


    public static void equipCosmetic(Player player, String cosmetico) {
        fallCosmetics.put(player.getUniqueId(), cosmetico);
    }


    public static void equipProjectileCosmetic(Player player, String cosmetico) {
        projectileCosmetics.put(player.getUniqueId(), cosmetico);
    }

    public static void removeCosmetic(Player player) {
        fallCosmetics.remove(player.getUniqueId());
    }

    public static void removeProjectileCosmetic(Player player) {
        projectileCosmetics.remove(player.getUniqueId());
    }

    public static boolean isCosmeticEquipped(Player player, String cosmetico) {
        return fallCosmetics.containsKey(player.getUniqueId()) &&
               fallCosmetics.get(player.getUniqueId()).equals(cosmetico);
    }

    public static boolean isProjectileCosmeticEquipped(Player player, String cosmetico) {
        return projectileCosmetics.containsKey(player.getUniqueId()) &&
               projectileCosmetics.get(player.getUniqueId()).equals(cosmetico);
    }

    public static String getCosmeticEquipped(Player player) {
        return fallCosmetics.get(player.getUniqueId());
    }

    public static String getProjectileCosmeticEquipped(Player player) {
        return projectileCosmetics.get(player.getUniqueId());
    }
}
