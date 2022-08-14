package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.enums.CringeItems
import de.coaster.cringepvp.managers.CoroutineManager
import org.bukkit.block.data.type.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot


class CrateListener : Listener {

    @EventHandler
    fun onClickOnCreate(event: PlayerInteractEvent) = with(event) {
        if(clickedBlock == null) return@with
        if(clickedBlock?.blockData !is Chest) return@with
        if (action != Action.RIGHT_CLICK_BLOCK) return@with
        if(hand != EquipmentSlot.HAND) return@with
        isCancelled = true

        CoroutineManager.shootItems(clickedBlock!!.location.add(0.0, 1.2, 0.0), player, arrayOf(CringeItems.GOLD.asItem(), CringeItems.GOLD.asItem()))
    }

}