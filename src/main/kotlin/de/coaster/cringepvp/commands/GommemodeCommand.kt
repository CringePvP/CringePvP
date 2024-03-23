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
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.time.Duration.Companion.minutes

@RegisterCommand(
    name = "gommemode",
    description = "Geh in den Gommemode",
    permission = "",
)
class GommemodeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender !is Player) return true


        if (sender.isInCooldown("gommemode")) {
            val coolDown = sender.getCooldown("gommemode")
            sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <color:#ff7f6e>Du musst noch $coolDown warten, bis du wieder in den</color> <b><gold>#Gommemode</gold></b> <color:#ff7f6e>kannst.</color>"))
            return true
        }
        sender.toCringeUser().setCooldown("gommemode", 5.minutes)
        sender.sendMessage(text("<color:#adffcd>CringePvP »</color> <b><gold>#Gommemode</gold></b> <color:#ff7f6e>aktiviert!</color>"))

        sender.addPotionEffects(listOf(
            PotionEffect(PotionEffectType.SPEED, 20*20, 1),
            PotionEffect(PotionEffectType.JUMP, 20*20, 1),
            PotionEffect(PotionEffectType.HEALTH_BOOST, 20*20, 1),
            PotionEffect(PotionEffectType.ABSORPTION, 20*20, 1),
            PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*20, 1),
            PotionEffect(PotionEffectType.GLOWING, 20*20, 1),
        ))

        return true
    }
}