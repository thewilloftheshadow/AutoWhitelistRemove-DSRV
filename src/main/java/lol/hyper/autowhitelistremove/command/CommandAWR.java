/*
 * This file is part of AutoWhitelistRemove.
 *
 * AutoWhitelistRemove is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoWhitelistRemove is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AutoWhitelistRemove.  If not, see <https://www.gnu.org/licenses/>.
 */

package lol.hyper.autowhitelistremove.command;

import lol.hyper.autowhitelistremove.AutoWhitelistRemove;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandAWR implements TabExecutor {

    private final AutoWhitelistRemove autoWhitelistRemove;

    public CommandAWR(AutoWhitelistRemove autoWhitelistRemove) {
        this.autoWhitelistRemove = autoWhitelistRemove;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "check": {
                    if (!sender.hasPermission("autowhitelistremove.check")) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                    } else {
                        Set<String> removedPlayers = autoWhitelistRemove.whitelistCheck.checkWhitelist(false);
                        sender.sendMessage(ChatColor.GOLD + "--------------------AWR---------------------");
                        if (removedPlayers.isEmpty()) {
                            sender.sendMessage(ChatColor.YELLOW + "No players to remove.");
                        } else {
                            // the valueOf is there because it thinks we are adding the chatcolor and size
                            sender.sendMessage(ChatColor.YELLOW + String.valueOf(removedPlayers.size())
                                    + " players will be removed. Type \"/awr check confirm\" to confirm the removal.");
                            sender.sendMessage(ChatColor.YELLOW + String.join(", ", removedPlayers));
                        }
                        sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                    }
                    return true;
                }
                case "reload": {
                    if (!sender.hasPermission("autowhitelistremove.reload")) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                    } else {
                        autoWhitelistRemove.loadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                    }
                    return true;
                }
                default: {
                    sender.sendMessage(ChatColor.GOLD + "--------------------AWR---------------------");
                    sender.sendMessage(ChatColor.GOLD + "/awr help " + ChatColor.YELLOW + "- Shows this menu.");
                    sender.sendMessage(ChatColor.GOLD + "/awr check " + ChatColor.YELLOW
                            + "- Check inactive players and remove them.");
                    sender.sendMessage(ChatColor.GOLD + "/awr reload " + ChatColor.YELLOW + "- Reload the config.");
                    sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                    return true;
                }
            }
        }
        if (args.length == 2) {
            if ("confirm".equalsIgnoreCase(args[1])) {
                if (sender.hasPermission("autowhitelistremove.check")) {
                    Set<String> removedPlayers = autoWhitelistRemove.whitelistCheck.checkWhitelist(true);
                    sender.sendMessage(ChatColor.GOLD + "--------------------AWR---------------------");
                    // the valueOf is there because it thinks we are adding the chatcolor and size
                    sender.sendMessage(
                            ChatColor.YELLOW + String.valueOf(removedPlayers.size()) + " players have been removed.");
                    sender.sendMessage(ChatColor.YELLOW + String.join(", ", removedPlayers));
                    sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                }
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "--------------------AWR---------------------");
            sender.sendMessage(ChatColor.GOLD + "/awr help " + ChatColor.YELLOW + "- Shows this menu.");
            sender.sendMessage(
                    ChatColor.GOLD + "/awr check " + ChatColor.YELLOW + "- Check which players are inactive.");
            sender.sendMessage(ChatColor.GOLD + "/awr check confirm" + ChatColor.YELLOW
                    + "- Confirm removal of inactive players.");
            sender.sendMessage(ChatColor.GOLD + "/awr reload " + ChatColor.YELLOW + "- Reload the config.");
            sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "AutoWhitelistRemove version "
                + autoWhitelistRemove.getDescription().getVersion() + ". Created by hyperdefined.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {
                return Collections.singletonList("confirm");
            }
        }
        return Arrays.asList("check", "help", "reload");
    }
}
