package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.enums.Kits
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.extensions.*
import de.coaster.cringepvp.managers.PlayerCache
import de.moltenKt.paper.extension.display.ui.addItems
import de.moltenKt.unfold.text
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent

class KitsListener : Listener {

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

        if(player.isInCooldown("kit.${kit.name}")) {
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Bitte warte noch ${player.getCooldown("kit.${kit.name}")}, bis du dieses Kit wieder verwendest.</gray>"))
            return@with
        }

        val cringeUser = player.toCringeUser()

        if(!cringeUser.rank.isHigherOrEqual(kit.minRank)) {
            player.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du benötigst mindestens den</color> <color:${kit.minRank.color}><b>${kit.minRank.name}</b></color> <color:#ff7f6e>Rang für diesen Befehl.</color>"))
            return@with
        }

        if(kit.kaufPreis != 0) {
            if(kit.currency.reference.get(cringeUser) < kit.kaufPreis) {
                player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast nicht genügend ${kit.currency.display.plainText} um dieses Kit zu kaufen.</gray>"))
                return@with
            }
            kit.currency.reference.set(cringeUser, kit.currency.reference.get(cringeUser) - kit.kaufPreis)
            PlayerCache.updateCringeUser(cringeUser)
        }

        cringeUser.setCooldown("kit.${kit.name}", kit.cooldown)

        player.inventory.addItems(*kit.items)
        player.closeInventory()
    }
}

