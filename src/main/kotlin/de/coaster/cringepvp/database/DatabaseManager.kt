package de.coaster.cringepvp.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.coaster.cringepvp.extensions.getLogger
import de.coaster.cringepvp.utils.Environment
import dev.fruxz.ascend.tool.time.calendar.Calendar
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

internal object DatabaseManager {
    /**
     * Configuration class for the database connection.
     */
    private val dbConfig = HikariConfig().apply {
        jdbcUrl = Environment.getEnv("DATABASE_URL")
        driverClassName =Environment.getEnv("DATABASE_DRIVER")
        username = Environment.getEnv("DATABASE_USER")
        password = Environment.getEnv("DATABASE_PASSWORD")
        maximumPoolSize = 10
    }
    /**
     * Represents a database connection.
     *
     * This variable is used to establish a connection to the database using the provided database configuration.
     * It is a private property, so it can only be accessed within the scope of its containing class.
     *
     * @property database The database connection instance.
     */
    private val database = Database.connect(HikariDataSource(dbConfig))

    /**
     * Connects to the database and performs necessary operations.
     */
    fun connect() {
        getLogger().info("Connecting to database...")
        database

        getLogger().info("Check for table updates...")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                TableUsers, TableUserTitles
            )
        }

        getLogger().info("Connected to database.")
    }

    /**
     * Disconnects from the database.
     * This method closes the database connection and logs a message indicating disconnection.
     */
    fun disconnect() {
        database.connector().close()
        getLogger().info("Disconnected from database.")
    }
}

object TableUsers : Table("users") {
    val userUUID = varchar("uuid", 45)
    val userName = varchar("username", 24)
    val userXP = long("xp").default(0)
    val userRank = varchar("rank", 64).default("Spieler")
    val userTitle = varchar("title", 64).default("")

    val userCoins = double("coins").default(0.0)
    val userCoinsAbbreviatedIndex = integer("coinsAbbreviatedIndex").default(0)
    val userGems = double("gems").default(0.0)
    val userGemsAbbreviatedIndex = integer("gemsAbbreviatedIndex").default(0)
    val userCrystals = double("crystals").default(0.0)
    val userCrystalsAbbreviatedIndex = integer("crystalsAbbreviatedIndex").default(0)
    val userRelicts = double("relicts").default(0.0)
    val userRelictsAbbreviatedIndex = integer("relictsAbbreviatedIndex").default(0)

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

    // Idle Castle Stuff
    val steinbruchLevel = long("steinbruch_level").default(0)

    val userFirstJoined = timestamp("firstJoined").default(Calendar.now().javaInstant)
    val userLastJoined = timestamp("lastTimeOnline").default(Calendar.now().javaInstant)
    val onlineTime = long("onlineTime").default(0)

    override val primaryKey = PrimaryKey(userUUID)
}

object TableUserTitles : Table("user_titles") {
    val userUUID = varchar("uuid", 45) references TableUsers.userUUID
    val title = varchar("title", 64)
    val ownedDate = timestamp("ownedDate").default(Calendar.now().javaInstant)

    override val primaryKey = PrimaryKey(userUUID, title)
}

internal fun <T> smartTransaction(block: Transaction.() -> T): T {

    return transaction {
        //addLogger(StdOutSqlLogger)
        return@transaction block()
    }
}