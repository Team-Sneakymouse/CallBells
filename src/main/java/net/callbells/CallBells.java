package net.callbells;

import org.bukkit.plugin.java.JavaPlugin;

import net.callbells.listeners.BellListener;

public class CallBells extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BellListener(), this);
    }

}
