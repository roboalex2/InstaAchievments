package at.instaachiev.notificationapi;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

public class Notification {
    private Material item;
    private TextComponent title;
    private TextComponent message;
    private NamespacedKey id;
    private Plugin plugin;


    public Notification(Plugin plugin, Material icon, TextComponent message) {
        this.id = new NamespacedKey(plugin, "notification/" + UUID.randomUUID().toString());
        this.item = icon;
        this.title = message;
        this.message = message;
        this.plugin = plugin;
    }


    public void show(Player... players) {
        add();
        grant(players);
        (new BukkitRunnable(){
            @Override
            public void run() {
                revoke(players);
                Bukkit.getUnsafe().removeAdvancement(id);
            }
        }).runTaskLaterAsynchronously(plugin, 4);
    }


    private String getJSON() {
        JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item", "minecraft:" + item.toString().toLowerCase());

        JsonObject display = new JsonObject();
        display.add("icon", icon);
        display.add("title", getJsonFromTextComponent(this.message));
        display.add("description", getJsonFromTextComponent(this.message));
        display.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/stone.png");
        display.addProperty("frame", "task");
        display.addProperty("announce_to_chat", false);
        display.addProperty("show_toast", true);
        display.addProperty("hidden", true);

        JsonObject criteria = new JsonObject();

        criteria.add("default", getDefaultTrigger());

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }


    private void grant(Player... players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        for (Player player : players) {
            if (!player.getAdvancementProgress(advancement).isDone()) {
                Collection<String> remainingCriteria = player.getAdvancementProgress(advancement).getRemainingCriteria();
                for (String remainingCriterion : remainingCriteria)
                    player.getAdvancementProgress(advancement).awardCriteria(remainingCriterion);
            }
        }
    }


    public void revoke(Player... players) {
        Advancement advancement = Bukkit.getAdvancement(id);
        for (Player player : players) {
            if (player.getAdvancementProgress(advancement).isDone()) {
                Collection<String> awardedCriteria = player.getAdvancementProgress(advancement).getAwardedCriteria();
                for (String awardedCriterion : awardedCriteria)
                    player.getAdvancementProgress(Bukkit.getAdvancement(id)).revokeCriteria(awardedCriterion);
            }
        }
    }


    private void add() {
        try {
            Bukkit.getUnsafe().loadAdvancement(id, getJSON());
        } catch (IllegalArgumentException e) {
            // Already Exists
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static JsonElement getJsonFromTextComponent(TextComponent textComponent) {
        Gson gson = new Gson();
        return gson.fromJson(ComponentSerializer.toString(textComponent), JsonElement.class);
    }

    private static JsonObject getDefaultTrigger() {
        JsonObject triggerObj = new JsonObject();
        triggerObj.addProperty("trigger", "minecraft:impossible");
        return triggerObj;
    }
}
