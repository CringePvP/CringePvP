package de.coaster.cringepvp.listeners

import com.destroystokyo.paper.ParticleBuilder
import de.coaster.cringepvp.enums.Rarity
import de.coaster.cringepvp.extensions.getCooldown
import de.coaster.cringepvp.extensions.isInCooldown
import de.coaster.cringepvp.extensions.setCooldown
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.CoroutineManager
import de.coaster.cringepvp.managers.ItemManager
import de.coaster.cringepvp.managers.PlayerCache
import de.moltenKt.core.extension.data.randomInt
import de.moltenKt.unfold.text
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.type.Chest
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import kotlin.time.Duration.Companion.minutes


class CrateListener : Listener {

    @EventHandler
    fun onClickOnCreate(event: PlayerInteractEvent) = with(event) {
        if(clickedBlock == null) return@with
        if(clickedBlock?.blockData !is Chest) return@with
        if (action != Action.RIGHT_CLICK_BLOCK) return@with
        if(hand != EquipmentSlot.HAND) return@with
        isCancelled = true

        val location = clickedBlock!!.location

        if(location.isInCooldown()) {
            player.sendMessage(text("<gold><b>CringePvP</b></gold> <dark_gray>×</dark_gray> <gray>Bitte warte noch ${location.getCooldown()}</gray>"))
            return@with
        }
        location.setCooldown(3.minutes)

        CoroutineManager.shootItems(clickedBlock!!.location.add(0.5, 1.2, 0.5), player, ItemManager.getItems(3))
        val chest = clickedBlock?.state as org.bukkit.block.Chest
        chest.open()
        CoroutineManager.startCoroutine({ chest.close() }, 3500)
    }

    @EventHandler
    fun onPickupItem(event: EntityPickupItemEvent) = with(event) {
        if(entity !is Player) return@with

        when (item.itemStack.type) {
            Material.GOLD_NUGGET -> {
                val itemStack = item.itemStack
                val displayName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName()).replace("[", "" ).replace("]", "")
                if (displayName.startsWith("Coin")) {
                    val amount = displayName.split("×")[1].trim().toInt()

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(coins = cringeUser.coins + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    val location = item.location.clone()
                    item.remove()

                    location.world.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.5f)
                    ParticleBuilder(Particle.BUBBLE_POP)
                        .location(location)
                        .count(randomInt(3 .. 5))
                        .offset(.35, .5, .35)
                        .extra(.01)
                        .spawn()

                    val rarityName = PlainTextComponentSerializer.plainText().serialize(itemStack.lore()?.first() ?: text(": NORMAL"))
                        .replace("[", "" )
                        .replace("]", "")
                        .split(":")[1].trim()

                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL


                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <gold><b>Coins</b></gold> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }
            Material.EMERALD -> {
                val itemStack = item.itemStack
                val displayName = PlainTextComponentSerializer.plainText().serialize(itemStack.displayName()).replace("[", "" ).replace("]", "")
                if (displayName.startsWith("Gem")) {
                    val amount = displayName.split("×")[1].trim().toInt()

                    var cringeUser = (entity as Player).toCringeUser()
                    cringeUser = cringeUser.copy(gems = cringeUser.gems + amount)
                    PlayerCache.updateCringeUser(cringeUser)

                    isCancelled = true
                    val location = item.location.clone()
                    item.remove()

                    location.world.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.5f)
                    ParticleBuilder(Particle.BUBBLE_POP)
                        .location(location)
                        .count(randomInt(3 .. 5))
                        .offset(.35, .5, .35)
                        .extra(.01)
                        .spawn()

                    val rarityName = PlainTextComponentSerializer.plainText().serialize(itemStack.lore()?.first() ?: text(": NORMAL"))
                        .replace("[", "" )
                        .replace("]", "")
                        .split(":")[1].trim()

                    val rarity = Rarity.values().find { it.name.equals(rarityName, true) } ?: Rarity.NORMAL


                    entity.sendActionBar(text("<${rarity.color}>${rarity.name} <dark_gray>»</dark_gray> <green><b>Gems</b></green> <dark_gray>×</dark_gray> <gray>$amount</gray>"))
                }
            }

            else -> {}
        }
    }
}