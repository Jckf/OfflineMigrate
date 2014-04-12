package it.flaten.offlinemigrate;

import com.google.common.base.Charsets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;

public class OfflineMigrate extends JavaPlugin implements Listener {
    private File playerdata;

    @Override
    public void onEnable() {
        this.playerdata = new File(this.getServer().getWorlds().get(0).getWorldFolder(), "playerdata");

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        UUID offlineUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
        UUID onlineUuid = player.getUniqueId();

        this.getLogger().info("Offline UUID: " + offlineUuid);
        this.getLogger().info("Online UUID: " + onlineUuid);

        if (offlineUuid == onlineUuid) {
            this.getLogger().info(player.getName() + " only has offline UUID. Cannot migrate.");
            return;
        }

        File offlineFile = new File(this.playerdata, offlineUuid + ".dat");
        File onlineFile = new File(this.playerdata, onlineUuid + ".dat");

        this.getLogger().info("Offline file: " + offlineFile);
        this.getLogger().info("Online file: " + onlineFile);

        if (!offlineFile.exists()) {
            this.getLogger().info(player.getName() + " has no offline file. Nothing to migrate.");
            return;
        }

        if (offlineFile.renameTo(onlineFile)) {
            this.getServer().getLogger().info(player.getName() + " migrated from offline to online UUID.");
        }
    }
}
