package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Kits
import de.coaster.cringepvp.extensions.hasKitSelected
import de.coaster.cringepvp.extensions.toCringeUser
import de.moltenKt.core.extension.math.ceil
import de.moltenKt.paper.extension.paper.player
import de.moltenKt.unfold.extension.replace
import de.moltenKt.unfold.text
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack


const val ROW_SIZE = 7

@RegisterCommand(
    name = "kits",
    description = "Kits",
    permission = "",
    aliases = ["kit"]
)
class KitsCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return true

        if (sender.hasKitSelected) {
            sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast bereits ein Kit ausgewählt. Du kannst nur einmal pro Leben ein Kit wählen.</gray>"))
            return true
        }

        val kitAmount = Kits.values().size
        val rowAmount = (kitAmount.toDouble() / ROW_SIZE.toDouble()).ceil().toInt()
        val inventory = Bukkit.createInventory(null, 9 * (rowAmount + 2), text("<color:#4aabff><b>Kits</b></color>"))
        val invScreen = mutableMapOf<Pair<Int, Int>, ItemStack>()

        val cringeUser = sender.toCringeUser()

        Kits.values().forEachIndexed { index, kit ->
            val item = kit.icon.clone().apply { editMeta { meta -> meta.lore(
                kit.items.map { item ->
                    text("<color:#4aabff>${item.amount}x %item%</color>").replace("%item%", item.displayName())
                }.toMutableList().
                also { list ->
                    list.add(text(" "))
                    if(!cringeUser.rank.isHigherOrEqual(kit.minRank)) {
                        list.add(text("<#ff0000>Du benötigst einen Rang von <${kit.minRank.color}>${kit.minRank.name} <#ff0000>oder höher!"))
                        return@also
                    }

                }
            ) }}
            invScreen[Pair(index % ROW_SIZE, index / ROW_SIZE)] = item
        }
        invScreen.forEach { (pos, item) -> inventory.setItem((pos.second + 1) * 9 + (pos.first + 1), item) }
        sender.openInventory(inventory)

        return true
    }
}