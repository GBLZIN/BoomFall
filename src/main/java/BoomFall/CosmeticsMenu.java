package BoomFall;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CosmeticsMenu implements Listener {


    @SuppressWarnings("deprecation")
    public static void open(Player player) {

        Inventory menu = Bukkit.createInventory(player, 36, "Cosméticos");

        // Item (Cosmético) de queda
        ItemStack queda = createItem(Material.FEATHER, "§aQueda",
                List.of("§8§l▎ §8Cosmético", "§r", "§7Seleciona uma partícula para ser", "§7exibida quando receber dano de queda."));
        menu.setItem(10, queda);

        // Item (Cosmético) em breve
        ItemStack emBreve = createItem(Material.GRAY_DYE, "§bEm breve...",
                List.of("§8§l▎ §8Cosmético"));
        for (int slot : new int[]{12, 14, 16}) {
            menu.setItem(slot, emBreve);
        }

        // Item (Sair)
        ItemStack sair = createItem(Material.RED_DYE, "§cSair", List.of());
        menu.setItem(31, sair);

        player.openInventory(menu);
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
    @SuppressWarnings("deprecation")
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (event.getView().getTitle().equals("Cosméticos")) {
            event.setCancelled(true);

            // Redirecionar o jogador para o menu de cosméticos de queda
            if (event.getSlot() == 10) {
                FallParticlesMenu.open(player);
            }

            // Voltar ao menu de cosméticos
            else if (event.getSlot() == 31) {
                player.closeInventory();
            }
        }
    }
}
