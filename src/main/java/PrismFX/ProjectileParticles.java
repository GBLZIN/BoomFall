package PrismFX;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.WeakHashMap;

public class ProjectileParticles implements Listener {

    private final Map<Projectile, String> activeProjectiles = new WeakHashMap<>();

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) return;

        String cosmetic = PlayerCosmetics.getProjectileCosmeticEquipped(player);
        if (cosmetic == null) return;

        Bukkit.getLogger().info("Projétil lançado por: " + player.getName());
        Bukkit.getLogger().info("Cosmético: " + cosmetic);

        Projectile projectile = event.getEntity();
        activeProjectiles.put(projectile, cosmetic);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isDead() || !projectile.isValid() || !activeProjectiles.containsKey(projectile)) {
                    cancel();
                    return;
                }

                switch (cosmetic) {
                    case "Chamas Abissais" -> {
                        projectile.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.SOUL, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.ASH, projectile.getLocation(), 1);
                    }
                    case "Luz Distorcida" -> {
                        projectile.getWorld().spawnParticle(Particle.REVERSE_PORTAL, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.DRAGON_BREATH, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, projectile.getLocation(), 1);
                    }
                    case "Chuva de Cristais" -> {
                        projectile.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.CHERRY_LEAVES, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.FALLING_SPORE_BLOSSOM, projectile.getLocation(), 1);
                    }
                    case "Centelha Mecânica" -> {
                        projectile.getWorld().spawnParticle(Particle.SCRAPE, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, projectile.getLocation(), 1);
                        projectile.getWorld().spawnParticle(Particle.CRIT, projectile.getLocation(), 1);
                    }
                }
            }
        }.runTaskTimer(PrismFXPlugin.getInstance(), 0L, 1L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        activeProjectiles.remove(event.getEntity());
    }
}
