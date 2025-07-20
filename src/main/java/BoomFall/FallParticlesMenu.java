package BoomFall;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;


import java.util.List;

public class FallParticlesMenu implements Listener {

    @SuppressWarnings("deprecation")
    public static void open(Player player) {

        Inventory menu = Bukkit.createInventory(player, 36, "Partículas de Queda");

        // Criando os itens em suas devidas posições do menu
        menu.setItem(10, createItem(Material.AMETHYST_CLUSTER, getCosmeticNameWithGradient("Desintegração Violeta"),
                List.of("§7Partículas que desintegram o ar ao cair.", "",
                        PlayerCosmetics.isCosmeticEquipped(player, "Desintegração Violeta") ? "§cClique para remover" : "§eClique para equipar")));

        menu.setItem(12, createItem(Material.LIGHTNING_ROD, getCosmeticNameWithGradient("Desintegração Azul Elétrica"),
                List.of("§7Partículas elétricas ao cair.", "",
                        PlayerCosmetics.isCosmeticEquipped(player, "Desintegração Azul Elétrica") ? "§cClique para remover" : "§eClique para equipar")));

        menu.setItem(14, createItem(Material.FIRE_CHARGE, getCosmeticNameWithGradient("Desintegração Vermelha Incandescente"),
                List.of("§7Partículas de fogo ao cair.", "",
                        PlayerCosmetics.isCosmeticEquipped(player, "Desintegração Vermelha Incandescente") ? "§cClique para remover" : "§eClique para equipar")));

        menu.setItem(16, createItem(Material.SLIME_BALL, getCosmeticNameWithGradient("Desintegração Verde Radiante"), List.of("§7Partículas verdes ao cair.", "",
                        PlayerCosmetics.isCosmeticEquipped(player, "Desintegração Verde Radiante") ? "§cClique para remover" : "§eClique para equipar")));

        // Botão de voltar
        ItemStack backButton = new ItemStack(Material.RED_DYE);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§cVoltar");
        backButton.setItemMeta(backMeta);
        menu.setItem(31, backButton);

        player.openInventory(menu);
    }

    // Criando os itens com nome e lore
    @SuppressWarnings("deprecation")
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
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Checando se o inventário é o "Partículas de Queda"
        if (event.getView().getTitle().equals("Partículas de Queda")) {
            event.setCancelled(true);

            if (event.getSlot() == 31) {
                CosmeticsMenu.open(player);
                return;
            }

            // Obtendo o nome do cosmético com base no slot clicado
            String cosmetic = getCosmeticNameFromSlot(event.getSlot());
            if (cosmetic != null) {
                handleCosmeticToggle(player, cosmetic);
                open(player);
            }
        }
    }

    private String getCosmeticNameFromSlot(int slot) {
        return switch (slot) {
            case 10 -> "Desintegração Violeta";
            case 12 -> "Desintegração Azul Elétrica";
            case 14 -> "Desintegração Vermelha Incandescente";
            case 16 -> "Desintegração Verde Radiante";
            default -> null;
        };
    }

    private void handleCosmeticToggle(Player player, String cosmetic) {
        if (PlayerCosmetics.isCosmeticEquipped(player, cosmetic)) {
            player.sendMessage("§b§lPARTÍCULA! §eVocê removeu §a" + cosmetic + "§e.");
            PlayerCosmetics.removeCosmetic(player);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1.0f, 1.2f);
        } else {
            player.sendMessage("§b§lPARTÍCULA! §eVocê equipou §a" + cosmetic + "§e.");
            PlayerCosmetics.equipCosmetic(player, cosmetic);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1.5f);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            String equippedCosmetic = PlayerCosmetics.getCosmeticEquipped(player);
            if (equippedCosmetic != null) {
                Bukkit.getLogger().info("Jogador: " + player.getName());
                Bukkit.getLogger().info("Cosmético equipado: " + equippedCosmetic);
                Color color = getColorForCosmetic(equippedCosmetic);
                if (color != null) {
                    spawnParticleEffect(player, color);
                }
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
            int currentStep = 1;

            @Override
            public void run() {
                if (currentStep > steps || !player.isOnline()) {
                    cancel();
                    return;
                }

                double radius = ((double) currentStep / steps) * maxRadius;
                var playerLoc = player.getLocation().clone().add(0, 0.1, 0);

                for (int i = 0; i < 30; i++) {
                    double angle = 2 * Math.PI * i / 30;
                    double x = Math.cos(angle) * radius;
                    double z = Math.sin(angle) * radius;

                    player.getWorld().spawnParticle(
                            Particle.REDSTONE,
                            playerLoc.clone().add(x, 0, z),
                            0,
                            new DustOptions(color, 1.2f)
                    );
                }

                currentStep++;
            }
        }.runTaskTimer(BoomFallPlugin.getInstance(), 0L, 2L);
    }

    @SuppressWarnings("deprecation")
    public static String getCosmeticNameWithGradient(String cosmetic) {

        return switch (cosmetic) {
            case "Desintegração Violeta" -> ChatColor.of("#8000FF") + "Desintegração Violeta";
            case "Desintegração Azul Elétrica" -> ChatColor.of("#0000FF") + "Desintegração Azul Elétrica";
            case "Desintegração Vermelha Incandescente" -> ChatColor.of("#FF0000") + "Desintegração Vermelha Incandescente";
            case "Desintegração Verde Radiante" -> ChatColor.of("#00FF00") + "Desintegração Verde Radiante";
            default -> ChatColor.WHITE + cosmetic;
        };
    }
}