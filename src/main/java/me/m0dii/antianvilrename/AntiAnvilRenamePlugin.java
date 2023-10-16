package me.m0dii.antianvilrename;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AntiAnvilRenamePlugin extends JavaPlugin {
    private final PluginManager pm;

    public AntiAnvilRenamePlugin() {
        this.pm = getServer().getPluginManager();
    }

    private FileConfiguration config = null;
    private File configFile = null;

    private Config cfg;

    public Config getCfg() {
        return cfg;
    }

    public void renewConfig() {
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        this.cfg.load(this.config);
    }

    public void onEnable() {
        prepareConfig();

        this.pm.registerEvents(new AnvilListener(this), this);

        PluginCommand cmd = this.getCommand("antianvilrename");

        if (cmd != null)
            cmd.setExecutor(new CommandHandler(this));

        setupMetrics();

        checkForUpdates();
    }

    private void checkForUpdates() {
        new UpdateChecker(this, 86811).getVersion(ver ->
        {
            String curr = this.getDescription().getVersion();

            if (!curr.equalsIgnoreCase(
                    ver.replace("v", ""))) {
                getLogger().info("You are running an outdated version of M0-CoreCord.");
                getLogger().info("Latest version: " + ver + ", you are using: " + curr);
                getLogger().info("You can download the latest version on Spigot:");
                getLogger().info("https://www.spigotmc.org/resources/m0-antianvilrename.86811/");
            }
        });
    }

    private void setupMetrics() {
        Metrics metrics = new Metrics(this, 12132);

        CustomChart c = new MultiLineChart("players_and_servers", () ->
        {
            Map<String, Integer> valueMap = new HashMap<>();

            valueMap.put("servers", 1);
            valueMap.put("players", Bukkit.getOnlinePlayers().size());

            return valueMap;
        });

        metrics.addCustomChart(c);
    }

    private void prepareConfig() {
        this.configFile = new File(this.getDataFolder(), "config.yml");

        if (!this.configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.configFile.getParentFile().mkdirs();

            this.copy(this.getResource("config.yml"), this.configFile);
        }

        try {
            this.getConfig().options().copyDefaults(true);
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);

        this.cfg = new Config();
        this.cfg.load(this.config);
    }

    private void copy(InputStream in, File file) {
        if (in == null) {
            this.getLogger().warning("Cannot copy, resource null");

            return;
        }

        try {
            OutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[1024];

            int len;

            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            out.close();
            in.close();
        } catch (Exception e) {
            this.getLogger().warning("Error copying resource: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void onDisable() {
        this.pm.disablePlugin(this);
    }
}
