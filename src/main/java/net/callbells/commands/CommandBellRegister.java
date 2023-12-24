package net.callbells.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.callbells.CallBells;

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

        return true;
    }
}

