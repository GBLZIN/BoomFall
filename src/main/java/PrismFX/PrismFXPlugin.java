package PrismFX;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class PrismFXPlugin extends JavaPlugin {

    private static PrismFXPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Registrar a execução do comando "cosmetics"
        Objects.requireNonNull(getCommand("cosmetics")).setExecutor(new CosmeticsCommand());

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new CosmeticsMenu(), this);
        getServer().getPluginManager().registerEvents(new FallParticlesMenu(), this);

        getServer().getPluginManager().registerEvents(new ProjectileParticlesMenu(), this);
        getServer().getPluginManager().registerEvents(new ProjectileParticles(), this);
    }

    public static PrismFXPlugin getInstance() {
        return instance;
    }
}
