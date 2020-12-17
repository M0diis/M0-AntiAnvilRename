package me.M0dii.RenameBlocker;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config
{
    public static String CANNOT_RENAME;
    public static List<String> ALLOWED_ITEMS;

    public static void load(Main plugin)
    {
        FileConfiguration cfg = plugin.getConfig();
    
        CANNOT_RENAME = format(cfg.getString("M0-RenameBlocker.RenameBlocked"));
        ALLOWED_ITEMS = cfg.getStringList("M0-RenameBlocker.AllowedItems");
    }
    
    private static String format(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
