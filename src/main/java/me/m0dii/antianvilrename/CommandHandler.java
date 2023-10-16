package me.m0dii.antianvilrename;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final AntiAnvilRenamePlugin plugin;
    private final Config cfg;

    public CommandHandler(AntiAnvilRenamePlugin plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getCfg();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String alias, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("m0antianvilrename.command.reload")) {
                    this.plugin.reloadConfig();
                    this.plugin.saveConfig();

                    this.plugin.renewConfig();

                    sender.sendMessage(this.cfg.getConfigReloadedMsg());
                } else sender.sendMessage(this.cfg.getNoPermissionMsg());

                return true;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                      @NotNull String label, String[] args) {
        List<String> completes = new ArrayList<>();

        if (args.length == 1) {
            completes.add("reload");
        }

        return completes;
    }
}
