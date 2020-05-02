package at.instaachiev.main;

import com.google.gson.JsonElement;
import net.minecraft.server.v1_15_R1.LocaleLanguage;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Utils {

    private static Object translator;



    public static String getAdvancementTitle(Advancement advancement) {
        try {
            return translateResourceKey(getAdvancementDisplayJsonElement(advancement).getAsJsonObject().get("title")
                    .getAsJsonObject().get("translate").toString().replace("\"", ""));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fix Exception before");
        }
    }


    public static String getAdvancementDescription(Advancement advancement) {
        try {
            return translateResourceKey(getAdvancementDisplayJsonElement(advancement).getAsJsonObject().get("description")
                    .getAsJsonObject().get("translate").toString().replace("\"", ""));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fix Exception before");
        }
    }


    public static String translateResourceKey(String str) throws InvocationTargetException, IllegalAccessException,
            ClassNotFoundException, NoSuchMethodException, InstantiationException {
        String title = "Fehler";
        if(translator == null) {
            translator = ServerPackage.MINECRAFT.getClass("LocaleLanguage").getConstructor().newInstance();
        }
        for(Method meth : ServerPackage.MINECRAFT.getClass("LocaleLanguage").getMethods()) {
            if(meth.getReturnType() == String.class && Modifier.isPublic(meth.getModifiers()) && Modifier.isSynchronized(meth.getModifiers())) {
                title = (String)meth.invoke(translator, str);
            }
        }
        return title;
    }


    public static JsonElement getAdvancementDisplayJsonElement(Advancement advancement) {
        try {
            Method getHandle = ServerPackage.CRAFTBUKKIT.getClass("advancement.CraftAdvancement")
                    .getMethod("getHandle");
            Object adv = getHandle.invoke(advancement);
            for(Field field : ServerPackage.MINECRAFT.getClass("Advancement").getDeclaredFields()) {
                if(field.getType() == ServerPackage.MINECRAFT.getClass("AdvancementDisplay")
                        && Modifier.isFinal(field.getModifiers()) && Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                    Object advancementdisplay = field.get(adv);
                    for(Method meth : ServerPackage.MINECRAFT.getClass("AdvancementDisplay").getMethods()) {
                        if(meth.getReturnType() == JsonElement.class && Modifier.isPublic(meth.getModifiers())) {
                            JsonElement element = (JsonElement) meth.invoke(advancementdisplay);
                            return element;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
