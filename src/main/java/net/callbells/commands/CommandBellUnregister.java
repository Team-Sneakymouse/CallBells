package net.callbells.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.callbells.CallBells;
import net.callbells.util.ChatUtility;
import net.callbells.util.RegistryUtil;

public class CommandBellUnregister extends Command {

    public CommandBellUnregister() {
        super("bellunregister");
        this.description = "Unregister yourself from the bell that you're looking at.";
        this.usageMessage = "/unregisterbell";
        this.setPermission(CallBells.IDENTIFIER + ".command." + this.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        // Check if the player is looking at a bell
        if (player.getTargetBlockExact(5) == null || player.getTargetBlockExact(5).getType() != Material.BELL) {
            player.sendMessage(ChatUtility.convertToComponent("&aYou must be looking at a bell to register it!"));
            return true;
        }

        Location bellLocation = player.getTargetBlockExact(5).getLocation();

        // Check if the bell is already registered
        if (RegistryUtil.isBellRegistered(bellLocation)) {
            // Bell is already registered
            // Add player's UUID to the bell if not already added
            List<UUID> bellOwners = RegistryUtil.getBellOwners(bellLocation);
            UUID playerUUID = player.getUniqueId();

            if (bellOwners.contains(playerUUID)) {
                String bellName = RegistryUtil.getBellName(bellLocation);

                bellOwners.remove(playerUUID);
                RegistryUtil.updateBellOwners(bellLocation, bellOwners);
                player.sendMessage(ChatUtility.convertToComponent("&aYou've succesfully unregistered from the bell &b'" + bellName + "'"));
            } else {
                player.sendMessage(ChatUtility.convertToComponent("&aYou aren't registered to that bell!"));
            }
        }

        return true;
    }

}
