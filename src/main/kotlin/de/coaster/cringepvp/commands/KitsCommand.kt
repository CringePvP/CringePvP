package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Kits
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.extensions.abbreviate
import de.coaster.cringepvp.extensions.plainText
import de.coaster.cringepvp.extensions.toCringeUser
import dev.fruxz.ascend.extension.math.ceil
import dev.fruxz.stacked.extension.replace
import dev.fruxz.stacked.text
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import kotlin.time.Duration


const val SMALL_ROW_SIZE = 7
const val ROW_SIZE = 9

@RegisterCommand(
    name = "kits",
    description = "Kits",
    permission = "",
    aliases = ["kit"]
)
class KitsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return true

        val kitAmount = Kits.values().size
        val rowAmount = (kitAmount.toDouble() / SMALL_ROW_SIZE.toDouble()).ceil().toInt()
        val inventory = Bukkit.createInventory(null, 9 * (rowAmount + 2), text("<color:#4aabff><b>Kits</b></color>"))
        val invScreen = mutableMapOf<Pair<Int, Int>, ItemStack>()

        val cringeUser = sender.toCringeUser()

        Kits.values().forEachIndexed { index, kit ->
            val item = kit.icon.clone().apply { editMeta { meta -> meta.lore(
                listOf(
                    text(" "),
                    text("<color:#26de81>Preis: ${if(kit.kaufPreis == 0 abbreviate 0) "Free" else "${kit.kaufPreis} ${kit.currencyType.display.plainText}"}</color>"),
                    text("<color:#45aaf2>Cooldown: ${if(kit.cooldown == Duration.ZERO) "Kein Cooldown" else "${kit.cooldown}"}</color>"),
                    text("<color:#fed330>Rang Anforderung: ${if(kit.minRank == Ranks.Spieler) "Keine" else "<${kit.minRank.color}>${kit.minRank.name}"}</color>"),
                    text(" ")
                ) +
                kit.items.map { item ->
                    text("<color:#4aabff>${item.amount}x %item%</color>").replace("%item%", item.displayName())
                }.toMutableList().
                also { list ->
                    list.add(text(" "))
                    if(!cringeUser.rank.isHigherOrEqual(kit.minRank)) {
                        list.add(text("<#ff0000>Du benötigst einen Rang von <${kit.minRank.color}>${kit.minRank.name} <#ff0000>oder höher!"))
                    }
                    if(kit.currencyType.reference.get(cringeUser) < kit.kaufPreis) {
                        list.add(text("<#ff0000>Du hast nicht genug ${kit.currencyType.display.plainText}! <#778ca3>(<#fed330>${kit.currencyType.reference.get(cringeUser)}<#778ca3>/<#fd9644>${kit.kaufPreis}<#778ca3>)"))
                    }
                }
            ) }}
            invScreen[Pair(index % SMALL_ROW_SIZE, index / SMALL_ROW_SIZE)] = item
        }
        invScreen.forEach { (pos, item) -> inventory.setItem((pos.second + 1) * 9 + (pos.first + 1), item) }
        sender.openInventory(inventory)

        return true
    }
}