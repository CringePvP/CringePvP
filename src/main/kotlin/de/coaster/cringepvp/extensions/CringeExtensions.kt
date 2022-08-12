package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.createCringeUser
import de.coaster.cringepvp.database.getCringeUserOrNull
import de.coaster.cringepvp.database.model.CringeUser
import org.bukkit.entity.Player

fun Player.toCringeUser(): CringeUser {
    return getCringeUserOrNull(this.uniqueId) ?: createCringeUser(CringeUser(this.uniqueId, this.name))
}