package de.coaster.cringepvp.database.model

import de.moltenKt.core.tool.smart.identification.Identifiable
import de.moltenKt.core.tool.timing.calendar.Calendar
import java.util.*
import kotlin.time.Duration

data class CringeUser(val uuid: UUID,
                      val username: String,

                      val xp: Long = 0,
                      val rank: String = "Spieler",

                      val coins: Long = 0,
                      val gems: Long = 0,
                      val crystals: Long = 0,
                      val relicts: Long = 0,

                      val kills: Long = 0,
                      val deaths: Long = 0,

                      val baseAttack : Double = 2.0, // Max: 2048
                      val baseDefense : Double = 0.0, // Max: 30
                      val baseSpeed : Double = 0.7, // Max: 1024
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
) : Identifiable<CringeUser>