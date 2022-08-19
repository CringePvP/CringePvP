package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
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

        if(args.size < 3) {
            sender.sendMessage("/settitle <player> <set/add/remove> <title>")
            return true
        }

        val targetPlayer = sender.server.getOfflinePlayer(args[0])
        var targetCringeUser = targetPlayer.uniqueId.toCringeUser()

        val action = args[1]
        val title = args[2]

        val realTitle= Titles.values().find { it.name.equals(title, true) }
        if(realTitle == null) {
            sender.sendMessage("§cDieser Title existiert nicht!")
            return true
        }

        when(action) {
            "set" -> {
                targetCringeUser = targetCringeUser.copy(title = realTitle)
                PlayerCache.updateCringeUser(targetCringeUser)
                if(!targetPlayer.isOnline) {
                    PlayerCache.remove(targetPlayer.uniqueId)
                } else {
                    targetPlayer.player?.sendMessage("§aDein Title wurde auf §e${realTitle.display}§a geändert.")
                }
                sender.sendMessage("§aTitle von ${targetPlayer.name} auf ${realTitle.display} gesetzt")
            }
            "add" -> {
                targetCringeUser = targetCringeUser.copy(ownedTitles = targetCringeUser.ownedTitles + realTitle)
                PlayerCache.updateCringeUser(targetCringeUser)
                if(!targetPlayer.isOnline) {
                    PlayerCache.remove(targetPlayer.uniqueId)
                } else {
                    targetPlayer.player?.sendMessage("§aDir wurde der Title §e${realTitle.display}§a hinzugefügt. Rüste ihn jetzt in /menu aus.")
                }
                sender.sendMessage("§aTitle ${realTitle.display} zu ${targetPlayer.name} hinzugefügt.")
            }
            "remove" -> {
                targetCringeUser = targetCringeUser.copy(ownedTitles = targetCringeUser.ownedTitles - realTitle)
                if(targetCringeUser.title == realTitle) {
                    targetCringeUser = targetCringeUser.copy(title = Titles.NoTITLE)
                }
                PlayerCache.updateCringeUser(targetCringeUser)
                if(!targetPlayer.isOnline) {
                    PlayerCache.remove(targetPlayer.uniqueId)
                } else {
                    targetPlayer.player?.sendMessage("§aDir wurde der Title §e${realTitle.display}§a entfernt.")
                }
                sender.sendMessage("§aTitle ${realTitle.display} von ${targetPlayer.name} entfernt.")
            }
            else -> {
                sender.sendMessage("§cDieser Action existiert nicht!")
            }
        }
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
                listOf("set", "add", "remove").filter { it.startsWith(args[1]) }
            }
            3 -> {
                Titles.values().map { it.name }.filter { it.startsWith(args[2]) }
            }
            else -> {
                listOf()
            }
        }
    }
}