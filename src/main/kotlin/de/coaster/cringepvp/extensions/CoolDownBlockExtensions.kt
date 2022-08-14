package de.coaster.cringepvp.extensions

import de.moltenKt.core.tool.timing.calendar.Calendar
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

fun Player.removeCooldown(cooldownKey: String) {
    "$uniqueId:$cooldownKey".removeCooldown()
}

fun Player.getCooldown(cooldownKey: String): Duration? {
    return "$uniqueId:$cooldownKey".getCooldown()
}