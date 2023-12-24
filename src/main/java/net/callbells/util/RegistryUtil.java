package net.callbells.util;

import org.bukkit.configuration.file.YamlConfiguration;

import net.callbells.CallBells;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistryUtil {

    private static final File configFile;
    private static final YamlConfiguration config;

    static {
        configFile = new File(CallBells.getInstance().getDataFolder(), "bells.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            saveDefaultResource("bells.yml");
        }
    }

    // Example method to get registered bell locations
    public static List<String> getRegisteredBells() {
        return config.getStringList("registered-bells");
    }

    // Example method to register a bell location
    public static void registerBell(String bellLocation, String bellName, UUID ownerUUID) {
        List<String> registeredBells = config.getStringList("registered-bells");
        registeredBells.add(bellLocation);
        config.set("registered-bells", registeredBells);

        // Store the owner UUID for the bell
        String ownersPath = "bell-owners." + bellLocation;
        List<UUID> owners = getBellOwners(bellLocation);
        owners.add(ownerUUID);
        config.set(ownersPath, owners.stream().map(UUID::toString).toList());

        // Store the bell name
        String namesPath = "bell-names." + bellLocation;
        config.set(namesPath, bellName);

        saveConfig();
    }

    // Check if a bell is already registered
    public static boolean isBellRegistered(String bellLocation) {
        List<String> registeredBells = config.getStringList("registered-bells");
        return registeredBells.contains(bellLocation);
    }

    // Get the owners of a bell
    public static List<UUID> getBellOwners(String bellLocation) {
        String ownersPath = "bell-owners." + bellLocation;
        return config.contains(ownersPath) ? config.getStringList(ownersPath).stream().map(UUID::fromString).toList() : new ArrayList<>();
    }

    // Update the owners of a bell
    public static void updateBellOwners(String bellLocation, List<UUID> owners) {
        String ownersPath = "bell-owners." + bellLocation;
        config.set(ownersPath, owners.stream().map(UUID::toString).toList());
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

    // Save the default resource to the plugin's data folder if it doesn't exist
    private static void saveDefaultResource(String resource) {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
