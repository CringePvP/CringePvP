package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.isBuilder
import de.coaster.cringepvp.extensions.loadInventory
import de.coaster.cringepvp.extensions.saveInventory
import de.moltenKt.unfold.text
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "build",
    description = "Build command",
    permission = "cringepvp.build",
    permissionDefault = PermissionDefault.OP
)
class BuildCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command!")
            return true
        }

        val buildMode = !sender.isBuilder
        sender.isBuilder = buildMode

        if (sender.isBuilder) {
            sender.sendMessage(text("<#55efc4>Du bist nun im Baumodus!"))
            sender.saveInventory(GameMode.ADVENTURE)
            sender.gameMode = GameMode.CREATIVE
            sender.loadInventory(GameMode.CREATIVE)
        } else {
            sender.sendMessage(text("<#55efc4>Du bist nun im Spielmodus!"))
            sender.saveInventory(GameMode.CREATIVE)
            sender.gameMode = GameMode.ADVENTURE
            sender.loadInventory(GameMode.ADVENTURE)
        }

        return true
    }
}