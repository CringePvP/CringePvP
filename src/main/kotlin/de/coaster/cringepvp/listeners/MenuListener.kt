package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.plainText
import de.moltenKt.unfold.text
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class MenuListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = with(event) {
        val titleComponent = view.title()
        val title = titleComponent.plainText
        if (title != "Dein Menü") return@with
        isCancelled = true
        if (currentItem == null) return@with
        if (currentItem!!.type != Material.CHEST) return@with
        val player = whoClicked as Player

        when(slot) {
            11 -> player.sendMessage(text("Deine Titel"))
            15 -> player.sendMessage(text("Deine Skills"))
        }
    }

}