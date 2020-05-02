package at.instaachiev.main;
import at.instaachiev.cmd.CmdAchievments;
import at.instaachiev.listener.OnAdvancementAchiev;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static Main PLUGIN;

    @Override
    public void onEnable() {
        PLUGIN = this;
        this.getCommand("achievments").setExecutor(new CmdAchievments());
        Bukkit.getServer().getPluginManager().registerEvents(new OnAdvancementAchiev(), this);
    }

}
