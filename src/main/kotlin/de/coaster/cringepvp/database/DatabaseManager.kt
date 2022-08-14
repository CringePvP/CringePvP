package de.coaster.cringepvp.database

import de.moltenKt.core.tool.timing.calendar.Calendar
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

internal object DatabaseManager {
    val database = Database.connect("jdbc:mysql://45.9.60.102:3306/s305_cringepvp", "com.mysql.cj.jdbc.Driver", "u305_b78Gdf4psJ", "bRXDJIRor0=+y4FqVGNd+G@I")
}

object TableUsers : Table("users") {
    val userUUID = varchar("uuid", 45)
    val userName = varchar("username", 24)
    val userXP = long("xp").default(0)
    val userRank = varchar("rank", 64).default("Spieler")
    val userTitle = varchar("title", 64).default("")

    val userCoins = long("coins").default(0)
    val userGems = long("gems").default(0)
    val userCrystals = long("crystals").default(0)
    val userRelicts = long("relicts").default(0)

    val userKills = long("kills").default(0)
    val userDeaths = long("deaths").default(0)

    val userBaseAttack = double("base_attack").default(2.0)
    val userBaseDefense = double("base_defense").default(0.0)
    val userBaseSpeed = double("base_speed").default(0.098)
    val userBaseHealth = double("base_health").default(20.0)

    val userVotes = long("votes").default(0)

    val userVoteKeys = long("vote_keys").default(0)
    val userVipKeys = long("vip_keys").default(0)
    val userNormalKeys = long("normal_keys").default(0)
    val userPremiumKeys = long("premium_keys").default(0)
    val userUltimateKeys = long("ultimate_keys").default(0)
    val userEpicKeys = long("epic_keys").default(0)
    val userLegendaryKeys = long("legendary_keys").default(0)
    val userMythicalKeys = long("mythical_keys").default(0)
    val userAncientKeys = long("ancient_keys").default(0)
    val userDivineKeys = long("divine_keys").default(0)
    val userImmortalKeys = long("immortal_keys").default(0)

    val userFirstJoined = timestamp("firstJoined").default(Calendar.now().javaInstant)
    val userLastJoined = timestamp("lastTimeOnline").default(Calendar.now().javaInstant)
    val onlineTime = long("onlineTime").default(0)

    override val primaryKey = PrimaryKey(userUUID)
}

internal fun <T> smartTransaction(block: Transaction.() -> T): T {

    return transaction {
        //addLogger(StdOutSqlLogger)
        return@transaction block()
    }
}