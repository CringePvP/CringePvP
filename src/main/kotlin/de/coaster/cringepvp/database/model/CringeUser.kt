package de.coaster.cringepvp.database.model

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
                      val rank: String = "Spieler",
                      val title: String = "",

                      val coins: Long = 0,
                      val gems: Long = 0,
                      val crystals: Long = 0,
                      val relicts: Long = 0,

                      val kills: Long = 0,
                      val deaths: Long = 0,

                      val baseAttack : Double = 2.0, // Max: 2048
                      val baseDefense : Double = 0.0, // Max: 30
                      val baseSpeed : Double = 0.098, // Max: 1024
                      val baseHealth : Double = 20.0, // Max: 1024

                      val votes: Long = 0,

                      val voteKeys: Long = 0,
                      val vipKeys: Long = 0,
                      val normalKeys: Long = 0,
                      val premiumKeys: Long = 0,
                      val ultimateKeys: Long = 0,
                      val epicKeys: Long = 0,
                      val legendaryKeys: Long = 0,
                      val mythicalKeys: Long = 0,
                      val ancientKeys: Long = 0,
                      val divineKeys: Long = 0,
                      val immortalKeys: Long = 0,

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