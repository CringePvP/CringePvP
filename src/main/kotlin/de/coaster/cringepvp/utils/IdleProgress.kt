package de.coaster.cringepvp.utils

import de.coaster.cringepvp.extensions.Currency
import de.coaster.cringepvp.extensions.plus
import de.coaster.cringepvp.extensions.times
import de.moltenKt.core.extension.math.floor
import kotlin.math.max
import kotlin.math.pow

// LevelProgression
// 1 - 9 = 1x
// 10 - 24 = 2x
// 25 - 49 = 4x
// From 50 the multiplier gets x2 for every 50 levels
fun getCurrentProfit(baseValue: Currency, level: Long): Currency {

    val multiplier = when {
        level == 0L -> 0
        level < 10 -> 1
        level < 25 -> 2
        level < 50 -> 4
        else -> 2.0.pow(3.0 + ((level - 50) / 50.0).floor()).toLong()
    }

    return (baseValue * level) * multiplier
}

fun getCurrentSpeed(baseValue: Long, level: Long): Long {

    val multiplier = when {
        level < 25 -> 1
        level < 100 -> 2
        else -> 2.0.pow(2.0 + (level - 100) / 100.0).toLong()
    }

    return max(1, baseValue / multiplier)
}