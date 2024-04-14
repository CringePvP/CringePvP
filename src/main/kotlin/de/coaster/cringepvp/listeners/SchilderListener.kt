package de.coaster.cringepvp.listeners

import com.destroystokyo.paper.MaterialTags
import de.coaster.cringepvp.utils.toItemBuilder
import dev.fruxz.stacked.text
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

class SchilderListener: Listener {

    @EventHandler
    fun onSignCreate(event: SignChangeEvent) = with(event) {
        if (player.hasPermission("cringepvp.sign.create")) {
            if (lines[0].equals("[Supply]", true)) {
                player.sendMessage("§aDu hast ein Schild erstellt!")

                line(0, text("<color:#91d3ff>・ Supply ・</color>"))

                val item = lines[1]
                val material = Material.matchMaterial(item) ?: return@with player.sendMessage("§cMaterial nicht gefunden!")
                val amount = lines[2].toIntOrNull() ?: 1

                line(1, text("<color:#eccc68>${material.name}</color>"))
                line(2, text("<color:#eccc68>${amount}</color>"))
                line(3, text("<color:#ffa42e><bold>© CringePvP</bold></color>"))
            } else if (lines[0].equals("[Müll]", true)) {
                player.sendMessage("§aDu hast ein Schild erstellt!")

                line(0, text("<color:#91d3ff>・ Mülleimer ・</color>"))

                line(1, text("<color:#8da0aa><i>  Werf alles in</i></color>"))
                line(2, text("<color:#8da0aa><i>  micht rein.</i></color>"))


                line(3, text("<color:#ffa42e><bold>© CringePvP</bold></color>"))
            }
        }
    }

    @EventHandler
    fun onSignInteract(event: PlayerInteractEvent) = with(event) {
        if (clickedBlock == null) return@with
        val block = clickedBlock!!
        if(!MaterialTags.SIGNS.isTagged(block)) return@with
        if (action != Action.RIGHT_CLICK_BLOCK) return@with

        val sign = block.state as org.bukkit.block.Sign
        val lines = sign.getTargetSide(player).lines()
        with(lines[0]) {
            when {
                equals(text("<color:#91d3ff>・ Mülleimer ・</color>")) -> {
                    isCancelled = true
                    player.openInventory(Bukkit.createInventory(null, 9, text("<color:#91d3ff>・ Mülleimer ・</color>")))
                    player.playSound(player.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
                }
                equals(text("<color:#91d3ff>・ Supply ・</color>")) -> {
                    isCancelled = true
                    val item = lines[1]
                    val plain = PlainTextComponentSerializer.plainText().serialize(item)
                    val material = Material.matchMaterial(plain) ?: return player.sendMessage("§cMaterial nicht gefunden!")
                    val amount = PlainTextComponentSerializer.plainText().serialize(lines[2]).toIntOrNull() ?: 1
                    player.inventory.addItem(material.toItemBuilder().asQuantity(amount).build())
                    player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                }
                else -> {}
            }
        }
    }
}