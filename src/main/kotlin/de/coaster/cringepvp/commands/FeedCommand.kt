package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.getCooldown
import de.coaster.cringepvp.extensions.isInCooldown
import de.coaster.cringepvp.extensions.setCooldown
import de.coaster.cringepvp.extensions.toCringeUser
import dev.fruxz.stacked.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.minutes

@RegisterCommand(
    name = "feed",
    description = "Feed yourself",
    permission = "",
    aliases = ["eat", "essen"]
)
class FeedCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true


        if (sender.isInCooldown("feed")) {
            val coolDown = sender.getCooldown("feed")
            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du musst noch $coolDown warten, bis du dich wieder vollfressen kannst.</color>"))
            return true
        }
        sender.toCringeUser().setCooldown("feed", 5.minutes)

        sender.foodLevel = 20
        sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du hast dich erfolgreich gefüttert.</color>"))


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