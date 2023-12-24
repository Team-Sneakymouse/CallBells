package net.callbells.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.callbells.CallBells;
import net.callbells.util.RegistryUtil;

public class CommandBellRegister extends Command {

    public CommandBellRegister() {
        super("bellregister");
        this.description = "Register yourself to the bell that you're looking at.";
        this.usageMessage = "/registerbell (name of the bell)";
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
            player.sendMessage("You must be looking at a bell to register it!");
            return true;
        }

        // Get the bell's location as a string
        String bellLocation = player.getTargetBlockExact(5).getLocation().toString();

        // Check if the bell is already registered
        if (RegistryUtil.isBellRegistered(bellLocation)) {
            // Bell is already registered
            // Add player's UUID to the bell if not already added
            List<UUID> bellOwners = RegistryUtil.getBellOwners(bellLocation);
            UUID playerUUID = player.getUniqueId();

            if (!bellOwners.contains(playerUUID)) {
                bellOwners.add(playerUUID);
                RegistryUtil.updateBellOwners(bellLocation, bellOwners);
                player.sendMessage("You've been added as an owner to the registered bell!");
            } else {
                player.sendMessage("You are already an owner of this bell!");
            }
        } else {
            // Bell is not registered
            // Check if a name is provided in the command
            if (args.length < 2) {
                player.sendMessage("Please provide a name for the bell: /registerbell <name>");
                return true;
            }

            String bellName = args[1];

            // Register the bell with its name, location, and the player's UUID
            RegistryUtil.registerBell(bellLocation, bellName, player.getUniqueId());
            player.sendMessage("Bell '" + bellName + "' registered!");
        }

        return true;
    }
}
