package at.instaachiev.cmd;

import at.instaachiev.main.Main;
import at.instaachiev.notificationapi.Notification;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAchievments implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player p = null;
        if(sender instanceof Player) {
            p = (Player)sender;
        }
        final Player player = p;

        TextComponent title = new TextComponent("Test");
        title.setBold(true);
        title.setColor(ChatColor.GOLD);

        StringBuilder str = new StringBuilder();
        for(String st : args) {
            str.append(st + " ");
        }

        TextComponent message = new TextComponent(str.toString());
        message.setColor(ChatColor.WHITE);

        new Notification(Main.PLUGIN, Material.DIAMOND, message).show(player);

        return true;
    }
}
