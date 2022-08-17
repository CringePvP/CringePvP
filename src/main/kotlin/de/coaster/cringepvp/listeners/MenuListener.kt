package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.extensions.plainText
import de.coaster.cringepvp.extensions.toCringeUser
import de.moltenKt.paper.extension.display.ui.itemStack
import de.moltenKt.unfold.text
import org.bukkit.Bukkit
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
        if (currentItem == null) return@with
        when(title){
            "Dein Menü" -> onMenuClick(this)
            "Titel" -> onTitelMenuClick(this)
        }

    }

    fun onTitelMenuClick(event: InventoryClickEvent) = with(event){
        isCancelled = true
    }

    fun onMenuClick(event: InventoryClickEvent) = with(event){
        isCancelled = true
        val player = whoClicked as Player

        when(slot) {
            11 -> {
                val titleMenu = Bukkit.createInventory(null, 36, text("Titel"))

                val cringeUser = player.toCringeUser()
                val titles = cringeUser.ownedTitles

                titles.forEachIndexed { index, title ->


                    titleMenu.setItem(
                        index,
                        Material.NAME_TAG.itemStack { editMeta { meta -> meta.displayName(text(title.name)) } })
                }
                player.openInventory(titleMenu)
            }
            15 -> player.sendMessage(text("Deine Skills"))
            else -> {}
        }
    }
}