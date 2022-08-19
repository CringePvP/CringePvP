package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "rank",
    description = "Setzt den Rang eines Spielers",
    permission = "cringepvp.rank",
    permissionDefault = PermissionDefault.OP,
    aliases = ["perms"]
)
class RankCommand : CommandExecutor, TabCompleter {


    /**
     * /rank <player> <rank>
     *
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(args.size < 2) {
            sender.sendMessage("/rank <player> <rank>")
            return true
        }

        val targetPlayer = sender.server.getOfflinePlayer(args[0])
        var targetCringeUser = targetPlayer.uniqueId.toCringeUser()
        val rank = args[1]

        val realRank = Ranks.values().find { it.name.equals(rank, true) }
        if(realRank == null) {
            sender.sendMessage("§cDieser Rang existiert nicht!")
            return true
        }

        targetCringeUser = targetCringeUser.copy(rank = realRank)
        PlayerCache.updateCringeUser(targetCringeUser)
        if(!targetPlayer.isOnline) {
            PlayerCache.remove(targetPlayer.uniqueId)
        } else {
            targetPlayer.player?.sendMessage("§aDein Rang wurde auf §e${rank}§a geändert.")
        }

        sender.sendMessage("§aRang von ${targetPlayer.name} auf ${rank} gesetzt")

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
                Ranks.values().sortedByDescending { ranks: Ranks -> ranks.ordinal }.map { it.name }.filter { it.startsWith(args[1]) }
            }

            else -> {
                listOf()
            }
        }
    }
}