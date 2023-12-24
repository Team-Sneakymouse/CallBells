package net.callbells.util;

import org.bukkit.configuration.file.YamlConfiguration;

import net.callbells.CallBells;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RegistryUtil {

    private static final File configFile;
    private static final YamlConfiguration config;

    static {
        configFile = new File(CallBells.getInstance().getDataFolder(), "bells.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            CallBells.getInstance().saveResource("bells.yml", false);
        }
    }

    // Example method to get registered bell locations
    public static List<String> getRegisteredBells() {
        return config.getStringList("registered-bells");
    }

    // Example method to register a bell location
    public static void registerBell(String bellLocation) {
        List<String> registeredBells = config.getStringList("registered-bells");
        registeredBells.add(bellLocation);
        config.set("registered-bells", registeredBells);

        saveConfig();
    }

    // Save the configuration to the file
    private static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
