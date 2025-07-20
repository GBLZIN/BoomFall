package BoomFall;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BoomFallPlugin extends JavaPlugin {

    private static BoomFallPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        // Registrar a execução do comando "cosmetics"
        Objects.requireNonNull(getCommand("cosmetics")).setExecutor(new CosmeticsCommand());

        // Registrar eventos
        getServer().getPluginManager().registerEvents(new CosmeticsMenu(), this);
        getServer().getPluginManager().registerEvents(new FallParticlesMenu(), this);
    }

    public static BoomFallPlugin getInstance() {
        return instance;
    }
}
