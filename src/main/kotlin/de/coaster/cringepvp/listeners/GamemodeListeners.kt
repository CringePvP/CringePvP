package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.*
import de.coaster.cringepvp.managers.PlayerCache
import de.fruxz.stacked.text
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class GamemodeListeners : Listener {

    companion object {
        var kristallFuellstand = 10
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) = with(event) {
        if (player.isBuilder) return@with
        isCancelled = true

        if (block.type == Material.AMETHYST_CLUSTER) {
            if(block.location.toCringeString() == "sky_-20_75_176") {
                // if kristallFuellstand != 0 -> add one crystal to user and remove 1 kristallfuelstand
                if (kristallFuellstand > 0) {
                    var cringeUser = player.toCringeUser()
                    cringeUser = cringeUser.copy(crystals = cringeUser.crystals + (1 abbreviate 0), xp = cringeUser.xp + 3)
                    PlayerCache.updateCringeUser(cringeUser)
                    player.sendActionBar(text("<color:#4aabff><b>Kristall</b></color> <dark_gray>×</dark_gray> <gray>1</gray>"))
                    kristallFuellstand--
                }
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to place a ${blockPlaced.type}")
    }

    @EventHandler
    fun onBlockMultiPlace(event: BlockMultiPlaceEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to multiplace a ${blockPlaced.type}")
    }

    @EventHandler
    fun onInteractFarmland(event: PlayerInteractEvent) = with(event) {
        if (player.isBuilder) return@with

        if(action == Action.PHYSICAL && clickedBlock?.type == org.bukkit.Material.FARMLAND) {
            isCancelled = true
            println("Player ${player.name} tried to interact with a ${clickedBlock?.type?.name ?: "null"}")
        }
    }

    @EventHandler
    fun onInteractEntity(event: PlayerInteractAtEntityEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to interact at entity ${event.rightClicked.type}")
    }

    @EventHandler
    fun onInteractEntity(event: PlayerInteractEntityEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to interact with entity ${event.rightClicked.type}")
    }

    @EventHandler
    fun onPot(event: PlayerFlowerPotManipulateEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to manipulate flower pot")
    }

    @EventHandler
    fun onPainting(event: HangingBreakByEntityEvent) = with(event) {
        if(remover !is Player) {
            isCancelled = true
            return@with
        }
        val player = remover as Player
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to break painting")
    }

    @EventHandler
    fun onBlockForm(event: BlockFormEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBucketEmtpy(event: PlayerBucketEmptyEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to empty bucket")
    }
}