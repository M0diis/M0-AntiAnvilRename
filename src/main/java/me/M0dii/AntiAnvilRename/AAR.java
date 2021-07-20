package me.M0dii.AntiAnvilRename;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class AAR extends JavaPlugin
{
    private final PluginManager pm;
    
    public AAR()
    {
        this.pm = getServer().getPluginManager();
    }
    
    private FileConfiguration config = null;
    private File configFile = null;
    
    private Config cfg;
    
    public Config getCfg()
    {
        return cfg;
    }
    
    public void renewConfig()
    {
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        
        this.cfg.load(this, this.config);
    }
    
    public void onEnable()
    {
        prepareConfig();
        
        this.pm.registerEvents(new AnvilListener(this), this);
    
        PluginCommand cmd = this.getCommand("antianvilrename");
        
        if(cmd != null)
            cmd.setExecutor(new CommandHandler(this));
    }
    
    private void prepareConfig()
    {
        this.configFile = new File(this.getDataFolder(), "config.yml");
        
        if(!this.configFile.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            this.configFile.getParentFile().mkdirs();
            
            this.copy(this.getResource("config.yml"), this.configFile);
        }
        
        try
        {
            this.getConfig().options().copyDefaults(true);
            this.getConfig().save(this.configFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    
        this.cfg = new Config();
        this.cfg.load(this, this.config);
    }
    
    private void copy(InputStream in, File file)
    {
        if(in == null)
        {
            this.getLogger().warning("Cannot copy, resource null");
            
            return;
        }
        
        try
        {
            OutputStream out = new FileOutputStream(file);
            
            byte[] buf = new byte[1024];
            
            int len;
            
            while((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            
            out.close();
            in.close();
        }
        catch(Exception e)
        {
            this.getLogger().warning("Error copying resource: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public void onDisable()
    {
        this.pm.disablePlugin(this);
    }
}
