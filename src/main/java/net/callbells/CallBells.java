package net.callbells;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.callbells.commands.CommandBellRegister;
import net.callbells.commands.CommandBellUnregister;
import net.callbells.listeners.BellListener;

public class CallBells extends JavaPlugin {

    public static final String IDENTIFIER = "callbells";

    @Override
    public void onEnable() {
        Bukkit.getCommandMap().register(IDENTIFIER, new CommandBellRegister());
        Bukkit.getCommandMap().register(IDENTIFIER, new CommandBellUnregister());

        getServer().getPluginManager().registerEvents(new BellListener(), this);
    }

}
