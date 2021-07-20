package me.M0dii.AntiAnvilRename;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter
{
    private final AAR plugin;
    private final Config cfg;
    
    public CommandHandler(AAR plugin)
    {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
                             String alias, String[] args)
    {
        if(sender instanceof Player)
        {
            Player p = (Player)sender;
            
            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("reload"))
                {
                    if(p.hasPermission("m0antianvilrename.command.reload"))
                    {
                        this.plugin.reloadConfig();
                        this.plugin.saveConfig();
                        
                        this.plugin.renewConfig();
                        
                        p.sendMessage(this.cfg.getRELOADED_CONFIG_MSG());
                    }
                    else p.sendMessage(this.cfg.getNO_PERMISSION_MSG());
                    
                    return true;
                }
            }
            
            return true;
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd,
                                      String label, String[] args)
    {
        List<String> completes = new ArrayList<>();
        
        if(args.length == 1)
        {
            completes.add("reload");
        }
        
        return completes;
    }
}
