package me.m0dii.antianvilrename;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Config {
    private String cannotRenameMsg;
    private String denyHoldMsg;
    private Boolean whitelistEnabled;
    private Boolean blacklistEnabled;
    private Boolean closeOnRename;
    private Boolean onlyWithLore;
    private String onlyWithLoreMsg;

    private List<String> wordBlacklist;

    private List<String> allowedItems;
    private List<Material> forbiddenItems;

    private List<String> denyHoldCommands;
    private List<Material> denyHoldItems;

    private String noPermissionMsg;
    private String configReloadedMsg;

    public Config() {
    }

    public void load(FileConfiguration cfg) {
        String prefix = "M0-AntiAnvilRename.";

        cannotRenameMsg = format(cfg.getString(prefix + "RenameBlocked"));

        whitelistEnabled = cfg.getBoolean(prefix + "EnableWhitelist");
        blacklistEnabled = cfg.getBoolean(prefix + "EnableBlacklist");

        closeOnRename = cfg.getBoolean(prefix + "CloseOnAttempt");
        allowedItems = cfg.getStringList(prefix + "AllowedItems");

        onlyWithLore = cfg.getBoolean(prefix + "OnlyWithLore.Enabled");
        onlyWithLoreMsg = format(cfg.getString(prefix + "OnlyWithLore.Message"));

        List<String> deniedItems = cfg.getStringList(prefix + "DeniedItems");

        this.setUpItems(deniedItems);

        denyHoldMsg = format(cfg.getString("M0-AntiAnvilRename.DenyWhileHolding.Message"));
        denyHoldCommands = cfg.getStringList("M0-AntiAnvilRename.DenyWhileHolding.Commands");
        List<String> denyHoldItems = cfg.getStringList("M0-AntiAnvilRename.DenyWhileHolding.Items");

        wordBlacklist = cfg.getStringList("M0-AntiAnvilRename.WordBlacklist");

        noPermissionMsg = format(cfg.getString("M0-AntiAnvilRename.NoPermission"));
        configReloadedMsg = format(cfg.getString("M0-AntiAnvilRename.ConfigReloaded"));

        this.setUpDenyHoldItems(denyHoldItems);
    }

    public void setUpItems(List<String> items) {
        this.forbiddenItems = new ArrayList<>();

        for (String i : items) {
            Material m = Material.getMaterial(i);

            if (m != null) {
                this.forbiddenItems.add(m);
            }
        }
    }

    private void setUpDenyHoldItems(List<String> items) {
        this.denyHoldItems = new ArrayList<>();

        for (String s : items) {
            Material m = Material.getMaterial(s);

            if (m != null) {
                this.denyHoldItems.add(m);
            }
        }
    }

    private String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
