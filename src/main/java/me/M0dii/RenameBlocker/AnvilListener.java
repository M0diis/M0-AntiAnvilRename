package me.M0dii.RenameBlocker;

import org.bukkit.Material;
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
       ItemStack second = e.getInventory().getSecondItem();
       
       if(first != null)
       {
           String firstName = first.getItemMeta().getDisplayName();
    
           if(renameText != null)
           {
               if(!renameText.equalsIgnoreCase(firstName))
               {
                   e.setResult(new ItemStack(Material.AIR));
               }
           }
       }
       
       if(second != null)
       {
           String secondName = second.getItemMeta().getDisplayName();
           
           if(renameText != null)
           {
               if(!renameText.equalsIgnoreCase(secondName))
               {
                   e.setResult(new ItemStack(Material.AIR));
               }
           }
       }
    }
}
