package de.coaster.cringepvp.database.model

import de.coaster.cringepvp.enums.Ranks
import de.coaster.cringepvp.enums.Titles
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

                      var coins: Long = 0,
                      var gems: Long = 0,
                      var crystals: Long = 0,
                      var relicts: Long = 0,

                      val kills: Long = 0,
                      val deaths: Long = 0,

                      val baseAttack : Double = 2.0, // Max: 2048
                      val baseDefense : Double = 0.0, // Max: 30
                      val baseSpeed : Double = 0.098, // Max: 1024
                      val baseHealth : Double = 20.0, // Max: 1024

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

                      val firstJoined: Calendar = Calendar.now(),
                      val lastTimeJoined: Calendar = Calendar.now(),
                      val onlineTime: Duration = Duration.ZERO,
                      override val identity: String = "$uuid",
) : Identifiable<CringeUser> {
    val level: Int
        get() {
            return ((this.xp.toDouble() / 100.toDouble()).pow(0.6)).toInt()
        }

    fun nextLevelExp(forLevel: Int = level+1): Long {
        return (exp(ln(forLevel.toDouble()) / 0.6) * 100).toLong()
    }
}