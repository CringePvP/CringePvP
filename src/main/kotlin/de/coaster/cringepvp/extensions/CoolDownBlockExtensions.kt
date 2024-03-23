package de.coaster.cringepvp.extensions

import de.coaster.cringepvp.database.model.CringeUser
import de.coaster.cringepvp.enums.Ranks
import dev.fruxz.ascend.tool.time.calendar.Calendar
import org.bukkit.entity.Player
import kotlin.time.Duration

private var cooldownBlocks = mapOf<Any, Calendar>()

fun Any.isInCooldown(): Boolean {
    return cooldownBlocks.containsKey(this) && cooldownBlocks[this]!!.inFuture
}

fun Any.setCooldown(duration: Duration) {
    cooldownBlocks += this to Calendar.now().plus(duration)
}

fun Any.removeCooldown() {
    cooldownBlocks -= this
}

fun Any.getCooldown(): Duration? {
    return cooldownBlocks[this]?.durationFromNow()
}

fun Player.isInCooldown(cooldownKey: String): Boolean {
    return "$uniqueId:$cooldownKey".isInCooldown()
}

fun Player.setCooldown(cooldownKey: String, duration: Duration) {
    "$uniqueId:$cooldownKey".setCooldown(duration)
}

fun CringeUser.setCooldown(cooldownKey: String, duration: Duration) {
    "$uuid:$cooldownKey".setCooldown(duration.div((Ranks.values().find { it.name == rank.name }?: Ranks.Spieler).cooldownMultiplier))
}

fun Player.removeCooldown(cooldownKey: String) {
    "$uniqueId:$cooldownKey".removeCooldown()
}

fun Player.getCooldown(cooldownKey: String): Duration? {
    return "$uniqueId:$cooldownKey".getCooldown()
}