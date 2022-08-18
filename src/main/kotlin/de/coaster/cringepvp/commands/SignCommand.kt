package de.coaster.cringepvp.commands

import de.coaster.cringepvp.annotations.RegisterCommand
import de.coaster.cringepvp.extensions.getCooldown
import de.coaster.cringepvp.extensions.isInCooldown
import de.coaster.cringepvp.extensions.setCooldown
import de.moltenKt.core.tool.timing.calendar.Calendar
import de.moltenKt.unfold.text
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import java.util.*
import kotlin.time.Duration.Companion.hours

@RegisterCommand(
    name = "sign",
    description = "Signieren",
    permission = "cringepvp.sign",
    permissionDefault = PermissionDefault.OP
)
class SignCommand : CommandExecutor {

    /**
     * /rank <player> <rank>
     *
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true

        if (sender.isInCooldown("sign")){
            val coolDown = sender.getCooldown("sign")
            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du musst noch $coolDown warten, bis du wieder ein Item</color> <color:#4aabff><b>signieren</b></color> <color:#ff7f6e>kannst.</color>"))
            return true
        }

        // Get Item in hand of player
        val item = sender.inventory.itemInMainHand
        if(item.type == org.bukkit.Material.AIR) {
            sender.sendMessage("§cDu musst ein Item in deinem Hand haben!")
            return true
        }

        item.editMeta { meta ->
            val lore = meta.lore() ?: mutableListOf()
            lore.plus(
                listOf(
                    text(" "),
                    text("<color:#34ace0>Dieses Item wurde von <color:#ffb142>${sender.name}</color> signiert</color>"),
                    text("<gray>Am ${Calendar.now().getFormatted(Locale.GERMAN)}"),
                )
            )
            meta.lore(lore)
        }
        sender.setCooldown("sign", 1.hours)
        sender.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Du hast ein Item signiert.</gray>"))

        return true
    }
}