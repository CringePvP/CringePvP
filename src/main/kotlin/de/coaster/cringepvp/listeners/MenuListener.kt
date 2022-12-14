package de.coaster.cringepvp.listeners

import de.coaster.cringepvp.commands.ROW_SIZE
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.enums.Currency
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.failSoundExecution
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
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1

class MenuListener : Listener {

    private fun unit() {}

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) = with(event) {
        val titleComponent = view.title()
        val title = titleComponent.plainText
        if (currentItem == null) return@with
        when(title){
            "Dein Menü" -> onMenuClick(this)
            "Titel" -> onTitelMenuClick(this)
            "Skills" -> onSkillsMenuClick(this)
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

    private fun onSkillsMenuClick(event: InventoryClickEvent) = with(event) {
        isCancelled = true
        val player = whoClicked as Player
        val cringeUser = player.toCringeUser()
        val ref: Triple<KFunction<Unit>, Currency, Long> = when(slot) {
            10 -> Triple(cringeUser::upgradeToNextAttack, Currency.COINS, cringeUser.getPriceForNextAttack())
            12 -> Triple(cringeUser::upgradeToNextDefense, Currency.COINS, cringeUser.getPriceForNextDefense())
            14 -> Triple(cringeUser::upgradeToNextSpeed, Currency.GEMS, cringeUser.getPriceForNextSpeed())
            16 -> Triple(cringeUser::upgradeToNextHealth, Currency.GEMS, cringeUser.getPriceForNextHealth())
            else -> Triple(::unit, Currency.COINS, 0)
        }

        if (ref.third == 0L) return@with
        if (ref.second.reference.get(cringeUser) >= ref.third) {
            ref.first.call()
            ref.second.reference.set(cringeUser, ref.second.reference.get(cringeUser) - ref.third)
            PlayerCache.updateCringeUser(cringeUser)
            player.soundExecution()
            openSkillsInventory(player, cringeUser)
        } else {
            player.failSoundExecution()
        }
    }

    private fun onMenuClick(event: InventoryClickEvent) = with(event){
        isCancelled = true
        val player = whoClicked as Player
        val cringeUser = player.toCringeUser()

        when(slot) {
            11 -> {
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
            15 -> {
                openSkillsInventory(player, cringeUser)
            }
            else -> {}
        }
    }

    private fun openSkillsInventory(player: Player, cringeUser: CringeUser) {
        val skillMenu = Bukkit.createInventory(null, 9 * 3, text("<#ff7f50>Skills"))
        skillMenu.setItem(10, Material.DIAMOND_SWORD.itemStack { editMeta { meta ->
            meta.displayName(text("<#ff7f50>Angriffsschaden"))
            meta.lore(listOf(
                text("<#aaaaaa>Aktuell: <#ff7f50>${cringeUser.baseAttack}"),
                text("<#aaaaaa>Aktuelles Level: <#ff7f50>${cringeUser.attackLevel}"),
                text(" "),
                text("<#aaaaaa>Upgrade: <#ff7f50>${cringeUser.getPriceForNextAttack()} Coins"),
                text("<#aaaaaa>Wird zu: <#ff7f50>${cringeUser.getNextAttack()} Angriffsschaden")
            ))
        } })

        skillMenu.setItem(12, Material.SHIELD.itemStack { editMeta { meta ->
            meta.displayName(text("<#ff7f50>Verteidigung"))
            meta.lore(listOf(
                text("<#aaaaaa>Aktuell: <#ff7f50>${cringeUser.baseDefense}"),
                text("<#aaaaaa>Aktuelles Level: <#ff7f50>${cringeUser.defenseLevel}"),
                text(" "),
                text("<#aaaaaa>Upgrade: <#ff7f50>${cringeUser.getPriceForNextDefense()} Coins"),
                text("<#aaaaaa>Wird zu: <#ff7f50>${cringeUser.getNextDefense()} Verteidigung")
            ))
        } })

        skillMenu.setItem(14, Material.LEATHER_BOOTS.itemStack { editMeta { meta ->
            meta.displayName(text("<#ff7f50>Laufgeschwindigkeit"))
            meta.lore(listOf(
                text("<#aaaaaa>Aktuell: <#ff7f50>${cringeUser.baseSpeed}"),
                text("<#aaaaaa>Aktuelles Level: <#ff7f50>${cringeUser.speedLevel}"),
                text(" "),
                text("<#aaaaaa>Upgrade: <#ff7f50>${cringeUser.getPriceForNextSpeed()} Gems"),
                text("<#aaaaaa>Wird zu: <#ff7f50>${cringeUser.getNextSpeed()} Laufgeschwindigkeit")
            ))
        } })

        skillMenu.setItem(16, Material.APPLE.itemStack { editMeta { meta ->
            meta.displayName(text("<#ff7f50>Leben"))
            meta.lore(listOf(
                text("<#aaaaaa>Aktuell: <#ff7f50>${cringeUser.baseHealth}"),
                text("<#aaaaaa>Aktuelles Level: <#ff7f50>${cringeUser.healthLevel}"),
                text(" "),
                text("<#aaaaaa>Upgrade: <#ff7f50>${cringeUser.getPriceForNextHealth()} Gems "),
                text("<#aaaaaa>Wird zu: <#ff7f50>${cringeUser.getNextHealth()} Leben")
            ))
        } })

        player.openInventory(skillMenu)
    }
}