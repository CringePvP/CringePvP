package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.createCringeUser
import de.coaster.cringepvp.database.getCringeUserOrNull
import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.managers.PlayerCache
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
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