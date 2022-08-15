package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.isBuilder
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

class GamemodeListeners : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to break a ${block.type}")
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
    fun onDamageEntity(event: EntityDamageByEntityEvent) = with(event) {
        if (damager !is Player) return@with
        if (entity is LivingEntity) return@with
        val player = damager as Player
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to damage entity ${event.entity.type}")
    }

    @EventHandler
    fun onPot(event: PlayerFlowerPotManipulateEvent) = with(event) {
        if (player.isBuilder) return@with

        isCancelled = true
        println("Player ${player.name} tried to manipulate flower pot")
    }

    @EventHandler
    fun onPainting(event: HangingBreakByEntityEvent) = with(event) {
        if(remover !is Player) return@with
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