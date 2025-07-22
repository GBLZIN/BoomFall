package PrismFX;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ProjectileParticlesMenu implements Listener {

    private static final String MENU_TITLE = "Partículas de Projéteis";
    private static final int SLOT_BACK = 31;

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(player, 36, MENU_TITLE);

        // Cosméticos de Projéteis disponíveis
        menu.setItem(10, buildCosmeticItem(player, Material.SOUL_TORCH, "Chamas Abissais", "§7Faíscas do abismo seguem seu projétil."));
        menu.setItem(12, buildCosmeticItem(player, Material.ENDER_EYE, "Luz Distorcida", "§7Distúrbios mágicos no ar acompanham o projétil."));
        menu.setItem(14, buildCosmeticItem(player, Material.AMETHYST_CLUSTER, "Chuva de Cristais", "§7Partículas prismáticas e folhas caindo."));
        menu.setItem(16, buildCosmeticItem(player, Material.REDSTONE, "Centelha Mecânica", "§7Faíscas e ruídos metálicos intensos."));

        // Botão de voltar
        menu.setItem(SLOT_BACK, createItem(Material.RED_DYE, "§cVoltar", List.of()));
        player.openInventory(menu);
    }

    private static ItemStack buildCosmeticItem(Player player, Material material, String name, String description) {
        boolean equipped = PlayerCosmetics.isProjectileCosmeticEquipped(player, name);
        String status = equipped ? "§cClique para remover" : "§eClique para equipar";

        return createItem(material, getColoredName(name), List.of(description, "§r", status));
    }

    private static String getColoredName(String name) {
        return switch (name) {
            case "Chamas Abissais" -> "§9" + name;
            case "Luz Distorcida" -> "§d" + name;
            case "Chuva de Cristais" -> "§b" + name;
            case "Centelha Mecânica" -> "§6" + name;
            default -> name;
        };
    }

    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (!event.getView().getTitle().equalsIgnoreCase(MENU_TITLE)) return;

        event.setCancelled(true);

        int clickedSlot = event.getSlot();

        if (clickedSlot == SLOT_BACK) {
            player.playSound(player.getLocation(), Sound.BLOCK_ROOTED_DIRT_BREAK, 1, 1);
            CosmeticsMenu.open(player);
            return;
        }

        String cosmetic = getCosmeticFromSlot(clickedSlot);
        if (cosmetic != null) {
            toggleCosmetic(player, cosmetic);
            open(player); // reabrir o menu para atualizar estado
        }
    }

    private String getCosmeticFromSlot(int slot) {
        return switch (slot) {
            case 10 -> "Chamas Abissais";
            case 12 -> "Luz Distorcida";
            case 14 -> "Chuva de Cristais";
            case 16 -> "Centelha Mecânica";
            default -> null;
        };
    }

    private void toggleCosmetic(Player player, String cosmetic) {
        if (PlayerCosmetics.isProjectileCosmeticEquipped(player, cosmetic)) {
            PlayerCosmetics.removeProjectileCosmetic(player);
            player.sendMessage("§b§lPROJÉTIL! §eVocê removeu §a" + cosmetic + "§e.");
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1.2f);
        } else {
            PlayerCosmetics.equipProjectileCosmetic(player, cosmetic);
            player.sendMessage("§b§lPROJÉTIL! §eVocê equipou §a" + cosmetic + "§e.");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
        }
    }
}
