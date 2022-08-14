package de.coaster.cringepvp.managers

import de.coaster.cringepvp.CringePvP
import de.coaster.cringepvp.CringePvP.Companion.coroutineScope
import de.moltenKt.unfold.text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import kotlin.time.Duration.Companion.seconds
import de.coaster.cringepvp.extensions.broadcastActionbar
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

object CoroutineManager {

    init {
        println("CoroutineManager initialized")
        startPlayerTimer()
        startClearLag()
    }

    fun startCoroutine(coroutine: suspend () -> Unit) {
        coroutineScope.launch {
            coroutine()
        }
    }

    fun startCoroutine(coroutine: suspend () -> Unit, delay: Long) {
        coroutineScope.launch {
            delay(delay)
            coroutine()
        }
    }

    private fun startPlayerTimer() {
        startCoroutine {
            while (true) {
                delay(1000)
                PlayerCache.all().forEach { player ->
                    PlayerCache.updateCringeUser(player.copy(onlineTime = player.onlineTime + 1.seconds))
                }
            }
        }
    }

    private fun startClearLag() {
        startCoroutine {
            while (true) {
                delay(90000)
                broadcastActionbar(text("<gold>[</gold><green>ClearLag</green><gold>]</gold> <aqua>Es werden alle am Boden liegenden Items entfernt in 30s</aqua>"))
                delay(15000)
                broadcastActionbar(text("<dark_purple>{</dark_purple><dark_red>NoLaggg</dark_red><dark_purple>}</dark_purple> <dark_green>Es werden alle Items in 15 Sekunden gelöscht.</dark_green>"))
                delay(12000)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Löschung in 3..</gray>"))
                delay(1000)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » Löschung in 2.."))
                delay(800)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Löschung in 1,2..</gray>"))
                delay(1200)
                broadcastActionbar(text("<gold><b>CringePvP</b></gold> » <gray>Jetzt wird gelöscht!</gray>"))

                Bukkit.getScheduler().runTask(CringePvP.instance, kotlinx.coroutines.Runnable{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=minecraft:item]")
                })
            }
        }
    }

    fun shootItems(location: Location, player: Player, items: Array<ItemStack>) {
        items.forEachIndexed { index: Int, item: ItemStack ->
            Bukkit.getScheduler().runTaskLater(CringePvP.instance, kotlinx.coroutines.Runnable {
                val itemInWorld = player.world.dropItem(location, item)
                itemInWorld.velocity = location.toVector().subtract(player.location.toVector()).normalize().multiply(-0.2).add(Vector(0.0, 0.3, 0.0))
//                    .rotateAroundY(
//                        Math.toRadians(
//                            (Math.random() * 360)
//                        )
//                    )
                itemInWorld.pickupDelay = 20
                itemInWorld.setCanMobPickup(false)
                player.world.playSound(itemInWorld.location, Sound.ENTITY_CAT_HISS, 1.0f, 1.0f)
            }, 20 * index.toLong())
        }
    }

}