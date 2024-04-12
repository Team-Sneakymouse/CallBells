package net.callbells.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import net.callbells.CallBells;

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
		return new ArrayList<>(config.getConfigurationSection("registered-bells").getKeys(false));
	}

	// Example method to register a bell location
	public static void registerBell(Location bellLocation, String bellName, UUID ownerUUID) {
		// Store the bell name
		String namesPath = "registered-bells." + locationToString(bellLocation) + ".name";
		config.set(namesPath, bellName);

		// Store the owner UUID for the bell
		String ownersPath = "registered-bells." + locationToString(bellLocation) + ".owners";
		List<UUID> owners = getBellOwners(bellLocation);
		owners.add(ownerUUID);
		config.set(ownersPath, owners.stream().map(UUID::toString).toList());

		saveConfig();
	}

	public static void removeBell(Location bellLocation) {
		String bellPath = "registered-bells." + locationToString(bellLocation);

		if (config.contains(bellPath)) {
			config.set(bellPath, null);
			saveConfig();
		}
	}

	// Check if a bell is already registered
	public static boolean isBellRegistered(Location bellLocation) {
		return config.contains("registered-bells." + locationToString(bellLocation));
	}

	// Get the owners of a bell
	public static List<UUID> getBellOwners(Location bellLocation) {
		String ownersPath = "registered-bells." + locationToString(bellLocation) + ".owners";
		List<String> ownersStringList = config.getStringList(ownersPath);

		// Convert the list to mutable
		List<UUID> owners = new LinkedList<>(ownersStringList.stream().map(UUID::fromString).toList());
		return owners;
	}

	// Update the owners of a bell
	public static void updateBellOwners(Location bellLocation, List<UUID> owners) {
		if (owners.isEmpty()) {
			removeBell(bellLocation);
			return;
		}

		String ownersPath = "registered-bells." + locationToString(bellLocation) + ".owners";
		config.set(ownersPath, owners.stream().map(UUID::toString).toList());
		saveConfig();
	}

	// Example method to get the name of a registered bell
	public static String getBellName(Location bellLocation) {
		String namesPath = "registered-bells." + locationToString(bellLocation) + ".name";
		return config.getString(namesPath);
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

	// Convert a Location to a compact string representation
	private static String locationToString(Location location) {
		return String.format("%s,%.0f,%.0f,%.0f",
				location.getWorld().getName(),
				location.getX(),
				location.getY(),
				location.getZ());
	}

}
