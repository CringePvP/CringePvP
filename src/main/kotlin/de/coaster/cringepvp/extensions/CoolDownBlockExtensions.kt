package de.coaster.cringepvp.extensions

import de.moltenKt.core.tool.timing.calendar.Calendar
import org.bukkit.Location
import org.bukkit.block.Block
import kotlin.time.Duration

private var cooldownBlocks = mapOf<Location, Calendar>()

fun Location.isInCooldown(): Boolean {
    return cooldownBlocks.containsKey(this) && cooldownBlocks[this]!!.inFuture
}

fun Location.setCooldown(duration: Duration) {
    cooldownBlocks += this to Calendar.now().plus(duration)
}

fun Location.removeCooldown() {
    cooldownBlocks -= this
}

fun Location.getCooldown(): Duration {
    return cooldownBlocks[this]!!.durationFromNow()
}

fun Block.isInCooldown(): Boolean {
    return location.isInCooldown()
}

fun Block.setCooldown(duration: Duration) {
    location.setCooldown(duration)
}

fun Block.removeCooldown() {
    location.removeCooldown()
}

fun Block.getCooldown(): Duration {
    return location.getCooldown()
}