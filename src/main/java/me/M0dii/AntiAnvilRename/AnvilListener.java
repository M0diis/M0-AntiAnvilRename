package me.M0dii.AntiAnvilRename;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener
{
    private final Main plugin;
    
    public AnvilListener(Main plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void cancelRename(PrepareAnvilEvent e)
    {
        String renameText = ChatColor.stripColor(e.getInventory().getRenameText());
        
        ItemStack first = e.getInventory().getFirstItem();
        
        HumanEntity p = e.getView().getPlayer();
        
        if(Config.WHITELIST_ENABLED)
        {
            for(String allowed : Config.ALLOWED_ITEMS)
            {
                Material allowedItem = Material.getMaterial(allowed);
                
                if(first != null)
                {
                    if(first.getType().equals(allowedItem))
                    {
                        return;
                    }
                }
            }
        }
        
        if(Config.BLACKLIST_ENABLED)
        {
            for(String s : Config.DENIED_ITEMS)
            {
                Material m = Material.getMaterial(s);
                
                if(m != null && first != null)
                {
                    if(m.equals(first.getType()))
                    {
                        if(p.hasPermission("m0antianvilrename.bypass"))
                        {
                            return;
                        }
    
                        p.sendMessage(Config.CANNOT_RENAME);
    
                        if(Config.CLOSE_ON_RENAME)
                        {
                            p.closeInventory();
                        }
    
                        e.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
        
        if(first != null)
        {
            String itemName = first.getType().name().toLowerCase();
    
            String allowPerm = "m0antianvilrename." + itemName + ".allow";
            
            if(p.hasPermission(allowPerm)) return;
        }
        
        if(first != null)
        {
            String firstName = ChatColor.stripColor(first.getItemMeta().getDisplayName());
            
            if(renameText != null)
            {
                if(!renameText.equalsIgnoreCase(firstName))
                {
                    if(p.hasPermission("m0antianvilrename.bypass"))
                    {
                        return;
                    }
                    
                    p.sendMessage(Config.CANNOT_RENAME);
                    
                    if(Config.CLOSE_ON_RENAME)
                    {
                        p.closeInventory();
                    }
                    
                    e.setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }
}
