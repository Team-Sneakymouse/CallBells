package net.callbells.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Bell;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.callbells.CallBells;
import net.callbells.commands.CommandBellRegister;
import net.callbells.util.ChatUtility;
import net.callbells.util.RegistryUtil;

public class BellListener implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //if (event.useInteractedBlock().equals(Result.DENY)) return;

        Player player = event.getPlayer();

        // Check if it's a right-click event
        if (event.getAction().toString().contains("RIGHT")) {
            Block clickedBlock = event.getClickedBlock();

            // Check if the clicked block is a Bell
            if (clickedBlock != null) {
                Location loc = clickedBlock.getLocation();
                if (clickedBlock.getType() == Material.BELL && RegistryUtil.isBellRegistered(loc)) {
                    if (event.useInteractedBlock().equals(Result.DENY)) ((Bell) clickedBlock.getState()).ring();

                    // Check if the player is on cooldown
                    if (cooldowns.containsKey(player.getUniqueId())) {
                        long lastInteractTime = cooldowns.get(player.getUniqueId());
                        long currentTime = System.currentTimeMillis();
                        long cooldownTime = CallBells.getInstance().getConfig().getInt("bellCooldown") * 1000; // 30 seconds in milliseconds

                        if (currentTime - lastInteractTime < cooldownTime) {
                            return;
                        }
                    }

                    String name = RegistryUtil.getBellName(loc);
                    boolean rung = false;

                    for (UUID uuid : RegistryUtil.getBellOwners(loc)) {
                        Player pl = Bukkit.getPlayer(uuid);
                        if (pl != null && pl.isOnline()) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cast forcecast " + pl.getName() + " bell-rung " + name + " " + player.getName());
                            rung = true;
                        }
                    }

                    if (rung) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cast forcecast " + player.getName() + " bell-ring-success " + name);
                    } else {
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cast forcecast " + player.getName() + " bell-ring-failure " + name);
                    }

                    // Set the cooldown
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onBellPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block placedBlock = event.getBlockPlaced();

        // Check if the placed block is a Bell
        if (placedBlock.getType() == Material.BELL && (player.isOp() || player.hasPermission(CommandBellRegister.PERMISSION) || player.hasPermission(CallBells.IDENTIFIER + ".*") || player.hasPermission(CallBells.IDENTIFIER + ".command.*"))) {
            // Remind the player of the "bellregister" command
            player.sendMessage(ChatUtility.convertToComponent("&eDon't forget to register this bell with /bellregister!"));
            // You can add additional logic here if needed
        }
    }

    @EventHandler
    public void onBellBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        // Check if the broken block is a Bell
        if (brokenBlock.getType() == Material.BELL) {
            // Check if the bell is registered
            if (RegistryUtil.isBellRegistered(brokenBlock.getLocation())) {
                player.sendMessage(ChatUtility.convertToComponent("&eYou broke a registered bell! It has been removed from the registry."));
                // Remove the bell from the registry
                RegistryUtil.removeBell(brokenBlock.getLocation());
            }
        }
    }

}
