package net.callbells.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.callbells.util.RegistryUtil;

public class BellListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check if it's a right-click event
        if (event.getAction().toString().contains("RIGHT")) {
            Player player = event.getPlayer();
            Block clickedBlock = event.getClickedBlock();

            // Check if the clicked block is a Bell
            if (clickedBlock != null && clickedBlock.getType() == Material.BELL) {
                // Handle the right-click on the Bell block
                player.sendMessage("You right-clicked a Bell!");
                // Add your custom logic here
            }
        }
    }

    @EventHandler
    public void onBellPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block placedBlock = event.getBlockPlaced();

        // Check if the placed block is a Bell
        if (placedBlock.getType() == Material.BELL) {
            // Remind the player of the "bellregister" command
            player.sendMessage("Don't forget to register this bell with /bellregister!");
            // You can add additional logic here if needed
        }
    }

    @EventHandler
    public void onBellBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        // Check if the broken block is a Bell
        if (brokenBlock.getType() == Material.BELL) {
            // Check if the bell is registered
            if (RegistryUtil.isBellRegistered(brokenBlock.getLocation())) {
                player.sendMessage("You broke a registered bell! It has been removed from the registry.");
                // Remove the bell from the registry
                RegistryUtil.removeBell(brokenBlock.getLocation());
            }
        }
    }

}
