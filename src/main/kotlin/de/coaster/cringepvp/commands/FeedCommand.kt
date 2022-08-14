package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.moltenKt.unfold.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault

@RegisterCommand(
    name = "feed",
    description = "Feed yourself",
    permission = "",
    aliases = ["eat", "essen"]
)
class FeedCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true

//        val coolDown = sender.getCooldown("feed")
//        if (coolDown != null && !coolDown.isOver) {
//            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du musst noch ${coolDown.remainingTime} warten, bis du dich wieder vollfressen kannst.</color>"))
//            return true
//        }
        sender.foodLevel = 20
        sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du hast dich erfolgreich gefüttert.</color>"))
//        sender.setCooldown("feed", 20 * 60 * 5).startCooldown()

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String> {
        return listOf(
            "feed",
            "eat",
            "essen"
        ).filter { it.startsWith(args.last()) }
    }
}