package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.createCringeUser
import de.coaster.cringepvp.database.getCringeUserOrNull
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.managers.PlayerCache
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

fun Player.toCringeUser(): CringeUser {
    return PlayerCache.get(uniqueId)
}

fun UUID.toCringeUser(): CringeUser {
    return PlayerCache.get(this)
}

fun UUID.toCringeUserDB(): CringeUser {
    return getCringeUserOrNull(this) ?: createCringeUser(CringeUser(this, Bukkit.getPlayer(this)?.name ?: Bukkit.getOfflinePlayer(this).name ?: "Unknown"))
}

fun broadcastActionbar(component: Component) {
    Bukkit.getOnlinePlayers().forEach { it.sendActionBar(component) }
}

fun Location.toCringeString(): String {
    return "${world.name}_${x}_${y}_${z}"
}

fun String.toCringeLocation(): Location {
    val split = this.split("_")
    return Location(Bukkit.getWorld(split[0]), split[1].toDouble(), split[2].toDouble(), split[3].toDouble())
}

fun ItemStack.setReceiver(player: Player): ItemStack {
    this.editMeta { meta -> meta.persistentDataContainer.set(NamespacedKey.minecraft("pickup-receiver"), PersistentDataType.STRING, player.uniqueId.toString()) }
    return this
}

fun ItemStack.getReceiver(): UUID? {
    return this.itemMeta?.persistentDataContainer?.get(NamespacedKey.minecraft("pickup-receiver"), PersistentDataType.STRING)?.let { UUID.fromString(it) }
}