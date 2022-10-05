package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import de.fruxz.stacked.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "upgrade",
    description = "Debug command",
    permission = "cringepvp.build",
    permissionDefault = PermissionDefault.OP
)
class UpgradeDebugCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command!")
            return true
        }

        var cringeUser = sender.toCringeUser()

        val amount = if(args.size == 1) args[0].toInt() else 1

        cringeUser = cringeUser.copy(steinbruchLevel = cringeUser.steinbruchLevel + amount)
        PlayerCache.updateCringeUser(cringeUser)

        sender.sendMessage(text ("You are now level ${cringeUser.steinbruchLevel}  (+$amount)" ))

        return true
    }
}