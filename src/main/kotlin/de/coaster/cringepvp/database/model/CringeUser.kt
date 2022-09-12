package de.coaster.cringepvp.database.model

import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Titles
import de.coaster.cringepvp.extensions.Currency
import de.coaster.cringepvp.extensions.abbreviate
import de.coaster.cringepvp.extensions.div
import de.coaster.cringepvp.utils.getCurrentProfit
import de.coaster.cringepvp.utils.getCurrentSpeed
import de.moltenKt.core.extension.data.randomInt
import de.moltenKt.core.tool.smart.identification.Identifiable
import de.moltenKt.core.tool.timing.calendar.Calendar
import java.util.*
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.time.Duration

data class CringeUser(val uuid: UUID,
                      val username: String,
                      val xp: Long = 0,
                      val rank: Ranks = Ranks.Spieler,
                      val title: Titles = Titles.NoTITLE,
                      val ownedTitles: Set<Titles> = setOf(),

                      var coins: Currency = Currency(0.0, 0),
                      var gems: Currency = Currency(0.0, 0),
                      var crystals: Currency = Currency(0.0, 0),
                      var relicts: Currency = Currency(0.0, 0),

                      val kills: Long = 0,
                      val deaths: Long = 0,

                      var baseAttack : Double = 2.0, // Max: 2048
                      var baseDefense : Double = 0.0, // Max: 30
                      var baseSpeed : Double = 0.098, // Max: 1024
                      var baseHealth : Double = 20.0, // Max: 1024

                      val votes: Long = 0,

                      var voteKeys: Long = 0,
                      var vipKeys: Long = 0,
                      var normalKeys: Long = 0,
                      var premiumKeys: Long = 0,
                      var ultimateKeys: Long = 0,
                      var epicKeys: Long = 0,
                      var legendaryKeys: Long = 0,
                      var mythicalKeys: Long = 0,
                      var ancientKeys: Long = 0,
                      var divineKeys: Long = 0,
                      var immortalKeys: Long = 0,

                      // Idle Castle Stuff
                      val steinbruchLevel: Long = 0,

                      val firstJoined: Calendar = Calendar.now(),
                      val lastTimeJoined: Calendar = Calendar.now(),
                      val onlineTime: Duration = Duration.ZERO,
                      override val identity: String = "$uuid",
) : Identifiable<CringeUser> {
    val level: Int
        get() {
            return ((this.xp.toDouble() / 100.toDouble()).pow(0.6)).toInt()
        }

    val steinBruchIdle: Currency
        get() {
            val steinbruchProfit = getCurrentProfit(Currency(0.25, 0), steinbruchLevel)
            val steinbruchSpeed = getCurrentSpeed(4, steinbruchLevel)
            return (steinbruchProfit)
        }

    val idleCash: Currency
        get() {
            return steinBruchIdle
        }

    fun nextLevelExp(forLevel: Int = level+1): Long {
        return (exp(ln(forLevel.toDouble()) / 0.6) * 100).toLong()
    }

    val attackLevel: Int
        get() {
            return baseAttack.toInt() - 1
        }

    fun upgradeToNextAttack() {
        baseAttack = getNextAttack()
    }

    fun getNextAttack(): Double {
        if(baseAttack == 2048.0) return 2048.0
        return (baseAttack + 1)
    }

    fun getPriceForNextAttack(): Currency {
        return (attackLevel * 100).toLong() abbreviate attackLevel
    }

    val defenseLevel: Int
        get() {
            return (baseDefense * 2).toInt() + 1
        }

    fun upgradeToNextDefense() {
        baseDefense = getNextDefense()
    }

    fun getNextDefense(): Double {
        if(baseDefense == 30.0) return 30.0
        return (baseDefense + 0.5)
    }

    fun getPriceForNextDefense(): Currency {
        return (defenseLevel * 100).toLong() abbreviate defenseLevel
    }

    val speedLevel: Int
        get() {
            return (((baseSpeed - 0.098) * 1000).toInt()) + 1
        }

    fun upgradeToNextSpeed() {
        baseSpeed = getNextSpeed()
    }

    fun getNextSpeed(): Double {
        if(baseSpeed == 1.024) return 1.024
        return (baseSpeed + 0.001)
    }

    fun getPriceForNextSpeed(): Currency {
        return ((speedLevel * 100).toLong() / 10) abbreviate speedLevel
    }

    val healthLevel: Int
        get() {
            return ((baseHealth - 20) * 2).toInt() + 1
        }

    fun upgradeToNextHealth() {
        baseHealth = getNextHealth()
    }

    fun getNextHealth(): Double {
        if(baseHealth == 1024.0) return 1024.0
        return (baseHealth + 0.5)
    }

    fun getPriceForNextHealth(): Currency {
        return ((healthLevel * 100).toLong() / 10) abbreviate healthLevel
    }
}