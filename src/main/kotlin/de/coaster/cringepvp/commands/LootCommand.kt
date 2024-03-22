package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.managers.ItemManager
import de.fruxz.stacked.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "addloot",
    description = "Fügt loot hinzu",
    permission = "cringepvp.loot",
    permissionDefault = PermissionDefault.OP,
    aliases = ["setloot"]
)
class LootCommand : CommandExecutor, TabCompleter {


    /**
     * /rank <player> <rank>
     *
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true

        if(args.isEmpty()) {
            sender.sendMessage("/addloot <rarity>")
            return true
        }

        val rarity = Rarity.values().find { it.name.equals(args[0], true) }
        if(rarity == null) {
            sender.sendMessage("§cDiese Rarity existiert nicht!")
            return true
        }

        // Get Item in hand of player
        val item = sender.inventory.itemInMainHand
        if(item.type == org.bukkit.Material.AIR) {
            sender.sendMessage("§cDu musst ein Item in deinem Hand haben!")
            return true
        }

        ItemManager.addItem(rarity, item)
        sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Item zum Loot von</gray> <${rarity.color}>${rarity.name} <gray>hinzugefügt.</gray>"))

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        return when(args.size) {
            1 -> {
                Rarity.values().sortedBy { it.rarity }.map { it.name }.filter { it.startsWith(args[0]) }
            }
            else -> {
                listOf()
            }
        }
    }
}