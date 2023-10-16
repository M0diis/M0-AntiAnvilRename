package me.m0dii.antianvilrename;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AnvilListener implements Listener {
    private final Config cfg;

    public AnvilListener(AntiAnvilRenamePlugin plugin) {
        this.cfg = plugin.getCfg();
    }

    @EventHandler
    public void denyWhileHolding(final PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage().split(" ")[0];

        Player p = e.getPlayer();

        if (p.hasPermission("m0antianvilrename.bypass.commands")) {
            return;
        }

        ItemStack holding = e.getPlayer().getInventory().getItemInMainHand();

        if(holding.getType().isAir()) {
            return;
        }

        if (cfg.getDenyHoldCommands().contains(cmd)) {
            for (String bl : cfg.getWordBlacklist()) {
                for (String c : e.getMessage().split(" ")) {
                    if (c.toLowerCase().contains(bl) || c.equalsIgnoreCase(bl)) {
                        e.setCancelled(true);
                    }
                }
            }

            if (cfg.getDenyHoldItems().contains(holding.getType())) {
                e.getPlayer().sendMessage(this.cfg.getDenyHoldMsg());

                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void cancelRename(final PrepareAnvilEvent e) {
        String renameText = ChatColor.stripColor(e.getInventory().getRenameText());

        ItemStack first = e.getInventory().getFirstItem();

        boolean loreOnly = this.cfg.getOnlyWithLore();

        HumanEntity p = e.getView().getPlayer();

        if (p.hasPermission("m0antianvilrename.bypass"))
            return;

        if (loreOnly && first != null) {
            ItemMeta m = first.getItemMeta();

            List<String> lore = m.getLore();

            boolean hasLore = lore != null && lore.stream().anyMatch(s -> !s.isEmpty());

            if (hasLore) {
                if (this.cfg.getCloseOnRename())
                    p.closeInventory();

                if (!this.cfg.getOnlyWithLoreMsg().isEmpty())
                    p.sendMessage(this.cfg.getOnlyWithLoreMsg());

                e.setResult(new ItemStack(Material.AIR));
            }
        }

        for (String bl : cfg.getWordBlacklist())
            if (renameText != null &&
                    (renameText.toLowerCase().contains(bl) || renameText.equalsIgnoreCase(bl)))
                e.setResult(new ItemStack(Material.AIR));

        if (this.cfg.getWhitelistEnabled()) {
            for (String allowed : this.cfg.getAllowedItems()) {
                Material allowedItem = Material.getMaterial(allowed);

                if (first != null) {
                    if (first.getType().equals(allowedItem))
                        return;
                }
            }
        }

        if (this.cfg.getBlacklistEnabled()) {
            for (Material m : this.cfg.getForbiddenItems()) {
                if (m != null && first != null) {
                    String itemName = first.getType().name().toLowerCase();

                    String allowPerm = "m0antianvilrename." + itemName + ".allow";

                    if (p.hasPermission(allowPerm)) return;

                    if (m.equals(first.getType())) {
                        String msg = this.cfg.getCannotRenameMsg();

                        if (!msg.isEmpty())
                            p.sendMessage(msg);

                        if (this.cfg.getCloseOnRename())
                            p.closeInventory();

                        e.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        } else if (first != null) {
            String itemName = first.getType().name();

            String allowPerm1 = "m0antianvilrename." + itemName.toLowerCase() + ".allow";
            String allowPerm2 = "m0antianvilrename." + itemName.toUpperCase() + ".allow";

            if (p.hasPermission(allowPerm1) || p.hasPermission(allowPerm2)) return;

            ItemMeta itemMeta = first.getItemMeta();

            if (itemMeta != null) {
                String firstName = ChatColor.stripColor(itemMeta.getDisplayName());

                if (renameText != null) {
                    if (!renameText.equalsIgnoreCase(firstName)) {
                        p.sendMessage(this.cfg.getCannotRenameMsg());

                        if (this.cfg.getCloseOnRename()) {
                            p.closeInventory();
                        }

                        e.setResult(new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }
}
