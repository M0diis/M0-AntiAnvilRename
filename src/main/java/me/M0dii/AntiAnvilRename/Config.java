package me.M0dii.AntiAnvilRename;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config
{
    private String CANNOT_RENAME;
    private String DENY_HOLD_MSG;
    private Boolean WHITELIST_ENABLED;
    private Boolean BLACKLIST_ENABLED;
    private Boolean CLOSE_ON_RENAME;
    private Boolean ONLY_WITH_LORE;
    private String ONLY_WITH_LORE_MSG;
    private List<String> ALLOWED_ITEMS;
    private List<Material> DENIED_ITEMS;
    
    private List<String> DENY_HOLD_CMDS;
    private List<Material> DENY_HOLDING_ITEMS;
    
    private final Main plugin;
    
    public Config(Main plugin)
    {
        this.plugin = plugin;
    }
    
    public void load()
    {
        FileConfiguration cfg = this.plugin.getConfig();
    
        String prefix = "M0-AntiAnvilRename.";
        
        CANNOT_RENAME = format(cfg.getString(prefix + "RenameBlocked"));
        
        WHITELIST_ENABLED = cfg.getBoolean(prefix + "EnableWhitelist");
        BLACKLIST_ENABLED = cfg.getBoolean(prefix + "EnableBlacklist");
        
        CLOSE_ON_RENAME = cfg.getBoolean(prefix + "CloseOnAttempt");
        ALLOWED_ITEMS = cfg.getStringList(prefix + "AllowedItems");
        
        ONLY_WITH_LORE = cfg.getBoolean(prefix + "OnlyWithLore.Enabled");
        ONLY_WITH_LORE_MSG = format(cfg.getString(prefix + "OnlyWithLore.Message"));
        
        List<String> deniedItems = cfg.getStringList(prefix + "DeniedItems");
        
        this.setUpItems(deniedItems);
    
        DENY_HOLD_MSG = format(cfg.getString("M0-AntiAnvilRename.DenyWhileHolding.Message"));
        DENY_HOLD_CMDS = cfg.getStringList("M0-AntiAnvilRename.DenyWhileHolding.Commands");
        List<String> denyHoldItems = cfg.getStringList("M0-AntiAnvilRename.DenyWhileHolding.Items");
        
        this.setUpDenyHoldItems(denyHoldItems);
    }
    
    public void setUpItems(List<String> items)
    {
        this.DENIED_ITEMS = new ArrayList<>();
        
        for(String i : items)
        {
            Material m = Material.getMaterial(i);
            
            if(m != null)
            {
                this.DENIED_ITEMS.add(m);
            }
        }
    }
    
    private void setUpDenyHoldItems(List<String> items)
    {
        this.DENY_HOLDING_ITEMS = new ArrayList<>();
        
        for(String s : items)
        {
            Material m = Material.getMaterial(s);
            
            if(m != null)
            {
                this.DENY_HOLDING_ITEMS.add(m);
            }
        }
    }
    
    public List<Material> getDenyHoldItems()
    {
        return this.DENY_HOLDING_ITEMS;
    }
    
    public String getDenyHoldMsg()
    {
        return this.DENY_HOLD_MSG;
    }
    
    public List<String> getDenyHoldCommands()
    {
        return DENY_HOLD_CMDS;
    }
    
    private String format(String text)
    {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
 
    public String getCannotRenameMsg()
    {
        return CANNOT_RENAME;
    }
    
    public Boolean whitelistEnabled()
    {
        return WHITELIST_ENABLED;
    }
    
    public Boolean blacklistEnabled()
    {
        return BLACKLIST_ENABLED;
    }
    
    public Boolean closeOnRename()
    {
        return CLOSE_ON_RENAME;
    }
    
    public List<String> getAllowedItems()
    {
        return ALLOWED_ITEMS;
    }
    
    public List<Material> getDeniedItems()
    {
        return DENIED_ITEMS;
    }
    
    public boolean onlyWithLore()
    {
        return this.ONLY_WITH_LORE;
    }
    
    public String getOnlyLoreMsg()
    {
        return this.ONLY_WITH_LORE_MSG;
    }
}
