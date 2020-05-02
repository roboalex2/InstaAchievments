package at.instaachiev.listener;

import at.instaachiev.main.Utils;
import net.minecraft.server.v1_15_R1.Advancements;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.logging.Level;

public class OnAdvancementAchiev implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        Bukkit.getLogger().log(Level.WARNING, Utils.getAdvancementTitle(event.getAdvancement()));
        Bukkit.getLogger().log(Level.WARNING, Utils.getAdvancementDescription(event.getAdvancement()));
        //TODO Use Reflections to cancle notification.
        MinecraftServer.getServer().getAdvancementData().REGISTRY.advancements.values().b.f = false;
    }
}
