package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "cringetitle",
    description = "Setzt den Title eines Spielers",
    permission = "cringepvp.title",
    permissionDefault = PermissionDefault.OP,
    aliases = ["settitle"]
)
class TitleCommand : CommandExecutor, TabCompleter {


    /**
     * /rank <player> <rank>
     *
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(args.size < 2) {
            sender.sendMessage("/title <player> <rank>")
            return true
        }

        val targetPlayer = sender.server.getOfflinePlayer(args[0])
        var targetCringeUser = targetPlayer.uniqueId.toCringeUser()
        val title = args[1]

        val realTitle= Titles.values().find { it.name.equals(title, true) }
        if(realTitle == null) {
            sender.sendMessage("§cDieser Title existiert nicht!")
            return true
        }

        targetCringeUser = targetCringeUser.copy(title = realTitle.name)
        PlayerCache.updateCringeUser(targetCringeUser)
        if(!targetPlayer.isOnline) {
            PlayerCache.remove(targetPlayer.uniqueId)
        } else {
            targetPlayer.player?.sendMessage("§aDein Title wurde auf §e${realTitle.display}§a geändert.")
        }

        sender.sendMessage("§aRang von ${targetPlayer.name} auf ${realTitle.display} gesetzt")

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
                sender.server.onlinePlayers.map { it.name }.filter { it.startsWith(args[0]) }
            }
            2 -> {
                Titles.values().map { it.name }.filter { it.startsWith(args[1]) }
            }
            else -> {
                listOf()
            }
        }
    }
}