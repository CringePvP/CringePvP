package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.ItemManager
import de.coaster.cringepvp.managers.PlayerCache
import de.moltenKt.unfold.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import java.util.*

@RegisterCommand(
    name = "rename",
    description = "Ändere den Namen von Items",
    permission = "cringepvp.rename",
    permissionDefault = PermissionDefault.OP
)
class RenameCommand : CommandExecutor {

    /**
     * /rank <player> <rank>
     *
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true

        if(args.isEmpty()) {
            sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>/rename <Neuer Name als <click:open_url:'https://webui.adventure.kyori.net/'><hover:show_text:'Öffne den WebEditor'><b>MiniMessage</b></hover></click></gray>"))
            return true
        }

        val neuerName = args.joinToString(" ")


        // Get Item in hand of player
        val item = sender.inventory.itemInMainHand
        if(item.type == org.bukkit.Material.AIR) {
            sender.sendMessage("§cDu musst ein Item in deinem Hand haben!")
            return true
        }

        item.editMeta { meta ->
            meta.displayName(text(neuerName))
        }
        sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Name zu</gray> <gold>${neuerName}</gold> <gray>geändert.</gray>"))

        return true
    }
}