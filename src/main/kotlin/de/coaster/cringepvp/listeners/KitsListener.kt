package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.enums.Kits
import de.coaster.cringepvp.extensions.hasKitSelected
import de.coaster.cringepvp.extensions.plainText
import de.moltenKt.paper.extension.display.ui.addItems
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent

class KitsListener : Listener {



    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) = with(event) {
        player.hasKitSelected = false
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = with(event) {
        val titleComponent = view.title()
        val title = titleComponent.plainText
        if (title != "Kits") return@with
        isCancelled = true
        if (currentItem == null) return@with
        val player = whoClicked as Player
        val kitString = currentItem!!.itemMeta!!.displayName()!!.plainText.replace(" Kit", "")
        val kit = Kits.valueOf(kitString)

        player.hasKitSelected = true
        player.inventory.addItems(*kit.items)
        player.closeInventory()
    }
}

