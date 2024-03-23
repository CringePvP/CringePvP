package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import dev.fruxz.stacked.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

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