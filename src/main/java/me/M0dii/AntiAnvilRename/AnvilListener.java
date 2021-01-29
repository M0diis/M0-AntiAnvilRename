package me.M0dii.AntiAnvilRename;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AnvilListener implements Listener
{
    private final Main plugin;
    
    private final Config cfg;
    
    public AnvilListener(Main plugin)
    {
        this.plugin = plugin;
        
        this.cfg = plugin.getCfg();
    }
    
    @EventHandler
    public void denyWhileHolding(PlayerCommandPreprocessEvent e)
    {
        String cmd = e.getMessage().split(" ")[0];
        
        ItemStack holding = e.getPlayer().getItemInHand();
        
        List<String> blockedCommands = this.cfg.getDenyHoldCommands();
        List<Material> blockedItems = this.cfg.getDenyHoldItems();
        
        if(blockedCommands.contains(cmd))
        {
            if(blockedItems.contains(holding.getType()))
            {
                e.getPlayer().sendMessage(this.cfg.getDenyHoldMsg());
                
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void cancelRename(PrepareAnvilEvent e)
    {
        String renameText = ChatColor.stripColor(e.getInventory().getRenameText());
        
        ItemStack first = e.getInventory().getFirstItem();
        
        HumanEntity p = e.getView().getPlayer();
        
        if(this.cfg.whitelistEnabled())
        {
            for(String allowed : this.cfg.getAllowedItems())
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
        
        if(this.cfg.blacklistEnabled())
        {
            for(Material m : this.cfg.getDeniedItems())
            {
                if(m != null && first != null)
                {
                    String itemName = first.getType().name().toLowerCase();
    
                    String allowPerm = "m0antianvilrename." + itemName + ".allow";
    
                    if(p.hasPermission(allowPerm)) return;

                    if(m.equals(first.getType()))
                    {
                        if(p.hasPermission("m0antianvilrename.bypass"))
                        {
                            return;
                        }
    
                        p.sendMessage(this.cfg.getCannotRenameMsg());
    
                        if(this.cfg.closeOnRename())
                        {
                            p.closeInventory();
                        }
    
                        e.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
        else
        {
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
                
                        p.sendMessage(this.cfg.getCannotRenameMsg());
                
                        if(this.cfg.closeOnRename())
                        {
                            p.closeInventory();
                        }
                
                        e.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
        

    }
}
