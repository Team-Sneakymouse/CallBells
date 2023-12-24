package net.callbells.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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

}
