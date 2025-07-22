package PrismFX;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class FallParticlesMenu implements Listener {

    private static final String MENU_TITLE = "Partículas de Queda";
    private static final int SLOT_BACK = 31;

    public static void open(Player player) {
        Inventory menu = Bukkit.createInventory(player, 36, MENU_TITLE);

        // Cosméticos de Queda disponíveis
        menu.setItem(10, buildCosmeticItem(player, Material.AMETHYST_CLUSTER, "Desintegração Violeta", "§7Partículas que desintegram o ar ao cair."));
        menu.setItem(12, buildCosmeticItem(player, Material.LIGHTNING_ROD, "Desintegração Azul Elétrica", "§7Partículas elétricas ao cair."));
        menu.setItem(14, buildCosmeticItem(player, Material.FIRE_CHARGE, "Desintegração Vermelha Incandescente", "§7Partículas de fogo ao cair."));
        menu.setItem(16, buildCosmeticItem(player, Material.SLIME_BALL, "Desintegração Verde Radiante", "§7Partículas verdes ao cair."));

        // Botão de voltar
        menu.setItem(SLOT_BACK, createItem(Material.RED_DYE, "§cVoltar", List.of()));

        player.openInventory(menu);
    }

    private static ItemStack buildCosmeticItem(Player player, Material material, String name, String description) {
        boolean equipped = PlayerCosmetics.isCosmeticEquipped(player, name);
        String status = equipped ? "§cClique para remover" : "§eClique para equipar";
        return createItem(material, getCosmeticNameWithGradient(name), List.of(description, "", status));
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
            open(player);
        }
    }

    private String getCosmeticFromSlot(int slot) {
        return switch (slot) {
            case 10 -> "Desintegração Violeta";
            case 12 -> "Desintegração Azul Elétrica";
            case 14 -> "Desintegração Vermelha Incandescente";
            case 16 -> "Desintegração Verde Radiante";
            default -> null;
        };
    }

    private void toggleCosmetic(Player player, String cosmetic) {
        if (PlayerCosmetics.isCosmeticEquipped(player, cosmetic)) {
            PlayerCosmetics.removeCosmetic(player);
            player.sendMessage("§b§lPARTÍCULA! §eVocê removeu §a" + cosmetic + "§e.");
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1.2f);
        } else {
            PlayerCosmetics.equipCosmetic(player, cosmetic);
            player.sendMessage("§b§lPARTÍCULA! §eVocê equipou §a" + cosmetic + "§e.");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        String equipped = PlayerCosmetics.getCosmeticEquipped(player);
        if (equipped != null) {
            Color color = getColorForCosmetic(equipped);
            if (color != null) {
                spawnParticleEffect(player, color);
            }
        }
    }

    private Color getColorForCosmetic(String cosmetic) {
        return switch (cosmetic) {
            case "Desintegração Violeta" -> Color.fromRGB(128, 0, 255);
            case "Desintegração Azul Elétrica" -> Color.fromRGB(0, 0, 255);
            case "Desintegração Vermelha Incandescente" -> Color.fromRGB(255, 0, 0);
            case "Desintegração Verde Radiante" -> Color.fromRGB(0, 255, 0);
            default -> null;
        };
    }

    private void spawnParticleEffect(Player player, Color color) {
        final double maxRadius = 1.8;
        final int steps = 5;

        new BukkitRunnable() {
            int step = 1;

            @Override
            public void run() {
                if (step > steps || !player.isOnline()) {
                    cancel();
                    return;
                }

                double radius = (step / (double) steps) * maxRadius;
                var baseLoc = player.getLocation().add(0, 0.1, 0);

                for (int i = 0; i < 30; i++) {
                    double angle = 2 * Math.PI * i / 30;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    player.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            baseLoc.clone().add(x, 0, z),
                            0,
                            new DustOptions(color, 1.2f)
                    );
                }

                step++;
            }
        }.runTaskTimer(PrismFXPlugin.getInstance(), 0L, 2L);
    }

    @SuppressWarnings("deprecation")
    public static String getCosmeticNameWithGradient(String cosmetic) {
        return switch (cosmetic) {
            case "Desintegração Violeta" -> ChatColor.of("#8000FF") + cosmetic;
            case "Desintegração Azul Elétrica" -> ChatColor.of("#0000FF") + cosmetic;
            case "Desintegração Vermelha Incandescente" -> ChatColor.of("#FF0000") + cosmetic;
            case "Desintegração Verde Radiante" -> ChatColor.of("#00FF00") + cosmetic;
            default -> ChatColor.WHITE + cosmetic;
        };
    }
}
