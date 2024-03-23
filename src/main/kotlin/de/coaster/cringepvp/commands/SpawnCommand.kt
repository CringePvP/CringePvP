package de.coaster.cringepvp.commands

import de.coaster.cringepvp.CringePvP
import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.failSoundExecution
import de.coaster.cringepvp.extensions.soundExecution
import de.coaster.cringepvp.extensions.toCringeString
import de.coaster.cringepvp.managers.CoroutineManager
import dev.fruxz.stacked.text
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds

@RegisterCommand(
    name = "spawn",
    description = "Spawn command",
    permission = "",
    aliases = ["home", "hub", "back", "lobby"],
)
class SpawnCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command!")
            return true
        }

        sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du wirst in 3 Sekunden zum Spawn teleportiert. Bitte bewege dich nicht."))

        var locString = ""

        CoroutineManager.startCoroutine({
           locString = sender.location.toCringeString()
        }, 1.seconds.inWholeMilliseconds)

        Bukkit.getScheduler().runTaskLater(CringePvP.instance, Runnable {
            if(sender.location.toCringeString() == locString) {
                sender.teleport(sender.world.spawnLocation.add(0.5, 0.0, 0.5))
                sender.soundExecution()
                sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du wurdest zum Spawn teleportiert."))
            } else {
                sender.failSoundExecution()
                sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <red>Abbruch. Du hast dich bewegt. kek"))
            }
        }, 3L * 20)

        return true
    }
}