package net.callbells;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import net.callbells.commands.CommandBellRegister;
import net.callbells.commands.CommandBellUnregister;
import net.callbells.listeners.BellListener;

public class CallBells extends JavaPlugin {

    private static CallBells instance;

    public static final String IDENTIFIER = "callbells";

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Bukkit.getCommandMap().register(IDENTIFIER, new CommandBellRegister());
        Bukkit.getCommandMap().register(IDENTIFIER, new CommandBellUnregister());

        getServer().getPluginManager().registerEvents(new BellListener(), this);

        getServer().getPluginManager().addPermission(new Permission(IDENTIFIER + ".*"));
        getServer().getPluginManager().addPermission(new Permission(IDENTIFIER + ".command.*"));
    }

    public static CallBells getInstance() {
        return instance;
    }

}
