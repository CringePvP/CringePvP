package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.convertAreaToString
import de.coaster.cringepvp.extensions.toCringeLocation
import de.coaster.cringepvp.extensions.toCringeString
import de.coaster.cringepvp.managers.CoroutineManager.startCoroutine
import de.coaster.cringepvp.utils.CringePath
import de.coaster.cringepvp.utils.FileConfig
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

class JumpPadListener : Listener {


    // You never do this. Only I do this. This is awful.
    @EventHandler
    fun onMoveEvent(event: PlayerMoveEvent) = with(event) {
        // Test if block below player is a slime_block
        val block = player.location.add(0.0, -1.0, 0.0).block
        if(block.type == org.bukkit.Material.SLIME_BLOCK) {
            // Get the full slime x,z area
            var minSlimeBlockX = 0
            while(block.location.add(minSlimeBlockX.toDouble() - 1, 0.0, 0.0).block.type == Material.SLIME_BLOCK) { minSlimeBlockX -= 1 }

            var maxSlimeBlockX = 0
            while(block.location.add(maxSlimeBlockX.toDouble() + 1, 0.0, 0.0).block.type == Material.SLIME_BLOCK) { maxSlimeBlockX += 1 }

            var minSlimeBlockZ = 0
            while(block.location.add(0.0, 0.0, minSlimeBlockZ.toDouble() - 1).block.type == Material.SLIME_BLOCK) { minSlimeBlockZ -= 1 }

            var maxSlimeBlockZ = 0
            while(block.location.add(0.0, 0.0, maxSlimeBlockZ.toDouble() + 1).block.type == Material.SLIME_BLOCK) { maxSlimeBlockZ += 1 }

            // Get the full slime area
            val slimeAreaKey = convertAreaToString(Pair(block.location.add(minSlimeBlockX.toDouble(), 0.0, minSlimeBlockZ.toDouble()), block.location.add(maxSlimeBlockX.toDouble(), 0.0, maxSlimeBlockZ.toDouble())))
            // Get from config
            val config = FileConfig("jump_pads.yml")
            if (config.contains(slimeAreaKey)) {
                // Convert to CringePath
                val path = CringePath.fromConfig(config, slimeAreaKey)
                val progress = CringePath.Progress(player, path)

                // Start the path
                progress.startTraveling()
            } else {
                config.set(slimeAreaKey, block.location.toCringeString())
                config.saveConfig()
            }
        }
    }


}