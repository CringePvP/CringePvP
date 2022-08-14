package de.coaster.cringepvp.listeners

import com.destroystokyo.paper.ParticleBuilder
import de.coaster.cringepvp.extensions.toCringeUser
import de.coaster.cringepvp.managers.PlayerCache
import de.coaster.cringepvp.managers.PlayerCache.updateCringeUser
import de.moltenKt.core.extension.data.randomInt
import de.moltenKt.unfold.text
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.event.player.PlayerLoadEvent
import me.neznamy.tab.api.team.UnlimitedNametagManager
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.spigotmc.event.player.PlayerSpawnLocationEvent
import kotlin.math.ceil
import kotlin.math.roundToInt


class CringeListener : Listener {

    @EventHandler
    fun onChat(event: PlayerCommandPreprocessEvent) = with(event) {
        if(message.startsWith("/plugins")) {
            player.sendMessage(text("<red>PluginsHide »</red> <red>Du darfst keine Plugins anschauen!</red>"))
            event.isCancelled = true
        } else if(message == "/pl") {
            player.sendMessage(text("<dark_purple><i><u>NoPlugins</u></i>: <dark_red><b>Du darfst Plugins nicht anschauen!</b></dark_red>"))
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) = with(event) {
        player.onSpawn()
        joinMessage(text("<#7bed9f>${event.player.name} betritt unsere Cringeschlacht."))
    }

    @EventHandler
    fun onRespawnEvent(event: PlayerRespawnEvent) = with(event) {
        player.onSpawn()
    }

    @EventHandler
    fun onPlayerSpawn(event: PlayerSpawnLocationEvent) = with(event) {
        spawnLocation = player.world.spawnLocation
        player.onSpawn()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) = with(event) {
        PlayerCache.remove(player.uniqueId)
        quitMessage(text("<#ff6b6b>${event.player.name} verlässt unsere Cringeschlacht."))
    }

    @EventHandler
    fun onHealthRegain(event: EntityRegainHealthEvent) {
        val entity = event.entity
        val healthAmount = event.amount

        ParticleBuilder(Particle.HEART)
            .location(entity.location)
            .count(ceil(5 * healthAmount.let { if (it >= 1) it else 1.0 }).roundToInt())
            .offset(.35, .5, .35)
            .extra(.01)
            .spawn()
    }

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.entity !is LivingEntity) return

        val entity = event.entity as LivingEntity

        entity.world.playSound(entity.location, Sound.BLOCK_BONE_BLOCK_BREAK, 1F, 2F)

        entity.world.playSound(entity.location, Sound.BLOCK_STONE_BREAK, 1F, 2F)

        ParticleBuilder(Particle.BLOCK_CRACK)
            .data(Material.REDSTONE_BLOCK.createBlockData())
            .count(ceil(15 * (event.damage.let { if (it >= 1) it else 1.0 })).roundToInt())
            .location(entity.location)
            .offset(.5, .75, .5)
            .extra(.01)
            .spawn()

        if (event.damager is Player && (event.entity as LivingEntity).health - event.damage <= 0) {
            (event.damager as Player).giveExp(2)
            var cringeUser = (event.damager as Player).toCringeUser()
            cringeUser = cringeUser.copy(kills = cringeUser.kills + 1, xp = cringeUser.xp + 2, coins = cringeUser.coins + randomInt(1 .. 3))
            updateCringeUser(cringeUser)

            if (event.entity is Player) {
                var deadCringeUser = (event.entity as Player).toCringeUser()
                deadCringeUser = deadCringeUser.copy(deaths = deadCringeUser.deaths + 1)
                updateCringeUser(deadCringeUser)
            }
        }
    }

    private fun Player.onSpawn() {
        getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.let { it.baseValue = 9999.0 }

        val cringeUser = toCringeUser()
        getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.let { it.baseValue = cringeUser.baseAttack }
        getAttribute(Attribute.GENERIC_ARMOR)?.let { it.baseValue = cringeUser.baseDefense }
        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.let { it.baseValue = cringeUser.baseSpeed }
        getAttribute(Attribute.GENERIC_MAX_HEALTH)?.let { it.baseValue = cringeUser.baseHealth }

        exp = 0.0f
        giveExp(cringeUser.xp.toInt())

        world.spawnParticle(Particle.EXPLOSION_HUGE, world.spawnLocation, 100, 0.0, 0.0, 0.0, 20.0)
        playSound(location, Sound.BLOCK_PORTAL_TRAVEL, 1f, 1f)
    }
}