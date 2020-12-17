package me.M0dii.AntiAnvilRename;

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
        String renameText = e.getInventory().getRenameText();
        
        ItemStack first = e.getInventory().getFirstItem();
        
        
        
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
        
        if(first != null)
        {
            String firstName = first.getItemMeta().getDisplayName();
            
            if(renameText != null)
            {
                if(!renameText.equalsIgnoreCase(firstName))
                {
                    for(HumanEntity p : e.getViewers())
                    {
                        if(p.hasPermission("m0antianvilrename.bypass"))
                        {
                            return;
                        }
                        
                        p.sendMessage(Config.CANNOT_RENAME);
                        
                        if(Config.CLOSE_ON_RENAME)
                        {
                            p.closeInventory();
                            
                            break;
                        }
                    }
                    
                    e.setResult(new ItemStack(Material.AIR));
                }
            }
        }
    }
}
