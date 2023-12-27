package net.callbells.util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;

import net.callbells.CallBells;

public class WorldGuardUtil {

    private static final WorldGuardPlugin worldGuardPlugin;

    static {
        Plugin plugin = CallBells.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin != null && plugin instanceof WorldGuardPlugin) {
            worldGuardPlugin = (WorldGuardPlugin) plugin;
        } else {
            worldGuardPlugin = null;
        }
    }

    public static boolean canBuildAtLocation(Player player, org.bukkit.Location bukkitLocation) {
        if (worldGuardPlugin == null || player.isOp()) {
            return true;
        }

        World worldEditWorld = BukkitAdapter.adapt(bukkitLocation.getWorld());
        BlockVector3 blockVector = BukkitAdapter.asBlockVector(bukkitLocation);

        // Check if the player has build rights in the region
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(worldEditWorld);
        return (regionManager == null || regionManager.getApplicableRegions(blockVector).testState(worldGuardPlugin.wrapPlayer(player), Flags.BUILD));
    }
    
}
