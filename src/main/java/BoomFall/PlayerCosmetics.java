package BoomFall;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerCosmetics {

    private static final Map<Player, String> cosmetics = new HashMap<>(); // Armazena o cosmético de queda ativo de cada jogador

    // Equipar cosmético de queda
    public static void equipCosmetic(Player player, String cosmetico) {
        cosmetics.put(player, cosmetico);
    }

    // Remover cosmético de queda
    public static void removeCosmetic(Player player) {
        cosmetics.remove(player);  // Remove o cosmético ativo do jogador
    }

    // Checar se o jogador tem um cosmético de queda ativo
    public static boolean isCosmeticEquipped(Player player, String cosmetico) {
        return cosmetics.containsKey(player) && cosmetics.get(player).equals(cosmetico);
    }

    // Obter o cosmético ativo do jogador
    public static String getCosmeticEquipped(Player player) {
        return cosmetics.get(player);
    }
}
