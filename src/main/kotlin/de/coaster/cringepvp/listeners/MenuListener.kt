package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.commands.ROW_SIZE
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.plainText
import de.coaster.cringepvp.extensions.soundExecution
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import de.moltenKt.core.extension.math.ceil
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

    private fun onTitelMenuClick(event: InventoryClickEvent) = with(event){
        isCancelled = true
        val player = whoClicked as Player
        var cringeUser = player.toCringeUser()
        val titles = cringeUser.ownedTitles

        val titleName = currentItem?.displayName()?.plainText ?: return@with
        val title = titles.firstOrNull { it.display == titleName } ?: Titles.NoTITLE
        cringeUser = cringeUser.copy(title = title)
        PlayerCache.updateCringeUser(cringeUser)
        player.closeInventory()
        player.soundExecution()
        player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast den Titel <gold>${title.display}</gold> ausgewählt.</gray>"))
    }

    private fun onMenuClick(event: InventoryClickEvent) = with(event){
        isCancelled = true
        val player = whoClicked as Player

        when(slot) {
            11 -> {
                val cringeUser = player.toCringeUser()
                val titles = cringeUser.ownedTitles

                val rowAmount = ((titles.size + 2).toDouble() / ROW_SIZE.toDouble()).ceil().toInt()

                val titleMenu = Bukkit.createInventory(null, 9 * (rowAmount), text("<#ff7f50>Titel"))

                titles.forEachIndexed { index, title ->
                    val rarity = title.rarity
                    titleMenu.setItem(
                        index,
                        Material.NAME_TAG.itemStack { editMeta { meta ->
                            meta.displayName(text("<${rarity.color}>${title.display}"))
                            meta.lore(title.description.split("\n").map { text("<gray>$it") } + listOf(
                                text(" "),
                                text("<#aaaaaa>Rarity: <${rarity.color}>${rarity.name}")
                            ))
                        } })
                }

                titleMenu.setItem((rowAmount * 9) - 1, Material.BARRIER.itemStack { editMeta { meta ->
                    meta.displayName(text("<${Titles.NoTITLE.rarity.color}>${Titles.NoTITLE.display}"))
                    meta.lore(listOf(
                        text("<gray>${Titles.NoTITLE.description}"),
                        text(" "),
                        text("<#aaaaaa>Rarity: <${Titles.NoTITLE.rarity.color}>${Titles.NoTITLE.rarity.name}")
                    ))
                } })

                player.openInventory(titleMenu)
            }
            15 -> player.sendMessage(text("Deine Skills"))
            else -> {}
        }
    }
}