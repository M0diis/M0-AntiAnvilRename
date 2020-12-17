package me.M0dii.AntiAnvilRename;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config
{
    public static String CANNOT_RENAME;
    public static Boolean WHITELIST_ENABLED;
    public static Boolean CLOSE_ON_RENAME;
    public static List<String> ALLOWED_ITEMS;

    public static void load(Main plugin)
    {
        FileConfiguration cfg = plugin.getConfig();
    
        CANNOT_RENAME = format(cfg.getString("M0-AntiAnvilRename.RenameBlocked"));
        WHITELIST_ENABLED = cfg.getBoolean("M0-AntiAnvilRename.EnableWhitelist");
        CLOSE_ON_RENAME = cfg.getBoolean("M0-AntiAnvilRename.CloseOnAttempt");
        ALLOWED_ITEMS = cfg.getStringList("M0-AntiAnvilRename.AllowedItems");
    }
    
    private static String format(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
