package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.extensions.*
import de.fruxz.stacked.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.hours

@RegisterCommand(
    name = "soulbound",
    description = "Mache ein Item Soulbound (Du behälst es beim Tod)",
    permission = "",
)
class SoulboundCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true
        val cringeUser = sender.toCringeUser()

        if(!cringeUser.rank.isHigherOrEqual(Ranks.Divine)) {
            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du benötigst mindestens den</color> <color:${Ranks.Divine.color}><b>${Ranks.Divine.name}</b></color> <color:#ff7f6e>Rang für diesen Befehl.</color>"))
            return true
        }


        if (sender.isInCooldown("soulbound")) {
            val coolDown = sender.getCooldown("soulbound")
            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du musst noch $coolDown warten, bis du wieder ein Item</color> <color:#4aabff><b>Soulbound</b></color> <color:#ff7f6e>machen kannst.</color>"))
            return true
        }

        val item = sender.inventory.itemInMainHand
        if(item.type == org.bukkit.Material.AIR) {
            sender.sendMessage("§cDu musst ein Item in deinem Hand haben!")
            return true
        }

        if(item.soulbound) {
            sender.sendMessage("§cDieses Item ist bereits Soulbound!")
            return true
        }
        item.soulbound = true

        sender.toCringeUser().setCooldown("soulbound", 3.hours)
        sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#4aabff><b>Soulbound</b></color> <color:#ff7f6e>für das aktuelle Item aktiviert!</color>"))

        return true
    }
}